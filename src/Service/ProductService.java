package Service;

import DAO.*;
import Entity.Product;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface ProductService {
  Product create(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException;

  int count() throws ApplicationErrorException;

  List<Product> list(HashMap<String, String> listattributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException;

  int edit(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException;

  int delete(String parameter) throws ApplicationErrorException;
  int updateStock(String code,String stock) throws ApplicationErrorException;
  int updatePrice(String code,String price) throws ApplicationErrorException;
}
