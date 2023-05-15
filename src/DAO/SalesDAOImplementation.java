package DAO;

import DBConnection.DBHelper;
import Entity.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesDAOImplementation implements SalesDAO {
  Connection salesConnection = DBHelper.getConnection();
  @Override
  public Sales create(Sales sales) throws ApplicationErrorException, SQLException {
    try {
      salesConnection.setAutoCommit(false);
      String salesEntryQuery = "INSERT INTO SALES(DATE,GRANDTOTAL) VALUES(?,?) RETURNING *";
      String salesItemInsertQuery = "INSERT INTO SALESITEM (ID, PRODUCTCODE, QUANTITY, SALESPRICE) VALUES (?,?,?) RETURNING *";
      String salesPriceQuery = "SELECT PRICE,STOCK,NAME FROM PRODUCT WHERE CODE=?";
      String stockUpdateQuery = "UPDATE PRODUCT SET STOCK=STOCK-? WHERE CODE=?";
      String grandTotalUpdateQuery = "UPDATE SALES SET GRANDTOTAL=? WHERE ID=?";
      PreparedStatement salesEntryStatement = salesConnection.prepareStatement(salesEntryQuery);
      PreparedStatement salesItemInsertStatement = salesConnection.prepareStatement(salesItemInsertQuery);
      PreparedStatement salesPriceStatement = salesConnection.prepareStatement(salesPriceQuery);
      PreparedStatement stockUpdateStatement = salesConnection.prepareStatement(stockUpdateQuery);
      PreparedStatement grandTotalUpdateStatement = salesConnection.prepareStatement(grandTotalUpdateQuery);
      salesEntryStatement.setDate(1, Date.valueOf(sales.getDate()));
      salesEntryStatement.setDouble(2, sales.getGrandTotal());
      ResultSet salesEntryResultSet = salesEntryStatement.executeQuery();
      Sales salesEntry = new Sales();
      while (salesEntryResultSet.next()) {
        salesEntry.setId(salesEntryResultSet.getInt(1));
        salesEntry.setDate(String.valueOf(salesEntryResultSet.getDate(2)));
        salesEntry.setGrandTotal(salesEntryResultSet.getDouble(3));
      }
      List<SalesItem> salesItemList = new ArrayList<>();
      ResultSet salesItemInsertResultSet;
      double price;
      float stock;
      String productName;
      double grandTotal = 0.0;
      for (SalesItem salesItem : sales.getSalesItemList()) {
        salesPriceStatement.setString(1, salesItem.getProduct().getCode());
        ResultSet salesPriceResultSet = salesPriceStatement.executeQuery();
        salesPriceResultSet.next();
        price = salesPriceResultSet.getDouble(1);
        stock = salesPriceResultSet.getFloat(2);
        productName=salesPriceResultSet.getString(3);
        grandTotal += price * salesItem.getQuantity();
        if (stock < salesItem.getQuantity()) {
          salesConnection.rollback();
          return null;
        }
        salesItemInsertStatement.setInt(1, salesEntry.getId());
        salesItemInsertStatement.setString(2, salesItem.getProduct().getCode());
        salesItemInsertStatement.setFloat(3, salesItem.getQuantity());
        salesItemInsertStatement.setDouble(4, price);
        salesItemInsertResultSet = salesItemInsertStatement.executeQuery();
        stockUpdateStatement.setFloat(1, salesItem.getQuantity());
        stockUpdateStatement.setString(2, salesItem.getProduct().getCode());
        stockUpdateStatement.executeQuery();
        while (salesItemInsertResultSet.next()) {
          salesItemList.add(new SalesItem(new Product(salesItemInsertResultSet.getString(2), productName), salesItemInsertResultSet.getFloat(3), salesItemInsertResultSet.getDouble(4)));
        }
      }
      grandTotalUpdateStatement.setDouble(1, grandTotal);
      grandTotalUpdateStatement.setInt(2, salesEntry.getId());
      grandTotalUpdateStatement.executeUpdate();
      salesEntry.setGrandTotal(grandTotal);
      salesEntry.setSalesItemList(salesItemList);
      salesConnection.commit();
      salesConnection.setAutoCommit(true);
      return salesEntry;
    } catch (SQLException e) {
      salesConnection.rollback();
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  @Override
  public int count(String parameter) throws ApplicationErrorException {
    int count;
    try {
      Statement countStatement = salesConnection.createStatement();

      if (parameter == null) {
        ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(ID) FROM SALES");
        countResultSet.next();
        count = countResultSet.getInt(1);
        return count;
      } else {
        ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(*) FROM PURCHASE WHERE CAST(DATE AS TEXT) ILIKE'" + parameter + "'");
        countResultSet.next();
        count = countResultSet.getInt(1);
        return count;
      }
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }
  @Override
  public List list(String attribute, String searchText, int pageLength, int offset) throws ApplicationErrorException {
    List<Sales> salesList = new ArrayList<>();
    try {
      String listQuery ="SELECT * FROM SALES WHERE " + attribute + "= COALESCE(?," + attribute + ")" + " ORDER BY ID LIMIT " + pageLength + "  OFFSET " + offset;
      PreparedStatement listStatement = salesConnection.prepareStatement(listQuery);
      if(attribute.equals("id") && searchText == null) {
        listStatement.setNull(1, Types.INTEGER);
      } else if(attribute.equals("id") || attribute.equals("grandtotal")) {
        listStatement.setDouble(1, Double.parseDouble(searchText));
      } else {
        listStatement.setDate(1, Date.valueOf(searchText));
      }
      ResultSet listResultSet = listStatement.executeQuery();
        while (listResultSet.next()) {
          Sales listedSale = new Sales();
          listedSale.setId(listResultSet.getInt(1));
          listedSale.setDate(String.valueOf(listResultSet.getDate(2)));
          listedSale.setGrandTotal(listResultSet.getDouble(3));
          salesList.add(listedSale);
        PreparedStatement listSalesItemStatement = salesConnection.prepareStatement("SELECT P.NAME, S.PRODUCTCODE,S.QUANTITY,S.PRICE FROM SALESITEMS S INNER JOIN PRODUCT P ON P.CODE=S.PRODUCTCODE WHERE S.ID=?");
        List<SalesItem> salesItemList = new ArrayList<>();
        for (Sales sales : salesList) {
          listSalesItemStatement.setInt(1, sales.getId());
          ResultSet listSalesResultSet = listSalesItemStatement.executeQuery();
          while (listSalesResultSet.next()) {
            salesItemList.add(new SalesItem(new Product(listSalesResultSet.getString(2), listSalesResultSet.getString(1)), listSalesResultSet.getFloat(3), listSalesResultSet.getDouble(4)));
          }
          sales.setSalesItemList(salesItemList);
        }
      }
        return salesList;
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  @Override
  public List list(String searchText) throws ApplicationErrorException {
    List<Sales> salesList = new ArrayList<>();
    try {
      Statement listStatement = salesConnection.createStatement();
      String listQuery = "SELECT * FROM SALES WHERE CAST(ID AS TEXT) '" + searchText + "' OR CAST(DATE AS TEXT) ILIKE '" + searchText + "' OR CAST (INVOICE AS TEXT) ILIKE '" + searchText + "'";
      ResultSet listResultSet = listStatement.executeQuery(listQuery);
      while (listResultSet.next()) {
        Sales listedSale = new Sales();
        listedSale.setId(listResultSet.getInt(1));
        listedSale.setDate(String.valueOf(listResultSet.getDate(2)));
        listedSale.setGrandTotal(listResultSet.getDouble(3));
        salesList.add(listedSale);
      }
      PreparedStatement listSalesItemStatement =
          salesConnection.prepareStatement("SELECT P.NAME, S.PRODUCTCODE,S.QUANTITY,S.PRICE FROM SALESITEMS S INNER JOIN PRODUCT P ON P.CODE=S.PRODUCTCODE WHERE S.ID=?");
      List<SalesItem> salesItemList = new ArrayList<>();
      for (Sales sales : salesList) {
        listSalesItemStatement.setInt(1, sales.getId());
        ResultSet listSalesResultSet = listSalesItemStatement.executeQuery();
        while (listSalesResultSet.next()) {
          salesItemList.add(new SalesItem(new Product(listSalesResultSet.getString(2), listSalesResultSet.getString(1)), listSalesResultSet.getFloat(3), listSalesResultSet.getDouble(4)));
        }
        sales.setSalesItemList(salesItemList);
      }
      return salesList;
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  @Override
  public int delete(int id) throws ApplicationErrorException {
    try {
      Statement deleteStatement = salesConnection.createStatement();
      int salesItemUpdatedCount=deleteStatement.executeUpdate("DELETE FROM SALESITEMS WHERE ID='" + id + "'");
      int salesUpdatedCount=deleteStatement.executeUpdate("DELETE FROM SALES WHERE ID='" + id + "'");
      if (salesItemUpdatedCount > 0
          && salesUpdatedCount > 0) {
        return 1;
      } else return -1;

    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }
}
