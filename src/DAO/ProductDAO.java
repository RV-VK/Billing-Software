package DAO;

import Entity.Product;
import org.apache.ibatis.annotations.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
  @Select("INSERT into product(CODE,NAME,UNITCODE,TYPE,PRICE,STOCK) VALUES (#{code},#{name},#{unitcode},#{type},#{price},#{stock}) RETURNING *")
  Product create(Product product)
          throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException, IOException;

  @Select("select count(id) from product")
  int count();

  @Select("Select * from product where ${attribute} = coalesce(#{searchText},${attribute}) AND isdeleted=false order by id limit #{pageLength} offset #{offset}")
  List<Product> list(@Param("attribute") String attribute, @Param("searchText") String searchText, @Param("pageLength") int pageLength, @Param("offset") int offset)
      throws ApplicationErrorException, PageCountOutOfBoundsException;

  @Select("SELECT * FROM PRODUCT WHERE ( NAME ILIKE '"
          + "${searchText}"
          + "' OR CODE ILIKE '"
          + "${searchText}"
          + "' OR UNITCODE ILIKE '"
          + "${searchText}"
          + "' OR TYPE ILIKE '"
          + "${searchText}"
          + "' OR CAST(ID AS TEXT) ILIKE '"
          + "${searchText}"
          + "' OR CAST(STOCK AS TEXT) ILIKE '"
          + "${searchText}"
          + "' OR CAST(PRICE AS TEXT) ILIKE '"
          + "${searchText}"
          + "' )"+" AND ISDELETED=FALSE")
  List<Product> searchList(@Param("searchText") String searchText) throws ApplicationErrorException;


  @Update("UPDATE PRODUCT SET CODE= COALESCE(#{code},CODE),NAME= COALESCE(#{name},NAME),UNITCODE= COALESCE(#{unitcode},UNITCODE),TYPE= COALESCE(#{type},TYPE),PRICE= COALESCE(#{price},PRICE) WHERE ID=#{id} ")
  boolean edit(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException;

  @Update("UPDATE PRODUCT SET ISDELETED='TRUE' WHERE (CAST(ID AS TEXT) ILIKE '%${parameter}%' OR CODE='${parameter}') AND STOCK=0")
  int delete(@Param("parameter") String parameter) throws ApplicationErrorException;

  @Select("SELECT * FROM PRODUCT WHERE CODE=#{code}")
  Product findByCode(String code) throws ApplicationErrorException;

  @Update("UPDATE PRODUCT SET STOCK=STOCK+#{stock} WHERE CODE=#{code}")
  int updateStock(@Param("code") String code,@Param("stock") float stock) throws ApplicationErrorException;

  @Update("UPDATE PRODUCT SET PRICE=#{price} WHERE CODE=#{code}")
  int updatePrice(@Param("code") String code,@Param("price") double price) throws ApplicationErrorException;
}
