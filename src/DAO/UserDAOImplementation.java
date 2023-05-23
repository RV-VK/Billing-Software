package DAO;

import DBConnection.DBHelper;
import DBConnection.MyBatisSession;
import Entity.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImplementation implements UserDAO {
  private Connection userConnection = DBHelper.getConnection();
  private List<User> userList = new ArrayList<>();

  private SqlSessionFactory sqlSessionFactory;
  private SqlSession sqlSession;
  private UserDAO userMapper;

  /**
   * This method Creates a User Entry in the User table
   * @param user Input Object
   * @return User Object - created
   * @throws SQLException
   * @throws ApplicationErrorException
   * @throws UniqueConstraintException
   */
  @Override
  public User create(User user)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    //    try {
    //      userConnection.setAutoCommit(false);
    //      PreparedStatement userCreateStatement =
    //          userConnection.prepareStatement(
    //              "INSERT INTO USERS(USERNAME,USERTYPE,PASSWORD,FIRSTNAME,LASTNAME,PHONENUMBER)
    // VALUES (?,?,?,?,?,?) RETURNING *");
    //      userCreateStatement.setString(1, user.getUserName());
    //      userCreateStatement.setString(2, user.getUserType());
    //      userCreateStatement.setString(3, user.getPassWord());
    //      userCreateStatement.setString(4, user.getFirstName());
    //      userCreateStatement.setString(5, user.getLastName());
    //      userCreateStatement.setLong(6, user.getPhoneNumber());
    //      ResultSet userCreateResultSet = userCreateStatement.executeQuery();
    //      userCreateResultSet.next();
    //      User createdUser =
    //          new User(
    //              userCreateResultSet.getInt(1),
    //              userCreateResultSet.getString(3),
    //              userCreateResultSet.getString(2),
    //              userCreateResultSet.getString(4),
    //              userCreateResultSet.getString(5),
    //              userCreateResultSet.getString(6),
    //              userCreateResultSet.getLong(7));
    //      userConnection.commit();
    //      userConnection.setAutoCommit(true);
    //      return createdUser;
    //    } catch (SQLException e) {
    //      userConnection.rollback();
    //      if (e.getSQLState().equals("23505")) {
    //        throw new UniqueConstraintException(
    //            ">> UserName must be unique!! The username you have entered already exists!!");
    //      }
    //      throw new ApplicationErrorException(
    //          "Application has went into an Error!!!\n Please Try again");
    //    }
    sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
    sqlSession = sqlSessionFactory.openSession();
    userMapper = sqlSession.getMapper(UserDAO.class);
    User user1 = userMapper.create(user);
    sqlSession.commit();
    sqlSession.close();
    return user1;
  }

  @Override
  public int count() throws ApplicationErrorException {
    //    try {
    //      Statement countStatement = userConnection.createStatement();
    //      ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(ID) FROM USERS");
    //      int count = 0;
    //      while (countResultSet.next()) {
    //        count = countResultSet.getInt(1);
    //      }
    //      return count;
    //    } catch (Exception e) {
    //      throw new ApplicationErrorException(
    //          "Application has went into an Error!!!\n Please Try again");
    //    }
    sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
    sqlSession = sqlSessionFactory.openSession();
    userMapper = sqlSession.getMapper(UserDAO.class);
    return userMapper.count();
  }

  @Override
  public List<User> searchList(String searchText) throws ApplicationErrorException {
    //    try {
    //      Statement listStatement = userConnection.createStatement();
    //      String listQuery =
    //          "SELECT * FROM USERS WHERE USERTYPE ILIKE '"
    //              + searchText
    //              + "' OR USERNAME ILIKE '"
    //              + searchText
    //              + "' OR FIRSTNAME ILIKE '"
    //              + searchText
    //              + "' OR LASTNAME ILIKE '"
    //              + searchText
    //              + "' OR PASSWORD ILIKE '"
    //              + searchText
    //              + "' OR CAST(PHONENUMBER AS TEXT) ILIKE '"
    //              + searchText
    //              + "' OR CAST(ID AS TEXT) ILIKE '"
    //              + searchText
    //              + "'";
    //      ResultSet listresultSet = listStatement.executeQuery(listQuery);
    //      return listHelper(listresultSet);
    //    } catch (SQLException e) {
    //      System.out.println(e.getMessage());
    //      throw new ApplicationErrorException(
    //          "Application has went into an Error!!!\n Please Try again");
    //    }
    sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
    sqlSession = sqlSessionFactory.openSession();
    userMapper = sqlSession.getMapper(UserDAO.class);
    return userMapper.searchList(searchText);
  }

  @Override
  public List list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException {
    //    int count;
    //    try {
    //      String EntryCount =
    //          "SELECT COUNT(*) OVER() FROM USERS WHERE "
    //              + attribute
    //              + "= COALESCE(?,"
    //              + attribute
    //              + ")"
    //              + " ORDER BY ID";
    //      String listQuery =
    //          "SELECT * FROM USERS WHERE "
    //              + attribute
    //              + "= COALESCE(?,"
    //              + attribute
    //              + ")"
    //              + " ORDER BY ID LIMIT "
    //              + pageLength
    //              + "  OFFSET "
    //              + offset;
    //      PreparedStatement countStatement = userConnection.prepareStatement(EntryCount);
    //      PreparedStatement listStatement =
    //          userConnection.prepareStatement(
    //              listQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    //      if (attribute.equals("id") && searchText == null) {
    //        listStatement.setNull(1, Types.INTEGER);
    //        countStatement.setNull(1, Types.INTEGER);
    //      } else if (attribute.equals("id") || attribute.equals("phonenumber")) {
    //        listStatement.setLong(1, Long.parseLong(searchText));
    //        countStatement.setLong(1, Long.parseLong(searchText));
    //      } else {
    //        listStatement.setString(1, searchText);
    //        countStatement.setString(1, searchText);
    //      }
    //      ResultSet countResultSet = countStatement.executeQuery();
    //      countResultSet.next();
    //      count = countResultSet.getInt(1);
    //      if (count <= offset) {
    //        int pageCount;
    //        if (count % pageLength == 0)
    //          pageCount=count/pageLength;
    //        else
    //          pageCount=(count/pageLength)+1;
    //        throw new PageCountOutOfBoundsException(
    //            ">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination "
    //                + pageCount);
    //        }
    //      ResultSet listResultSet = listStatement.executeQuery();
    //      return listHelper(listResultSet);
    //    } catch (Exception e) {
    //      throw new ApplicationErrorException(e.getMessage());
    //    }
    sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
    sqlSession = sqlSessionFactory.openSession();
    userMapper = sqlSession.getMapper(UserDAO.class);
    return userMapper.list(attribute, searchText, pageLength, offset);
  }

  private List<User> listHelper(ResultSet resultSet) throws SQLException {
    while (resultSet.next()) {
      User listedUser =
          new User(
              resultSet.getInt(1),
              resultSet.getString(3),
              resultSet.getString(2),
              resultSet.getString(4),
              resultSet.getString(5),
              resultSet.getString(6),
              resultSet.getLong(7));
      userList.add(listedUser);
    }
    return userList;
  }

  @Override
  public boolean edit(User user)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    //    Connection editConnection = DBHelper.getConnection();
    //    try {
    //      editConnection.setAutoCommit(false);
    //      String editQuery =
    //          "UPDATE USERS SET USERNAME= COALESCE(?,USERNAME),USERTYPE=
    // COALESCE(?,USERTYPE),PASSWORD= COALESCE(?,PASSWORD),FIRSTNAME=
    // COALESCE(?,FIRSTNAME),LASTNAME= COALESCE(?,LASTNAME),PHONENUMBER=COALESCE(?,PHONENUMBER)
    // WHERE ID=?";
    //      PreparedStatement editStatement = editConnection.prepareStatement(editQuery);
    //      editStatement.setString(1, user.getUserName());
    //      editStatement.setString(2, user.getUserType());
    //      editStatement.setString(3, user.getPassWord());
    //      editStatement.setString(4, user.getFirstName());
    //      editStatement.setString(5, user.getLastName());
    //      if (user.getPhoneNumber() == 0) {
    //        editStatement.setNull(6, Types.BIGINT);
    //      } else {
    //        editStatement.setLong(6, user.getPhoneNumber());
    //      }
    //      editStatement.setInt(7, user.getId());
    //      if (editStatement.executeUpdate() > 0) {
    //        editConnection.commit();
    //        editConnection.setAutoCommit(true);
    //        return true;
    //      }
    //      return false;
    //    } catch (SQLException e) {
    //      if (e.getSQLState().equals("23505")) {
    //        throw new UniqueConstraintException(
    //            ">> Username must be unique!!!\n>>The username you have entered already
    // exists!!!");
    //      }
    //      editConnection.rollback();
    //      throw new ApplicationErrorException(
    //          "Application has went into an Error!!!\n Please Try again");
    //    }
    sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
    sqlSession = sqlSessionFactory.openSession();
    userMapper = sqlSession.getMapper(UserDAO.class);
    boolean status = userMapper.edit(user);
    sqlSession.commit();
    sqlSession.close();
    return status;
  }

  @Override
  public int delete(String username) throws ApplicationErrorException {
    //    try {
    //      Statement deleteStatement = userConnection.createStatement();
    //      if (deleteStatement.executeUpdate("DELETE FROM USERS WHERE USERNAME='" + username + "'")
    //          > 0) {
    //        return 1;
    //      } else {
    //        return -1;
    //      }
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //      throw new ApplicationErrorException(
    //          "Application has went into an Error!!!\n Please Try again");
    //    }
    sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
    sqlSession = sqlSessionFactory.openSession();
    userMapper = sqlSession.getMapper(UserDAO.class);
    int rowsAffected = userMapper.delete(username);
    sqlSession.commit();
    sqlSession.close();
    return rowsAffected;
  }

  public int checkIfInitialSetup() throws SQLException {
    //    ResultSet resultSet =
    //        userConnection.createStatement().executeQuery("SELECT COUNT(ID) FROM USERS WHERE
    // USERTYPE='Admin'");
    //    resultSet.next();
    //    return resultSet.getInt(1) == 0;
    sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
    sqlSession = sqlSessionFactory.openSession();
    userMapper = sqlSession.getMapper(UserDAO.class);
    int status = userMapper.checkIfInitialSetup();
    return status;
  }

  @Override
  public User login(String userName, String passWord)
      throws SQLException, ApplicationErrorException {
    //    try{
    //    ResultSet resultSet = userConnection
    //            .createStatement()
    //            .executeQuery("SELECT PASSWORD,USERTYPE FROM USERS WHERE
    // USERNAME='"+userName+"'");
    //    if (resultSet.next()) {
    //      if (resultSet.getString(1).equals(passWord)) return resultSet.getString(2);
    //      else return null;
    //    } else return null;
    //    }
    //    catch(SQLException e)
    //    {
    //      throw new ApplicationErrorException(e.getMessage());
    //    }
    //  }
    sqlSessionFactory = MyBatisSession.getSqlSessionFactory();
    sqlSession = sqlSessionFactory.openSession();
    userMapper = sqlSession.getMapper(UserDAO.class);
    User user=userMapper.login(userName,passWord);
    if(user.getPassWord().equals(passWord))
      return user;
    else
      return new User();
  }
}