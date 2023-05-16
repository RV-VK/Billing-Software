package DAO;

import Entity.Product;
import org.postgresql.plugin.AuthenticationPlugin;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
  Product create(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException;

  int count() throws ApplicationErrorException;

  List list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException, PageCountOutOfBoundsException;

  List list(String searchText) throws ApplicationErrorException;
  boolean edit(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException;

  int delete(String parameter) throws ApplicationErrorException;
  Product findByCode(String code) throws ApplicationErrorException;
  int updateStock(String code,float stock) throws ApplicationErrorException;
  int updatePrice(String code,double price) throws ApplicationErrorException;
}
