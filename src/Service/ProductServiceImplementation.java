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
   * @param product Input product
   * @return Product
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
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
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  public int count() throws ApplicationErrorException {
    return productDAO.count();
  }

  /**
   * This method invokes the ProductDAO object and serves the List function.
   * @param listattributes Key Value Pairs(Map) of List function attributes.
   * @return List - Products
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws PageCountOutOfBoundsException Custom Exception thrown when a non-existing page is given as input in Pageable List.
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
   * @param product Edited Product
   * @return resultCode - Integer
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table.
   * @throws UnitCodeViolationException Custom Exception to convey Foreign Key Violation in Product table.
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
   * @param parameter Input parameter for delete function.(Code/Id)
   * @return resultCode - Integer
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  public int delete(String parameter) throws ApplicationErrorException {
    return productDAO.delete(parameter);
  }

  /**This method invokes the ProductDAO and serves the stock Update function.
   *
   * @param code Product Code
   * @param stock updated Stock
   * @return statusCode - Integer
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  public int updateStock(String code,String stock) throws ApplicationErrorException {
    if (stock.matches(NUMBER_REGEX) && code.matches(CODE_REGEX))
      return productDAO.updateStock(code, Float.parseFloat(stock));
    else return -1;
  }

  /**This method invokes the ProductDAO and serves the Price Update function.
   *
   * @param code Product code
   * @param price updated Price
   * @return statusCode - Integer
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  public int updatePrice(String code,String price) throws ApplicationErrorException{
    if(price.matches(NUMBER_REGEX) && code.matches(CODE_REGEX))
      return productDAO.updatePrice(code,Double.parseDouble(price));
    else
      return -1;
  }
  /**
   * This method validates the Product attributes.
   * @param product Product to be Validated
   * @return status - Boolean.
   */
  private boolean validate(Product product) {
    if ((product.getName()!=null&&!product.getName().matches(NAME_REGEX))
        || (product.getType()!=null && !product.getType().matches(NAME_REGEX))
        || (product.getCode()!=null&&!product.getCode().matches(CODE_REGEX))) return false;
    else return true;
  }
}
