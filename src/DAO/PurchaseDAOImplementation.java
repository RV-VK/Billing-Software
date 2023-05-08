package DAO;
import DBConnection.DBHelper;
import Entity.Product;
import Entity.Purchase;
import Entity.PurchaseItem;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public int count(String paramter) {
        return 0;
    }

    @Override
    public List list(int pageLength, int pageNumber) {
        return null;
    }

    @Override
    public List list(String attribute, String searchText, int pageLength, int pageNumber) {
        return null;
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
