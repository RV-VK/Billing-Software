package Service;

import DAO.*;
import Entity.Product;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface ProductService {
  Product createProductService(Product product)
          throws SQLException, ApplicationErrorException, UniqueConstraintException;

  int countProductService() throws ApplicationErrorException;

  List<Product> listProductService(HashMap<String, String> listattributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException;

  int editProductService(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException;

  int deleteProductService(String parameter) throws ApplicationErrorException;
}
