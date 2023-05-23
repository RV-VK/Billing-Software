package DAO;

import Entity.Store;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.SQLException;

public interface StoreDAO {
  @Select("INSERT INTO STORE VALUES(NAME,PHONENUMBER,ADDRESS,GSTNUMBER) VALUES (#{name},#{phonenumber},#{address},#{gstnumber}) RETURNING *")
  Store create(Store store) throws ApplicationErrorException, SQLException;

  @Update("UPDATE STORE SET NAME=COALESCE(#{name},NAME), PHONENUMBER=COALESCE(NULLIF(#{phonenumber},0),PHONENUMBER), ADDRESS= COALESCE(#{address},ADDRESS), GSTNUMBER=COALESCE(#{gstnumber},GSTNUMBER)")
  int edit(Store store) throws SQLException, ApplicationErrorException;

  @Delete("TRUNCATE STORE ,PRODUCT, USERS, UNIT, PURCHASE, SALES, PURCHASEITEMS, SALESITEMS")
  int delete(String password) throws ApplicationErrorException;
}
