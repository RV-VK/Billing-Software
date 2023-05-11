package DAO;

import DBConnection.DBHelper;
import Entity.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImplementation implements UserDAO {
  Connection userConnection = DBHelper.getConnection();
  @Override
  public User create(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException {
    try {
      userConnection.setAutoCommit(false);
      PreparedStatement userCreateStatement = userConnection.prepareStatement("INSERT INTO USERS(USERNAME,USERTYPE,PASSWORD,FIRSTNAME,LASTNAME,PHONENUMBER) VALUES (?,?,?,?,?,?) RETURNING *");
      userCreateStatement.setString(1, user.getUserName());
      userCreateStatement.setString(2, user.getUserType());
      userCreateStatement.setString(3, user.getPassWord());
      userCreateStatement.setString(4, user.getFirstName());
      userCreateStatement.setString(5, user.getLastName());
      userCreateStatement.setLong(6, user.getPhoneNumber());
      ResultSet userCreateResultSet = userCreateStatement.executeQuery();
      userCreateResultSet.next();
      User createdUser = new User(userCreateResultSet.getString(3), userCreateResultSet.getString(2), userCreateResultSet.getString(4), userCreateResultSet.getString(5), userCreateResultSet.getString(6), userCreateResultSet.getLong(7));
      userConnection.commit();
      userConnection.setAutoCommit(true);
      return createdUser;
    } catch (SQLException e) {
      userConnection.rollback();
      if(e.getSQLState().equals("23505"))
      {
        throw new UniqueConstraintException(">> UserName must be unique!! The username you have entered already exists!!");
      }
      throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
    }
  }
  @Override
  public int count() throws ApplicationErrorException {
    try {
      Statement countStatement = userConnection.createStatement();
      ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(ID) FROM USERS");
      int count = 0;
      while (countResultSet.next()) {
        count = countResultSet.getInt(1);
      }
      return count;
    } catch (Exception e) {
      throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
    }
  }
  @Override
  public List<User> list(String searchText) throws ApplicationErrorException {
    List<User> userList = new ArrayList<>();
    try {
        Statement listStatement = userConnection.createStatement();
        String listQuery = "SELECT * FROM USERS WHERE USERTYPE ILIKE '" + searchText + "' OR USERNAME ILIKE '" + searchText + "' OR FIRSTNAME ILIKE '" + searchText + "' OR LASTNAME ILIKE '" + searchText + "' OR PASSWORD ILIKE '"+searchText+"' OR CAST(PHONENUMBER AS TEXT) ILIKE '"+searchText+"' OR ID ILIKE '"+searchText;
        ResultSet listresultSet = listStatement.executeQuery(listQuery);
        while (listresultSet.next()) {
          User listedUser = new User(listresultSet.getInt(1), listresultSet.getString(3), listresultSet.getString(2), listresultSet.getString(4), listresultSet.getString(5), listresultSet.getString(6), listresultSet.getLong(7));
          userList.add(listedUser);
        }
        return userList;
      }
    catch (SQLException e) {
      throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
    }
  }
  @Override
  public List list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException {
    List<User> userList = new ArrayList<>();
    try {
      String listQuery ="SELECT * FROM USERS WHERE " + attribute + "= COALESCE(?," + attribute + ")" + " ORDER BY ID LIMIT " + pageLength + "  OFFSET " + offset;
      PreparedStatement listStatement = userConnection.prepareStatement(listQuery);
      if(attribute.equals("id") && searchText == null) {
        listStatement.setNull(1, Types.INTEGER);
      } else if(attribute.equals("id") || attribute.equals("phonenumber")) {
        listStatement.setLong(1, Long.parseLong(searchText));
      } else {
        listStatement.setString(1, searchText);
      }
      ResultSet listResultSet = listStatement.executeQuery();
      while (listResultSet.next()) {
          User listedUser = new User(listResultSet.getInt(1), listResultSet.getString(3), listResultSet.getString(2), listResultSet.getString(4), listResultSet.getString(5), listResultSet.getString(6), listResultSet.getLong(7));
          userList.add(listedUser);
        }
        return userList;
    } catch (Exception e) {
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }
  @Override
  public boolean edit(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException {
    Connection editConnection = DBHelper.getConnection();
    try {
      editConnection.setAutoCommit(false);
      String editQuery = "UPDATE USERS SET USERNAME= COALESCE(?,USERNAME),USERTYPE= COALESCE(?,USERTYPE),PASSWORD= COALESCE(?,PASSWORD),FIRSTNAME= COALESCE(?,FIRSTNAME),LASTNAME= COALESCE(?,LASTNAME),PHONENUMBER=COALESCE(?,PHONENUMBER) WHERE ID=?";
      PreparedStatement editStatement = editConnection.prepareStatement (editQuery);
      editStatement.setString (1,user.getUserName ());
      editStatement.setString (2,user.getUserType ());
      editStatement.setString (3,user.getPassWord ());
      editStatement.setString (4,user.getFirstName ());
      editStatement.setString (5,user.getLastName ());
      if(user.getPhoneNumber ()==0)
      {
        editStatement.setNull (6,Types.BIGINT);
      }
      else{
          editStatement.setLong (6,user.getPhoneNumber ());
      }
      editStatement.setInt(7,user.getId ());
      editStatement.executeUpdate ();editConnection.commit();
      editConnection.setAutoCommit(true);
      return true;
    } catch (SQLException e) {
      if (e.getSQLState().equals("23505")) {
        throw new UniqueConstraintException(">> Username must be unique!!!\n>>The username you have entered already exists!!!");
      }
      editConnection.rollback();
      throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
    }
  }
  @Override
  public int delete(String username) throws ApplicationErrorException {
    Connection deleteConnection = DBHelper.getConnection();
    try {
      Statement deleteStatement = deleteConnection.createStatement();
      if (deleteStatement.executeUpdate("DELETE FROM USERS WHERE USERNAME='" + username + "'")
          > 0) {
        return 1;
      } else {
        return -1;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }
}
