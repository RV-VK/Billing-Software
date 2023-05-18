package DAO;

import DBConnection.DBHelper;
import Entity.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImplementation implements ProductDAO {
  private final Connection productConnection = DBHelper.getConnection();
  private List<Product> productList = new ArrayList<>();

  /**
   * This Method Creates an Entry in the Product Table
   *
   * @param product - Input product
   * @return product - Entered product
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   * @throws SQLException Exception thrown based on SQL syntax.
   * @throws UniqueConstraintException Custom Exception to convey Unique constraint Violation in SQL table
   */
  @Override
  public Product create(Product product)
      throws ApplicationErrorException,
          SQLException,
          UniqueConstraintException,
          UnitCodeViolationException {
    try {
      productConnection.setAutoCommit(false);
      PreparedStatement productCreateStatement =
          productConnection.prepareStatement(
              "INSERT INTO PRODUCT(CODE,NAME,unitcode,TYPE,PRICE,STOCK) VALUES (?,?,?,?,?,?) RETURNING *");
      productCreateStatement.setString(1, product.getCode());
      productCreateStatement.setString(2, product.getName());
      productCreateStatement.setString(3, product.getunitcode());
      productCreateStatement.setString(4, product.getType());
      productCreateStatement.setDouble(5, product.getPrice());
      productCreateStatement.setFloat(6, product.getAvailableQuantity());
      ResultSet productCreateResultSet = productCreateStatement.executeQuery();
      productCreateResultSet.next();
      productConnection.commit();
      productConnection.setAutoCommit(true);
      return getProductFromResultSet(productCreateResultSet);
    } catch (SQLException e) {
      productConnection.rollback();
      if (e.getSQLState().equals("23503")) {
        throw new UnitCodeViolationException(">> The unit Code you have entered  does not Exists!!");
      } else if (e.getSQLState().equals("23505")) {
        if (e.getMessage().contains("product_name"))
          throw new UniqueConstraintException(
              ">> Name must be unique!!!\n>> The Name you have entered already exists!!!");
        else
          throw new UniqueConstraintException(
              ">> Code must be unique!!!\n>> The code you have entered already exists!!!");
      } else {
        throw new ApplicationErrorException(
            ">> Application has went into an Error!!!\n>>Please Try again");
      }
    }

  }

  /**
   * Private Method to assist Product Construction from ResultSet.
   * @param resultSet - Product ResultSet.
   * @return Product
   * @throws SQLException Exception thrown based on SQL syntax.
   */
  private Product getProductFromResultSet(ResultSet resultSet) throws SQLException {
    return new Product(resultSet.getInt(1),
            resultSet.getString(2),
            resultSet.getString(3),
            resultSet.getString(4),
            resultSet.getString(5),
            resultSet.getFloat(6),
            resultSet.getDouble(7));
  }


  /**
   * This Method returns the number of entries in the Product table.
   *
   * @return count
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public int count() throws ApplicationErrorException {
    try {
      ResultSet countResultSet = productConnection.createStatement().executeQuery("SELECT COUNT(ID) FROM PRODUCT");
      int count = 0;
      while (countResultSet.next()) {
        count = countResultSet.getInt(1);
      }
      return count;
    } catch (Exception e) {
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }

  /**
   * This method Lists the products in the product table based on the given search-text.
   *
   * @param searchText The search-text that must be found.
   * @return List - Products
   * @throws ApplicationErrorException - Exception thrown due to Persistence problems.
   */
  public List<Product> list(String searchText) throws ApplicationErrorException {
    try {
      String listQuery =
          "SELECT * FROM PRODUCT WHERE ( NAME ILIKE '"
              + searchText
              + "' OR CODE ILIKE '"
              + searchText
              + "' OR UNITCODE ILIKE '"
              + searchText
              + "' OR TYPE ILIKE '"
              + searchText
              + "' OR CAST(ID AS TEXT) ILIKE '"
              + searchText
              + "' OR CAST(STOCK AS TEXT) ILIKE '"
              + searchText
              + "' OR CAST(PRICE AS TEXT) ILIKE '"
              + searchText
              + "' )"+" AND ISDELETED=FALSE";
      ResultSet listresultSet =productConnection.createStatement().executeQuery(listQuery);
      return listHelper(listresultSet);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }

  /**
   * This method lists the products in the products table based on the given searchable attribute
   * and its corresponding search-text formatted in a pageable manner.
   *
   * @param attribute The attribute to be looked upon.
   * @param searchText The searchtext to be found.
   * @param pageLength The number of entries that must be listed.
   * @param offset The Page number that has to be listed
   * @return List - Products
   * @throws ApplicationErrorException  Exception thrown due to Persistence problems.
   */
  @Override
  public List<Product> list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException, PageCountOutOfBoundsException {
    int count = Integer.MAX_VALUE;
    try {
      String EntryCount="SELECT COUNT(*) OVER() FROM PRODUCT WHERE "
              + attribute
              + "= COALESCE(?,"
              + attribute
              + ")"
              + "AND ISDELETED=FALSE ORDER BY ID";
      String listQuery =
          "SELECT * FROM PRODUCT WHERE "
              + attribute
              + "= COALESCE(?,"
              + attribute
              + ")"
              + "AND ISDELETED=FALSE ORDER BY ID LIMIT "
              + pageLength
              + "  OFFSET "
              + offset;
      PreparedStatement countStatement=productConnection.prepareStatement(EntryCount);
      PreparedStatement listStatement =
          productConnection.prepareStatement(
              listQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
      if (attribute.equals("id") && searchText == null) {
        listStatement.setNull(1, Types.INTEGER);
        countStatement.setNull(1,Types.INTEGER);
      } else if (attribute.equals("id") || attribute.equals("stock") || attribute.equals("price")) {
        listStatement.setDouble(1, Double.parseDouble(searchText));
        countStatement.setDouble(1,Double.parseDouble(searchText));
      } else {
        listStatement.setString(1, searchText);
        countStatement.setString(1,searchText);
      }
      ResultSet countResultSet=countStatement.executeQuery();
      if(countResultSet.next())
        count=countResultSet.getInt(1);
      if (count <= offset) {
        int pageCount;
        if (count % pageLength == 0)
          pageCount=count/pageLength;
        else
          pageCount=(count/pageLength)+1;
        throw new PageCountOutOfBoundsException(
              ">> Requested Page doesnt Exist!!\n>> Existing Pagecount with given pagination "
                  + pageCount);
      }
      ResultSet resultSet = listStatement.executeQuery();
      return listHelper(resultSet);
    } catch (SQLException e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  /**
   * This method serves the ListDAO function.
   * @param resultSet ListQuery results.
   * @return List - Product
   * @throws SQLException Exception thrown based on SQL syntax.
   */
  private List<Product> listHelper(ResultSet resultSet) throws SQLException {
    while (resultSet.next()) {
      productList.add(getProductFromResultSet(resultSet));
    }
    return productList;
  }

  /**
   * This method updates the attributes of the product entry in the Product table
   *
   * @param product The Updated Product entry
   * @return status - Boolean
   * @throws SQLException  Exception thrown based on SQL syntax.
   * @throws ApplicationErrorException  Exception thrown due to Persistence problems.
   * @throws UniqueConstraintException  Custom Exception to convey Unique constraint Violation in SQL table
   * @throws UnitCodeViolationException  Custom Exception to convey Foreign Key Violation in Product table.
   */
  @Override
  public boolean edit(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueConstraintException,
          UnitCodeViolationException {
    try {
      productConnection.setAutoCommit(false);
      String editQuery =
          "UPDATE PRODUCT SET CODE= COALESCE(?,CODE),NAME= COALESCE(?,NAME),UNITCODE= COALESCE(?,UNITCODE),TYPE= COALESCE(?,TYPE),PRICE= COALESCE(?,PRICE) WHERE ID=? ";
      PreparedStatement editStatement = productConnection.prepareStatement(editQuery);
      editStatement.setString(1, product.getCode());
      editStatement.setString(2, product.getName());
      editStatement.setString(3, product.getunitcode());
      editStatement.setString(4, product.getType());
      if (product.getPrice() == 0) {
        editStatement.setNull(5, Types.NUMERIC);
      } else {
        editStatement.setDouble(5, product.getPrice());
      }
      editStatement.setInt(6, product.getId());
      if (editStatement.executeUpdate() > 0) {
        productConnection.commit();
        productConnection.setAutoCommit(true);
        return true;
      }
    } catch (SQLException e) {
      productConnection.rollback();
      if (e.getSQLState().equals("23505")) {
        if (e.getMessage().contains("product_code")) {
          throw new UniqueConstraintException(
              ">>Code must be unique!!!\n>>The code you have entered already exists!!!");
        } else if (e.getMessage().contains("product_name")) {
          throw new UniqueConstraintException(
              "Name must be unique!!!\n>>The Name you have entered already exists!!!");
        }
      } else if (e.getSQLState().equals("23503")) {
        throw new UnitCodeViolationException(">>The unitcode you have entered doesn't exist!!!");
      } else {
        e.printStackTrace();
        throw new ApplicationErrorException(
            "Application has went into an Error!!!\n Please Try again");
      }
    }
    return false;
  }

  /**
   * This method deletes an entry in the Product table based on the given parameter.
   *
   * @param parameter - Input Parameter to delete.
   * @return resultCode - Integer
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public int delete(String parameter) throws ApplicationErrorException {
    try {
      Statement deleteStatement = productConnection.createStatement();
      ResultSet stockResultSet;
      if (Character.isAlphabetic(parameter.charAt(0)))
        stockResultSet =
            deleteStatement.executeQuery(
                "SELECT STOCK FROM PRODUCT WHERE CODE='" + parameter + "'");
      else
        stockResultSet =
            deleteStatement.executeQuery("SELECT STOCK FROM PRODUCT WHERE ID='" + parameter + "'");
      if (!stockResultSet.next()) return -1;
      float stock = stockResultSet.getFloat(1);
      if (stock > 0) return 0;
      else {
        if (Character.isAlphabetic(parameter.charAt(0)))
          if (deleteStatement.executeUpdate(
              "UPDATE PRODUCT SET ISDELETED='TRUE' WHERE CODE ='" + parameter + "'")>0)
            return 1;
          else
            return -1;
        else
          if (deleteStatement.executeUpdate(
              "UPDATE PRODUCT SET ISDELETED='TRUE' WHERE ID ='" + parameter + "'")>0)
            return 1;
          else
            return -1;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new ApplicationErrorException(
          "Application has went into an Error!!!\n Please Try again");
    }
  }

  /**
   * This method finds the Product by its product code attribute.
   *
   * @param code Input product code.
   * @return Product
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  @Override
  public Product findByCode(String code) throws ApplicationErrorException {
    try {
      ResultSet getProductResultSet =
              productConnection.createStatement()
                      .executeQuery("SELECT * FROM PRODUCT  WHERE CODE='" + code + "'");
      getProductResultSet.next();
      return getProductFromResultSet(getProductResultSet);
    } catch (Exception e) {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  /**This method updates the Stock column of Product based on given product code.
   *
   * @param code Product code
   * @param stock Stock to be updated
   * @return updated Stock - Float
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  public int updateStock(String code,float stock) throws ApplicationErrorException {
    try{
    return productConnection.createStatement().executeUpdate("UPDATE PRODUCT SET STOCK="+stock+" WHERE CODE='"+code+"'");
    }catch(Exception e)
    {
      throw new ApplicationErrorException(e.getMessage());
    }
  }

  /**This method updates the Price column of Product based on given product code.
   *
   * @param code Product code
   * @param price Price to be updated
   * @return updated Price - Double
   * @throws ApplicationErrorException Exception thrown due to Persistence problems.
   */
  public int updatePrice(String code,double price) throws ApplicationErrorException {
    try{
      return productConnection.createStatement().executeUpdate("UPDATE PRODUCT SET PRICE="+price+" WHERE CODE='"+code+"'");
    }catch(Exception e)
    {
      throw new ApplicationErrorException(e.getMessage());
    }
  }
}
