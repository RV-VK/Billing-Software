package DAO;

import CLIController.SalesCLI;
import DBConnection.DBHelper;
import Entity.Product;
import Entity.Sales;
import Entity.SalesItem;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SalesDAOImplementation implements SalesDAO {
    @Override
    public Sales create(Sales sales) throws ApplicationErrorException {
        Connection salesCreateConnection= DBHelper.getConnection ();
        try {
            salesCreateConnection.setAutoCommit (false);
            String salesEntryQuery = "INSERT INTO SALES(DATE,GRANDTOTAL) VALUES(?,?) RETURNING *";
            String salesItemInsertQuery= "INSERT INTO SALESITEM (ID, PRODUCTCODE, QUANTITY, SALESPRICE) VALUES (?,?,?) RETURNING *";
            String salesPriceQuery="SELECT PRICE,STOCK FROM PRODUCT WHERE CODE=?";
            String stockUpdateQuery="UPDATE PRODUCT SET STOCK=STOCK-? WHERE CODE=? RETURNING NAME";
            PreparedStatement salesEntryStatement=salesCreateConnection.prepareStatement (salesEntryQuery);
            PreparedStatement salesItemInsertStatement=salesCreateConnection.prepareStatement (salesItemInsertQuery);
            PreparedStatement salesPriceStatement=salesCreateConnection.prepareStatement (salesPriceQuery);
            PreparedStatement stockUpdateStatement=salesCreateConnection.prepareStatement (stockUpdateQuery);
            salesEntryStatement.setDate (1, Date.valueOf (sales.getDate ()));
            salesEntryStatement.setDouble (2,sales.getGrandTotal ());
            ResultSet salesEntryResultSet=salesEntryStatement.executeQuery ();
            Sales salesEntry=new Sales ();
            while(salesEntryResultSet.next ()) {
                salesEntry.setId (salesEntryResultSet.getInt (1));
                salesEntry.setDate (String.valueOf (salesEntryResultSet.getDate (2)));
                salesEntry.setGrandTotal (salesEntryResultSet.getDouble (3));
            }
            List< SalesItem > salesItemList=new ArrayList<> ();
            ResultSet salesItemInsertResultSet;
            double price;
            float stock;
            String productName;
            for(SalesItem salesItem:sales.getSalesItemList ())
            {
                salesPriceStatement.setString (1,salesItem.getProduct ().getCode ());
                ResultSet salesPriceResultSet=salesPriceStatement.executeQuery ();
                salesPriceResultSet.next ();
                price=salesPriceResultSet.getDouble (1);
                stock=salesPriceResultSet.getFloat (2);
                if(stock<salesItem.getQuantity ())
                {
                    salesCreateConnection.rollback ();
                    return null;
                }
                salesItemInsertStatement.setInt (1,sales.getId ());
                salesItemInsertStatement.setString (2,salesItem.getProduct ().getCode ());
                salesItemInsertStatement.setFloat (3,salesItem.getQuantity ());
                salesItemInsertStatement.setDouble (4,price);
                salesItemInsertResultSet=salesItemInsertStatement.executeQuery ();
                stockUpdateStatement.setFloat(1,salesItem.getQuantity ());
                stockUpdateStatement.setString (2,salesItem.getProduct ().getCode ());
                ResultSet stockUpdateResultSet=stockUpdateStatement.executeQuery ();
                stockUpdateResultSet.next ();
                productName=stockUpdateResultSet.getString (1);
                while(salesItemInsertResultSet.next ())
                {
                    salesItemList.add(new SalesItem (new Product (salesItemInsertResultSet.getString (2),productName),salesItemInsertResultSet.getFloat (3),salesItemInsertResultSet.getDouble (4)));
                }
            }
            salesEntry.setSalesItemList (salesItemList);
            salesCreateConnection.commit ();
            return salesEntry;
        }
        catch ( Exception e)
        {
                throw new ApplicationErrorException (e.getMessage ());
        }
    }

    @Override
    public int count(String parameter) {
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
    public int delete(int id) {
        return 0;
    }
}
