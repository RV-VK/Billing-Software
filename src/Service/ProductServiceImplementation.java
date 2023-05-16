package Service;

import DAO.*;
import Entity.Product;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProductServiceImplementation implements ProductService {

  private ProductDAO productDAO = new ProductDAOImplementation();
  private final String NAME_REGEX = "^[a-zA-Z\\s]{1,30}$";
  private final String CODE_REGEX = "^[a-zA-Z0-9]{2,6}$";
  private final String NUMBER_REGEX="^[0-9]*$";

  /**
   * This method invokes the ProductDAO object and serves the Product creation.
   *
   * @param product
   * @return Product
   * @throws SQLException
   * @throws ApplicationErrorException
   * @throws UniqueConstraintException
   */
  public Product create(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException {
    if (validate(product)) return productDAO.create(product);
    else return null;
  }

  /**
   * This method invokes the ProductDAO object and serves the Count function.
   * @return count - Integer
   * @throws ApplicationErrorException
   */
  public int count() throws ApplicationErrorException {
    return productDAO.count();
  }

  /**
   * This method invokes the ProductDAO object and serves the List function.
   * @param listattributes
   * @return List - Products
   * @throws ApplicationErrorException
   * @throws PageCountOutOfBoundsException
   */
  public List<Product> list(HashMap<String, String> listattributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException {
    List<Product> productList;
    if (Collections.frequency(listattributes.values(), null) == 0
        || Collections.frequency(listattributes.values(), null) == 1) {
      int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
      int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
      int offset = (pageLength * pageNumber) - pageLength;
      productList =
          productDAO.list(
              listattributes.get("Attribute"),
              listattributes.get("Searchtext"),
              pageLength,
              offset);
      return productList;
    } else if (Collections.frequency(listattributes.values(), null) == listattributes.size() - 1
        && listattributes.get("Searchtext") != null) {
      productList = productDAO.list(listattributes.get("Searchtext"));
      return productList;
    }
    return null;
  }

  /**
   * This method invokes the Product DAO object and serves the edit function.
   * @param product
   * @return resultCode - Integer
   * @throws SQLException
   * @throws ApplicationErrorException
   * @throws UniqueConstraintException
   * @throws UnitCodeViolationException
   */
  public int edit(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException {
    if (!validate(product)) return 0;
    boolean status = productDAO.edit(product);
    if (status) {
      return 1;
    } else {
      return -1;
    }
  }

  /**
   *This method invokes the ProductDAO object and serves the delete function
   * @param parameter
   * @return resultCode - Integer
   * @throws ApplicationErrorException
   */
  public int delete(String parameter) throws ApplicationErrorException {
    return productDAO.delete(parameter);
  }

  public int updateStock(String code,String stock) throws ApplicationErrorException {
    if (stock.matches(NUMBER_REGEX) && code.matches(CODE_REGEX))
      return productDAO.updateStock(code, Float.parseFloat(stock));
    else return -1;
  }

  public int updatePrice(String code,String price) throws ApplicationErrorException{
    if(price.matches(NUMBER_REGEX) && code.matches(CODE_REGEX))
      return productDAO.updatePrice(code,Double.parseDouble(price));
    else
      return -1;
  }
  /**
   * This method validates the Product attributes.
   * @param product
   * @return status - Boolean.
   */
  private boolean validate(Product product) {
    if (!product.getName().matches(NAME_REGEX)
        || !product.getType().matches(NAME_REGEX)
        || !product.getCode().matches(CODE_REGEX)) return false;
    else return true;
  }
}
