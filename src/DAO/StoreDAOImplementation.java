package DAO;

import DBConnection.DBHelper;
import Entity.Store;
import java.sql.*;

public class StoreDAOImplementation implements StoreDAO {
  private Connection storeConnection = DBHelper.getConnection();

  @Override
  public Store create(Store store) throws ApplicationErrorException, SQLException {
    try {
      storeConnection.setAutoCommit(false);
      PreparedStatement unitCreateStatement =
          storeConnection.prepareStatement(
              "INSERT INTO STORE(NAME,PHONENUMBER,ADDRESS,GSTNUMBER) VALUES (?,?,?,?) RETURNING *");
      unitCreateStatement.setString(1, store.getName());
      unitCreateStatement.setLong(2, store.getPhoneNumber());
      unitCreateStatement.setString(3, store.getAddress());
      unitCreateStatement.setString(4, store.getGstCode());
      ResultSet storeCreateResultSet = unitCreateStatement.executeQuery();
      storeCreateResultSet.next();
      Store createdStore =
          new Store(
              storeCreateResultSet.getString(2),
              storeCreateResultSet.getLong(3),
              storeCreateResultSet.getString(4),
              storeCreateResultSet.getString(5));
      storeConnection.commit();
      storeConnection.setAutoCommit(true);
      return createdStore;
    } catch (SQLException e) {
      storeConnection.rollback();
      if(e.getSQLState().equals("23514"))
        return null;
      else
        throw new ApplicationErrorException(
          e.getMessage());
    }
  }

  @Override
  public int edit(Store store) throws SQLException, ApplicationErrorException {
    storeConnection.setAutoCommit(false);
    try {
      String editQuery =
          "UPDATE STORE SET NAME= COALESCE(?,NAME),PHONENUMBER= COALESCE(?,PHONENUMBER),ADDRESS= COALESCE(?,ADDRESS),GSTNUMBER=COALESCE(?,GSTNUMBER)";
      PreparedStatement editStatement = storeConnection.prepareStatement(editQuery);
      editStatement.setString(1, store.getName());
      if (store.getPhoneNumber() == 0) {
        editStatement.setNull(2, Types.BIGINT);
      } else {
        editStatement.setLong(2, store.getPhoneNumber());
      }
      editStatement.setString(3, store.getAddress());
      editStatement.setString(4,store.getGstCode());
      if (editStatement.executeUpdate() > 0) {
        storeConnection.commit();
        storeConnection.setAutoCommit(true);
        return 1;
      } else {
        return -1;
      }
    } catch (Exception e) {
      storeConnection.rollback();
      System.out.println(e.getMessage());
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }

  @Override
  public int delete(String adminPassword) throws ApplicationErrorException {
    try {
      Statement storeExistenceCheckStatement = storeConnection.createStatement();
      ResultSet storeResultSet = storeExistenceCheckStatement.executeQuery("SELECT * FROM STORE");
      if (storeResultSet.next()) {
        Statement passwordCheckStatement = storeConnection.createStatement();
        ResultSet adminPasswordResultSet =
            passwordCheckStatement.executeQuery(
                "SELECT PASSWORD FROM USERS WHERE USERTYPE='Admin'");
        if (adminPasswordResultSet.next()) {
          String originalPassword = adminPasswordResultSet.getString(1);
          if (originalPassword.equals(adminPassword)) {
            Statement deleteStatement = storeConnection.createStatement();
            deleteStatement.execute("DELETE FROM STORE");
            return 1;
          } else {
            return -1;
          }
        }
        else {
          return -1;
        }
      } else {
        return 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }
}
