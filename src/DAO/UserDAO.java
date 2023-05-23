package DAO;

import Entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
  @Select("INSERT INTO USERS(USERNAME,USERTYPE,PASSWORD,FIRSTNAME,LASTNAME,PHONENUMBER) VALUES (#{userName},#{userType},#{passWord},#{firstName},#{lastName},#{phoneNumber}) RETURNING *")
  User create(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException;

  @Select("SELECT COUNT(ID) FROM USERS")
  int count() throws ApplicationErrorException;

  @Select("SELECT * FROM USERS WHERE ( USERNAME ILIKE '"
          + "${searchText}"
          + "' OR USERTYPE ILIKE '"
          + "${searchText}"
          + "' OR PASSWORD ILIKE '"
          + "${searchText}"
          + "' OR FIRSTNAME ILIKE '"
          + "${searchText}"
          + "' OR LASTNAME ILIKE '"
          + "${searchText}"
          + "' OR CAST(ID AS TEXT) ILIKE '"
          + "${searchText}"
          + "' OR CAST(PHONENUMBER AS TEXT) ILIKE '"
          + "${searchText}"
          + "')")
  List<User> searchList(String searchText) throws ApplicationErrorException;

  @Select("SELECT *  FROM USERS WHERE ${attribute} = COALESCE(#{searchText},${attribute}) ORDER BY ID LIMIT #{pageLength} OFFSET #{offset}")
  List<User> list(@Param("attribute") String attribute,@Param("searchText") String searchText,@Param("pageLength") int pageLength,@Param("offset") int offset)
      throws ApplicationErrorException;

  @Update("UPDATE USERS SET USERNAME= COALESCE(#{userName},USERNAME),USERTYPE= COALESCE(#{userType},USERTYPE),PASSWORD= COALESCE(#{passWord},PASSWORD),FIRSTNAME= COALESCE(#{firstName},FIRSTNAME),LASTNAME= COALESCE(#{lastName},LASTNAME),PHONENUMBER=COALESCE(NULLIF(#{phoneNumber},0),PHONENUMBER) WHERE ID=#{id}")
  boolean edit(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException;

  @Delete("DELETE FROM USERS WHERE USERNAME=#{parameter}")
  int delete(@Param("parameter") String parameter) throws ApplicationErrorException;

  @Select("SELECT COUNT(ID) FROM USERS WHERE USERTYPE='Admin'")
  int checkIfInitialSetup() throws SQLException;

  @Select("SELECT PASSWORD,USERTYPE FROM USERS WHERE USERNAME=#{username}")
  User login(@Param("username") String username, String passWord) throws SQLException, ApplicationErrorException;
}
