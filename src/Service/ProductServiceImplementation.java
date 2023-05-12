package Service;
import DAO.*;
import Entity.Product;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProductServiceImplementation implements ProductService {

  private ProductDAO productDAO = new ProductDAOImplementation();
  private final String NAME_REGEX="^[a-zA-Z\\s]{1,30}$";
  private final String CODE_REGEX="^[a-zA-Z0-9]{2,6}$";
  public Product createProductService(Product product) throws SQLException, ApplicationErrorException, UniqueConstraintException {
    if(validate(product))
      return productDAO.create(product);
    else
      return new Product();
    }
  public int countProductService() throws ApplicationErrorException {
    return productDAO.count();
  }
  public List<Product> listProductService(HashMap<String, String> listattributes) throws ApplicationErrorException, PageCountOutOfBoundsException {
    List<Product> productList;
    if (Collections.frequency(listattributes.values(), null) == 0||Collections.frequency (listattributes.values (),null)==1) {
      int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
      int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
      int offset = (pageLength * pageNumber) - pageLength;
      productList = productDAO.list(listattributes.get("Attribute"), listattributes.get("Searchtext"), pageLength, offset);
      return productList;
    } else if (Collections.frequency(listattributes.values(), null) == listattributes.size() - 1 && listattributes.get("Searchtext") != null) {
      productList = productDAO.list(listattributes.get("Searchtext"));
      return productList;
    }
    return null;
  }
  public int editProductService(Product product) throws SQLException, ApplicationErrorException, UniqueConstraintException, UnitCodeViolationException {
    if (!validate(product))
      return 0;
    boolean status=productDAO.edit(product);
    if(status)
    {
      return 1;
    }
    else{
      return -1;
    }
  }
  public int deleteProductService(String parameter) throws ApplicationErrorException {
    return productDAO.delete(parameter);
  }

  private boolean validate(Product product)
  {
    if(!product.getName().matches(NAME_REGEX)||!product.getType().matches(NAME_REGEX)||!product.getCode().matches(CODE_REGEX))
      return false;
    else return true;
  }
}
