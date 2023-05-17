package DAO;

import DBConnection.DBHelper;
import Entity.Product;
import Entity.Purchase;
import Entity.PurchaseItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAOImplementation implements PurchaseDAO {
  private final Connection purchaseConnection = DBHelper.getConnection();
  private List<Purchase> purchaseList = new ArrayList<>();


  @Override
  public Purchase create(Purchase purchase) throws ApplicationErrorException, SQLException {
    try {
      purchaseConnection.setAutoCommit(false);
      PreparedStatement stockUpdateStatement = purchaseConnection.prepareStatement("UPDATE PRODUCT SET STOCK=STOCK+? WHERE CODE=?");
      PreparedStatement purchaseEntryStatement = purchaseConnection.prepareStatement("INSERT INTO PURCHASE(DATE,INVOICE,GRANDTOTAL) VALUES(?,?,?) RETURNING *");
      PreparedStatement productNameStatement = purchaseConnection.prepareStatement("SELECT NAME FROM PRODUCT WHERE CODE=?");
      purchaseEntryStatement.setDate(1, Date.valueOf(purchase.getDate()));
      purchaseEntryStatement.setInt(2, purchase.getInvoice());
      purchaseEntryStatement.setDouble(3, purchase.getGrandTotal());
      ResultSet purchaseEntryResultSet = purchaseEntryStatement.executeQuery();
      Purchase purchaseEntry = new Purchase();
      while (purchaseEntryResultSet.next()) {
        purchaseEntry.setId(purchaseEntryResultSet.getInt(1));
        purchaseEntry.setDate(String.valueOf(purchaseEntryResultSet.getDate(2)));
        purchaseEntry.setInvoice(purchaseEntryResultSet.getInt(3));
        purchaseEntry.setGrandTotal(purchaseEntryResultSet.getDouble(4));
      }
      List<PurchaseItem> purchaseItemList = new ArrayList<>();
      PreparedStatement purchaseItemInsertStatement =
          purchaseConnection.prepareStatement("INSERT INTO PURCHASEITEMS(INVOICE,PRODUCTCODE,QUANTITY,COSTPRICE) VALUES(?,?,?,?) RETURNING *");
      ResultSet purchaseItemInsertResultSet;
      for (PurchaseItem purchaseItem : purchase.getPurchaseItemList()) {
        purchaseItemInsertStatement.setInt(1, purchase.getInvoice());
        purchaseItemInsertStatement.setString(2, purchaseItem.getProduct().getCode());
        purchaseItemInsertStatement.setFloat(3, purchaseItem.getQuantity());
        purchaseItemInsertStatement.setDouble(4, purchaseItem.getUnitPurchasePrice());
        purchaseItemInsertResultSet = purchaseItemInsertStatement.executeQuery();
        stockUpdateStatement.setFloat(1, purchaseItem.getQuantity());
        stockUpdateStatement.setString(2, purchaseItem.getProduct().getCode());
        if (!(stockUpdateStatement.executeUpdate() > 0)) {
          return null;
        }
        while (purchaseItemInsertResultSet.next()) {
          productNameStatement.setString(1, purchaseItemInsertResultSet.getString(2));
          ResultSet productNameResultSet = productNameStatement.executeQuery();
          productNameResultSet.next();
          purchaseItemList.add(
              new PurchaseItem(
                  new Product(
                      purchaseItemInsertResultSet.getString(2), productNameResultSet.getString(1)),
                  purchaseItemInsertResultSet.getFloat(3),
                  purchaseItemInsertResultSet.getDouble(4)));
        }
      }
      purchaseEntry.setPurchaseItemList(purchaseItemList);
      purchaseConnection.commit();
      purchaseConnection.setAutoCommit(true);
      return purchaseEntry;
    } catch (SQLException e) {
      purchaseConnection.rollback();
      if(e.getSQLState().equals("23503"))
        throw new ApplicationErrorException(">> The Product code you have entered doesnt exists!");
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  @Override
  public int count(String parameter) throws ApplicationErrorException {
    int count;
    try {
      if (parameter == null) {
        ResultSet countResultSet =purchaseConnection.createStatement().executeQuery("SELECT COUNT(ID) FROM PURCHASE");
        countResultSet.next();
        count = countResultSet.getInt(1);
        return count;
      } else {
        ResultSet countResultSet =
                purchaseConnection.createStatement().executeQuery(
                "SELECT COUNT(*) FROM PURCHASE WHERE CAST(DATE AS TEXT) ILIKE'" + parameter + "'");
        countResultSet.next();
        count = countResultSet.getInt(1);
        return count;
      }
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  @Override
  public List list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException {
    int count;
    try {
      String EntryCount="SELECT COUNT(*) OVER() FROM PURCHASE WHERE "
              + attribute
              + "= COALESCE(?,"
              + attribute
              + ")"
              + " ORDER BY ID";
      String listQuery =
          "SELECT * FROM PURCHASE WHERE "
              + attribute
              + "= COALESCE(?,"
              + attribute
              + ")"
              + " ORDER BY ID LIMIT "
              + pageLength
              + "  OFFSET "
              + offset;
      PreparedStatement countStatement=purchaseConnection.prepareStatement(EntryCount);
      PreparedStatement listStatement = purchaseConnection.prepareStatement(listQuery);
      if (attribute.equals("id") && searchText == null) {
        listStatement.setNull(1, Types.INTEGER);
        countStatement.setNull(1,Types.INTEGER);
      } else if (attribute.equals("id")
          || attribute.equals("grandtotal")
          || attribute.equals("invoice")) {
        listStatement.setDouble(1, Double.parseDouble(searchText));
        countStatement.setDouble(1,Double.parseDouble(searchText));
      } else {
        listStatement.setDate(1, Date.valueOf(searchText));
        countStatement.setDate(1,Date.valueOf(searchText));
      }
      ResultSet countResultSet=countStatement.executeQuery();
      if (countResultSet.next()) {
        count = countResultSet.getInt(1);
      }
      else return null;
      if (count <= offset) {
        int pageCount;
        if (count % pageLength == 0)
          pageCount=count/pageLength;
        else
          pageCount=(count/pageLength)+1;
        throw new PageCountOutOfBoundsException(
            ">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination "
                + pageCount);
        }
      ResultSet listResultSet = listStatement.executeQuery();
      return listHelper(listResultSet);
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }


  @Override
  public List list(String searchText) throws ApplicationErrorException {
    try {
      String listQuery =
          "SELECT * FROM PURCHASE WHERE CAST(ID AS TEXT) ILIKE '"
              + searchText
              + "' OR CAST(DATE AS TEXT) ILIKE '"
              + searchText
              + "' OR CAST(INVOICE AS TEXT) ILIKE '"
              + searchText
              + "'";
      ResultSet listResultSet = purchaseConnection.createStatement().executeQuery(listQuery);
      return listHelper(listResultSet);
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  private List<Purchase> listHelper(ResultSet resultSet) throws SQLException {
    while (resultSet.next()) {
      Purchase listedPurchase = new Purchase();
      listedPurchase.setId(resultSet.getInt(1));
      listedPurchase.setDate(String.valueOf(resultSet.getDate(2)));
      listedPurchase.setInvoice(resultSet.getInt(3));
      listedPurchase.setGrandTotal(resultSet.getInt(4));
      purchaseList.add(listedPurchase);
    }
    PreparedStatement listPurchaseItemsStatement =
            purchaseConnection.prepareStatement(
                    "SELECT P.NAME,PU.PRODUCTCODE,PU.QUANTITY,PU.COSTPRICE FROM PURCHASEITEMS PU INNER JOIN PRODUCT P ON P.CODE=PU.PRODUCTCODE WHERE PU.INVOICE=?");
    for (Purchase purchase : purchaseList) {
      List<PurchaseItem> purchaseItemList = new ArrayList<>();
      listPurchaseItemsStatement.setInt(1, purchase.getInvoice());
      ResultSet listPurchaseResultSet = listPurchaseItemsStatement.executeQuery();
      while (listPurchaseResultSet.next()) {
        purchaseItemList.add(
                new PurchaseItem(
                        new Product(
                                listPurchaseResultSet.getString(2), listPurchaseResultSet.getString(1)),
                        listPurchaseResultSet.getFloat(3),
                        listPurchaseResultSet.getDouble(4)));
      }
      purchase.setPurchaseItemList(purchaseItemList);
    }
    return purchaseList;
  }
  @Override
  public int delete(int invoice) throws ApplicationErrorException {
    try {
      if ( purchaseConnection.createStatement().executeUpdate("DELETE FROM PURCHASEITEMS WHERE INVOICE='" + invoice + "'")
              > 0
          &&  purchaseConnection.createStatement().executeUpdate("DELETE FROM PURCHASE WHERE INVOICE='" + invoice + "'")
              > 0) {
        return 1;
      } else return -1;
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }
}
