package Service;
import DAO.*;
import Entity.Product;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProductServiceImplementation implements ProductService {

  private ProductDAO productDAO = new ProductDAOImplementation();
  public Product createProductService(Product product)
          throws SQLException, ApplicationErrorException, UniqueNameException, UniqueConstraintException {

    return productDAO.create(product);
    }

  public int countProductService() throws ApplicationErrorException {
    return productDAO.count();
  }
  public List<Product> listProductService(HashMap<String, String> listattributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException {
    List<Product> productList;
    ProductDAO listProductDAO = new ProductDAOImplementation();
    if (Collections.frequency(listattributes.values(), null) == 0||Collections.frequency (listattributes.values (),null)==1) {
      int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
      int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
      int offset = (pageLength * pageNumber) - pageLength;
      productList =
          listProductDAO.list(
              listattributes.get("Attribute"),
              listattributes.get("Searchtext"),
              pageLength,
              offset);
      return productList;
    } else if (Collections.frequency(listattributes.values(), null) == listattributes.size() - 1
        && listattributes.get("Searchtext") != null) {
      productList = listProductDAO.list(listattributes.get("Searchtext"));
      return productList;
    }
    return null;
  }
  public int editProductService(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueNameException,
          UniqueConstraintException,
          UnitCodeViolationException {
    ProductDAO productEditDAO = new ProductDAOImplementation();
    String procodeRegex = "^[a-zA-Z0-9]{2,6}$";
    String nameRegex = "^[a-zA-Z\\s]{3,30}$";

    if (product.getName() != null && !product.getName ().matches(nameRegex)
        || product.getCode() != null && !product.getCode ().matches(procodeRegex)
        || product.getType () != null && !product.getType ().matches(nameRegex)
        ) {
      return 0;
    }
    boolean status=productEditDAO.edit (product);
    if(status)
    {
      return 1;
    }
    else{
      return -1;
    }
  }
  public int deleteProductService(String parameter) throws ApplicationErrorException {
    ProductDAO deleteProductDAO = new ProductDAOImplementation();
    return deleteProductDAO.delete(parameter);
  }
}
