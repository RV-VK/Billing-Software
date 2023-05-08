package DAO;
import DBConnection.DBHelper;
import Entity.Product;
import Entity.Purchase;
import Entity.PurchaseItem;
import org.checkerframework.checker.units.qual.A;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAOImplementation implements PurchaseDAO{
    @Override
    public Purchase create(Purchase purchase) throws ApplicationErrorException {
        Connection purchaseCreateConnection= DBHelper.getConnection();
        try{
            purchaseCreateConnection.setAutoCommit(false);
            String purchaseEntryQuery="INSERT INTO PURCHASE(DATE,INVOICE,GRANDTOTAL) VALUES(?,?,?) RETURNING *";
            String purchaseItemInsertQuery="INSERT INTO PURCHASEITEMS(INVOICE,PRODUCTCODE,QUANTITY,COSTPRICE) VALUES(?,?,?,?) RETURNING *";
            String stockUpdateQuery="UPDATE PRODUCT SET STOCK=STOCK+? WHERE CODE=?";
            PreparedStatement stockUpdateStatement=purchaseCreateConnection.prepareStatement(stockUpdateQuery);
            PreparedStatement purchaseEntryStatement=purchaseCreateConnection.prepareStatement(purchaseEntryQuery);
            purchaseEntryStatement.setDate(1, Date.valueOf(purchase.getDate()));
            purchaseEntryStatement.setInt(2,purchase.getInvoice());
            purchaseEntryStatement.setDouble(3,purchase.getGrandTotal());
            ResultSet purchaseEntryResultSet=purchaseEntryStatement.executeQuery();
            Purchase purchaseEntry=new Purchase();
            while(purchaseEntryResultSet.next())
            {
                purchaseEntry.setId(purchaseEntryResultSet.getInt(1));
                purchaseEntry.setDate(String.valueOf(purchaseEntryResultSet.getDate(2)));
                purchaseEntry.setInvoice(purchaseEntryResultSet.getInt(3));
                purchaseEntry.setGrandTotal(purchaseEntryResultSet.getDouble(4));
            }
            List<PurchaseItem> purchaseItemList=new ArrayList<>();
            PreparedStatement purchaseItemInsertStatement=purchaseCreateConnection.prepareStatement(purchaseItemInsertQuery);
            ResultSet purchaseItemInsertResultSet;
            for(PurchaseItem purchaseItem:purchase.getPurchaseItemList()) {
                purchaseItemInsertStatement.setInt(1, purchase.getInvoice());
                purchaseItemInsertStatement.setString(2, purchaseItem.getProduct().getCode());
                purchaseItemInsertStatement.setFloat(3, purchaseItem.getQuantity());
                purchaseItemInsertStatement.setDouble(4, purchaseItem.getUnitPurchasePrice());
                purchaseItemInsertResultSet = purchaseItemInsertStatement.executeQuery();
                stockUpdateStatement.setFloat(1,purchaseItem.getQuantity());
                stockUpdateStatement.setString(2,purchaseItem.getProduct().getCode());
                if(!(stockUpdateStatement.executeUpdate()>0)){
                    return null;
                }
                while (purchaseItemInsertResultSet.next()){
                    purchaseItemList.add(new PurchaseItem(purchaseItem.getProduct(),purchaseItemInsertResultSet.getFloat(3),purchaseItemInsertResultSet.getDouble(4)));
                }
                purchaseEntry.setPurchaseItemList(purchaseItemList);
            }
            return purchaseEntry;
        }catch(Exception e)
        {
            throw new ApplicationErrorException(e.getMessage());
        }
    }

    @Override
    public int count(String parameter) throws ApplicationErrorException {
        Connection getCountConnection=DBHelper.getConnection();
        try{
            if(parameter==null)
            {
                Statement countStatement=getCountConnection.createStatement();
                ResultSet countResultSet=countStatement.executeQuery("SELECT COUNT(ID) FROM PURCHASE");
                int count;
                countResultSet.next();
                count=countResultSet.getInt(1);
                return count;
            }
            else{
                Statement countStatement=getCountConnection.createStatement();
                ResultSet countResultSet=countStatement.executeQuery("SELECT COUNT(*) WHERE DATE ILIKE'"+parameter+"' OR TYPE ILIKE'"+parameter+"'");
                int count;
                countResultSet.next();
                count=countResultSet.getInt(1);
                return count;
            }
        }
        catch (Exception e){
            throw new ApplicationErrorException(e.getMessage());
        }
    }

    @Override
    public List list(int pageLength, int pageNumber) throws ApplicationErrorException, PageCountOutOfBoundsException {
        Connection listConnection=DBHelper.getConnection();
        List<Purchase> purchaseList=new ArrayList<>();
        int count=0;
        try{
            Statement countStatement= listConnection.createStatement();
            ResultSet countResultSet=countStatement.executeQuery("SELECT COUNT(ID) FROM PRODUCT");
            countResultSet.next();
            count=countResultSet.getInt(1);
        }catch(Exception e)
        {
            throw  new ApplicationErrorException(e.getMessage());
        }
        if(count<=((pageLength*pageNumber)-pageLength))
        {
            throw new PageCountOutOfBoundsException(">> Requested page doesnt exist !!!\\n>> Existing page count with given pagination \""+((count/pageLength)+1));
        }
        else{
            try{
                int begin=(pageLength*pageNumber)-pageLength;
                Statement listStatement=listConnection.createStatement();
                ResultSet listResultSet=listStatement.executeQuery("SELECT * FROM PURCHASE ORDER BY ID LIMIT "+pageLength+" OFFSET "+begin);
                while(listResultSet.next())
                {
                    Purchase listedPurchase=new Purchase();
                    listedPurchase.setId(listResultSet.getInt(1));
                    listedPurchase.setDate(String.valueOf(listResultSet.getDate(2)));
                    listedPurchase.setInvoice(listResultSet.getInt(3));
                    listedPurchase.setGrandTotal(listResultSet.getInt(4));
                    purchaseList.add(listedPurchase);
                }
                PreparedStatement listPurchaseItemsStatement=listConnection.prepareStatement("SELECT * FROM PURCHASEITEMS WHERE INVOICE=?");
                List<PurchaseItem> purchaseItemList=new ArrayList<>();
                for(Purchase purchase:purchaseList)
                {
                    listPurchaseItemsStatement.setInt(1,purchase.getInvoice());
                    ResultSet listPurchaseResultSet=listPurchaseItemsStatement.executeQuery();
                    while(listPurchaseResultSet.next())
                    {
                        purchaseItemList.add(new PurchaseItem(new Product(listPurchaseResultSet.getString(2)),listPurchaseResultSet.getFloat(3),listPurchaseResultSet.getDouble(4)));
                    }
                    purchase.setPurchaseItemList(purchaseItemList);
                }
                return purchaseList;

            }catch (Exception e)
            {
                throw new ApplicationErrorException(e.getMessage());
            }
        }
    }

    @Override
    public List list(String attribute, String searchText, int pageLength, int offset) throws ApplicationErrorException {
        Connection listConnection=DBHelper.getConnection();
        List<Purchase> purchaseList=new ArrayList<>();
        try{
            Statement listStatement=listConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String listQuery="SELECT * FROM PURCHASE WHERE "+attribute+" = '"+searchText+"' ORDER BY ID LIMIT "+pageLength+" OFFSET "+offset;
            ResultSet listResultSet=listStatement.executeQuery(listQuery);
            if(listResultSet.next())
            {
                listResultSet.beforeFirst();
                while(listResultSet.next())
                {
                    Purchase listedPurchase=new Purchase();
                    listedPurchase.setId(listResultSet.getInt(1));
                    listedPurchase.setDate(String.valueOf(listResultSet.getDate(2)));
                    listedPurchase.setInvoice(listResultSet.getInt(3));
                    listedPurchase.setGrandTotal(listResultSet.getInt(4));
                    purchaseList.add(listedPurchase);
                }
                PreparedStatement listPurchaseItemsStatement=listConnection.prepareStatement("SELECT * FROM PURCHASEITEMS WHERE INVOICE=?");
                List<PurchaseItem> purchaseItemList=new ArrayList<>();
                for(Purchase purchase:purchaseList)
                {
                    listPurchaseItemsStatement.setInt(1,purchase.getInvoice());
                    ResultSet listPurchaseResultSet=listPurchaseItemsStatement.executeQuery();
                    while(listPurchaseResultSet.next())
                    {
                        purchaseItemList.add(new PurchaseItem(new Product(listPurchaseResultSet.getString(2)),listPurchaseResultSet.getFloat(3),listPurchaseResultSet.getDouble(4)));
                    }
                    purchase.setPurchaseItemList(purchaseItemList);
                }
                return purchaseList;

            }
            else{
                return null;
            }

        }
        catch(Exception e)
        {
            throw new ApplicationErrorException(e.getMessage());
        }

    }

    @Override
    public List list(String searchText) {
        return null;
    }

    @Override
    public int delete(int invoice) {
        return 0;
    }
}
