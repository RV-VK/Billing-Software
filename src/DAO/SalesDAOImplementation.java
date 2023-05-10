package DAO;

import DBConnection.DBHelper;
import Entity.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesDAOImplementation implements SalesDAO {
  @Override
  public Sales create(Sales sales) throws ApplicationErrorException {
    Connection salesCreateConnection = DBHelper.getConnection();
    try {
      salesCreateConnection.setAutoCommit(false);
      String salesEntryQuery = "INSERT INTO SALES(DATE,GRANDTOTAL) VALUES(?,?) RETURNING *";
      String salesItemInsertQuery =
          "INSERT INTO SALESITEM (ID, PRODUCTCODE, QUANTITY, SALESPRICE) VALUES (?,?,?) RETURNING *";
      String salesPriceQuery = "SELECT PRICE,STOCK FROM PRODUCT WHERE CODE=?";
      String stockUpdateQuery = "UPDATE PRODUCT SET STOCK=STOCK-? WHERE CODE=? RETURNING NAME";
      PreparedStatement salesEntryStatement =
          salesCreateConnection.prepareStatement(salesEntryQuery);
      PreparedStatement salesItemInsertStatement =
          salesCreateConnection.prepareStatement(salesItemInsertQuery);
      PreparedStatement salesPriceStatement =
          salesCreateConnection.prepareStatement(salesPriceQuery);
      PreparedStatement stockUpdateStatement =
          salesCreateConnection.prepareStatement(stockUpdateQuery);
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
      for (SalesItem salesItem : sales.getSalesItemList()) {
        salesPriceStatement.setString(1, salesItem.getProduct().getCode());
        ResultSet salesPriceResultSet = salesPriceStatement.executeQuery();
        salesPriceResultSet.next();
        price = salesPriceResultSet.getDouble(1);
        stock = salesPriceResultSet.getFloat(2);
        if (stock < salesItem.getQuantity()) {
          salesCreateConnection.rollback();
          return null;
        }
        salesItemInsertStatement.setInt(1, sales.getId());
        salesItemInsertStatement.setString(2, salesItem.getProduct().getCode());
        salesItemInsertStatement.setFloat(3, salesItem.getQuantity());
        salesItemInsertStatement.setDouble(4, price);
        salesItemInsertResultSet = salesItemInsertStatement.executeQuery();
        stockUpdateStatement.setFloat(1, salesItem.getQuantity());
        stockUpdateStatement.setString(2, salesItem.getProduct().getCode());
        ResultSet stockUpdateResultSet = stockUpdateStatement.executeQuery();
        stockUpdateResultSet.next();
        productName = stockUpdateResultSet.getString(1);
        while (salesItemInsertResultSet.next()) {
          salesItemList.add(
              new SalesItem(
                  new Product(salesItemInsertResultSet.getString(2), productName),
                  salesItemInsertResultSet.getFloat(3),
                  salesItemInsertResultSet.getDouble(4)));
        }
      }
      salesEntry.setSalesItemList(salesItemList);
      salesCreateConnection.commit();
      return salesEntry;
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  @Override
  public int count(String parameter) throws ApplicationErrorException {
    Connection getCountConnection = DBHelper.getConnection();
    try {
      if (parameter == null) {
        Statement countStatement = getCountConnection.createStatement();
        ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(ID) FROM SALES");
        int count;
        countResultSet.next();
        count = countResultSet.getInt(1);
        return count;
      } else {
        Statement countStatement = getCountConnection.createStatement();
        ResultSet countResultSet =
            countStatement.executeQuery(
                "SELECT COUNT(*) FROM PURCHASE WHERE CAST(DATE AS TEXT) ILIKE'" + parameter + "'");
        int count;
        countResultSet.next();
        count = countResultSet.getInt(1);
        return count;
      }
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  @Override
  public List list(int pageLength, int pageNumber)
      throws ApplicationErrorException, PageCountOutOfBoundsException {
    Connection listConnection = DBHelper.getConnection();
    List<Sales> salesList = new ArrayList<>();
    int count = 0;
    try {
      Statement countStatement = listConnection.createStatement();
      ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(ID) FROM PRODUCT");
      countResultSet.next();
      count = countResultSet.getInt(1);
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
    if (count <= ((pageLength * pageNumber) - pageLength)) {
      throw new PageCountOutOfBoundsException(
          ">> Requested page doesnt exist !!!\\n>> Existing page count with given pagination \""
              + ((count / pageLength) + 1));
    } else {
      try {
        int begin = (pageLength * pageNumber) - pageLength;
        Statement listStatement = listConnection.createStatement();
        ResultSet listResultSet =
            listStatement.executeQuery(
                "SELECT * FROM PURCHASE ORDER BY ID LIMIT " + pageLength + " OFFSET " + begin);
        while (listResultSet.next()) {
          Sales listedSale = new Sales();
          listedSale.setId(listResultSet.getInt(1));
          listedSale.setDate(String.valueOf(listResultSet.getDate(2)));
          listedSale.setGrandTotal(listResultSet.getDouble(3));
          salesList.add(listedSale);
        }
        PreparedStatement listSalesItemStatement =
            listConnection.prepareStatement(
                "SELECT P.NAME, S.PRODUCTCODE,S.QUANTITY,S.PRICE FROM SALESITEMS S INNER JOIN PRODUCT P ON P.CODE=S.PRODUCTCODE WHERE S.ID=?");
        List<SalesItem> salesItemList = new ArrayList<>();
        for (Sales sales : salesList) {
          listSalesItemStatement.setInt(1, sales.getId());
          ResultSet listSalesResultSet = listSalesItemStatement.executeQuery();
          while (listSalesResultSet.next()) {
            salesItemList.add(
                new SalesItem(
                    new Product(listSalesResultSet.getString(2), listSalesResultSet.getString(1)),
                    listSalesResultSet.getFloat(3),
                    listSalesResultSet.getDouble(4)));
          }
          sales.setSalesItemList(salesItemList);
        }
        return salesList;
      } catch (Exception e) {
        throw new ApplicationErrorException(e.getMessage());
      }
    }
  }

  @Override
  public List list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException {
    Connection listConnection = DBHelper.getConnection();
    List<Sales> salesList = new ArrayList<>();
    try {
      Statement listStatement =
          listConnection.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
      String listQuery =
          "SELECT * FROM SALES WHERE "
              + attribute
              + " = '"
              + searchText
              + "' ORDER BY ID LIMIT "
              + pageLength
              + " OFFSET "
              + offset;
      ResultSet listResultSet = listStatement.executeQuery(listQuery);
      if (listResultSet.next()) {
        listResultSet.beforeFirst();
        while (listResultSet.next()) {
          Sales listedSale = new Sales();
          listedSale.setId(listResultSet.getInt(1));
          listedSale.setDate(String.valueOf(listResultSet.getDate(2)));
          listedSale.setGrandTotal(listResultSet.getDouble(3));
          salesList.add(listedSale);
        }
        PreparedStatement listSalesItemStatement =
            listConnection.prepareStatement(
                "SELECT P.NAME, S.PRODUCTCODE,S.QUANTITY,S.PRICE FROM SALESITEMS S INNER JOIN PRODUCT P ON P.CODE=S.PRODUCTCODE WHERE S.ID=?");
        List<SalesItem> salesItemList = new ArrayList<>();
        for (Sales sales : salesList) {
          listSalesItemStatement.setInt(1, sales.getId());
          ResultSet listSalesResultSet = listSalesItemStatement.executeQuery();
          while (listSalesResultSet.next()) {
            salesItemList.add(
                new SalesItem(
                    new Product(listSalesResultSet.getString(2), listSalesResultSet.getString(1)),
                    listSalesResultSet.getFloat(3),
                    listSalesResultSet.getDouble(4)));
          }
          sales.setSalesItemList(salesItemList);
        }
        return salesList;
      } else {
        return null;
      }
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  @Override
  public List list(String searchText) throws ApplicationErrorException {
    Connection listConnection = DBHelper.getConnection();
    List<Sales> salesList = new ArrayList<>();
    try {
      Statement listStatement = listConnection.createStatement();
      String listQuery =
          "SELECT * FROM SALES WHERE CAST(ID AS TEXT) '"
              + searchText
              + "' OR CAST(DATE AS TEXT) ILIKE '"
              + searchText
              + "' OR CAST (INVOICE AS TEXT) ILIKE '"
              + searchText
              + "'";
      ResultSet listResultSet = listStatement.executeQuery(listQuery);
      while (listResultSet.next()) {
        Sales listedSale = new Sales();
        listedSale.setId(listResultSet.getInt(1));
        listedSale.setDate(String.valueOf(listResultSet.getDate(2)));
        listedSale.setGrandTotal(listResultSet.getDouble(3));
        salesList.add(listedSale);
      }
      PreparedStatement listSalesItemStatement =
          listConnection.prepareStatement(
              "SELECT P.NAME, S.PRODUCTCODE,S.QUANTITY,S.PRICE FROM SALESITEMS S INNER JOIN PRODUCT P ON P.CODE=S.PRODUCTCODE WHERE S.ID=?");
      List<SalesItem> salesItemList = new ArrayList<>();
      for (Sales sales : salesList) {
        listSalesItemStatement.setInt(1, sales.getId());
        ResultSet listSalesResultSet = listSalesItemStatement.executeQuery();
        while (listSalesResultSet.next()) {
          salesItemList.add(
              new SalesItem(
                  new Product(listSalesResultSet.getString(2), listSalesResultSet.getString(1)),
                  listSalesResultSet.getFloat(3),
                  listSalesResultSet.getDouble(4)));
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
    Connection deleteConnection = DBHelper.getConnection();
    try {
      Statement deleteStatement = deleteConnection.createStatement();
      if (deleteStatement.executeUpdate("DELETE FROM SALESITEMS WHERE ID='" + id + "'") > 0
          && deleteStatement.executeUpdate("DELETE FROM SALES WHERE ID='" + id + "'") > 0) {
        return 1;
      } else return -1;

    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }
}
