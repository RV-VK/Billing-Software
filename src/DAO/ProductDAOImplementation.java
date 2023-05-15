package DAO;

import DBConnection.DBHelper;
import Entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImplementation implements ProductDAO {
    private Connection productConnection = DBHelper.getConnection();
    /**
     * This Method Creates an Entry in the Product Table
     *
     * @param product - Input product
     * @return product - Entered product
     * @throws ApplicationErrorException
     * @throws SQLException
     * @throws UniqueConstraintException
     */
    @Override
    public Product create(Product product) throws ApplicationErrorException, SQLException, UniqueConstraintException {
        try {
            productConnection.setAutoCommit(false);
            PreparedStatement productCreateStatement = productConnection.prepareStatement("INSERT INTO PRODUCT(CODE,NAME,unitcode,TYPE,PRICE,STOCK) VALUES (?,?,?,?,?,?) RETURNING *");
            productCreateStatement.setString(1, product.getCode());
            productCreateStatement.setString(2, product.getName());
            productCreateStatement.setString(3, product.getunitcode());
            productCreateStatement.setString(4, product.getType());
            productCreateStatement.setDouble(5, product.getPrice());
            productCreateStatement.setFloat(6, product.getAvailableQuantity());
            ResultSet productCreateResultSet = productCreateStatement.executeQuery();
            productCreateResultSet.next();
            Product createdProduct = new Product(productCreateResultSet.getInt(1), productCreateResultSet.getString(2), productCreateResultSet.getString(3), productCreateResultSet.getString(4), productCreateResultSet.getString(5), productCreateResultSet.getFloat(6), productCreateResultSet.getDouble(7), productCreateResultSet.getDouble(8));
            productConnection.commit();
            productConnection.setAutoCommit(true);
            return createdProduct;
        } catch(SQLException e) {
            productConnection.rollback();
            if(e.getSQLState().equals("23503")) {
                return null;
            } else if(e.getSQLState().equals("23505")) {
                if(e.getMessage().contains("product_name"))
                    throw new UniqueConstraintException(">> Name must be unique!!!\n>> The Name you have entered already exists!!!");
                else
                    throw new UniqueConstraintException(">> Code must be unique!!!\n>> The code you have entered already exists!!!");
            } else {
                throw new ApplicationErrorException(">> Application has went into an Error!!!\n>>Please Try again");
            }
        }
    }

    /**
     * This Method returns the number of entries in the Product table.
     *
     * @return count
     * @throws ApplicationErrorException
     */
    @Override
    public int count() throws ApplicationErrorException {
        try {
            Statement countStatement = productConnection.createStatement();
            ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(ID) FROM PRODUCT");
            int count = 0;
            while(countResultSet.next()) {
                count = countResultSet.getInt(1);
            }
            return count;
        } catch(Exception e) {
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }

    /**
     * This method Lists the products in the product table based on the given search-text.
     * @param searchText
     * @return List<Product>
     * @throws ApplicationErrorException
     */
    public List<Product> list(String searchText) throws ApplicationErrorException {
        List<Product> productList = new ArrayList<>();
        try {
            Statement listStatement = productConnection.createStatement();
            String listQuery = "SELECT * FROM PRODUCT WHERE NAME ILIKE '" + searchText + "' OR CODE ILIKE '" + searchText + "' OR UNITCODE ILIKE '" + searchText + "' OR TYPE ILIKE '" + searchText + "' OR CAST(ID AS TEXT) ILIKE '" + searchText + "' OR CAST(STOCK AS TEXT) ILIKE '" + searchText + "' OR CAST(PRICE AS TEXT) ILIKE '" + searchText + "'";
            ResultSet listresultSet = listStatement.executeQuery(listQuery);
            while(listresultSet.next()) {
                Product listedProduct = new Product(listresultSet.getInt(1), listresultSet.getString(2), listresultSet.getString(3), listresultSet.getString(4), listresultSet.getString(5), listresultSet.getFloat(6), listresultSet.getDouble(7), listresultSet.getDouble(8));
                productList.add(listedProduct);
            }
            return productList;
        } catch(SQLException e) {
            e.printStackTrace();
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }

    @Override
    public List<Product> list(String attribute, String searchText, int pageLength, int offset) throws ApplicationErrorException {
        List<Product> productList = new ArrayList<>();
        try {
            String listQuery = "SELECT * FROM PRODUCT WHERE " + attribute + "= COALESCE(?," + attribute + ")" + " ORDER BY ID LIMIT " + pageLength + "  OFFSET " + offset;
            PreparedStatement listStatement = productConnection.prepareStatement(listQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if(attribute.equals("id") && searchText == null) {
                listStatement.setNull(1, Types.INTEGER);
            } else if(attribute.equals("id") || attribute.equals("stock") || attribute.equals("price")) {
                listStatement.setDouble(1, Double.parseDouble(searchText));
            } else {
                listStatement.setString(1, searchText);
            }
            ResultSet listResultSet = listStatement.executeQuery();
            while(listResultSet.next()) {
                Product listedProduct = new Product(listResultSet.getInt(1), listResultSet.getString(2), listResultSet.getString(3), listResultSet.getString(4), listResultSet.getString(5), listResultSet.getFloat(6), listResultSet.getDouble(7), listResultSet.getDouble(8));
                productList.add(listedProduct);
            }
            return productList;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }

    @Override
    public boolean edit(Product product) throws SQLException, ApplicationErrorException, UniqueConstraintException, UnitCodeViolationException {
        try {
            productConnection.setAutoCommit(false);
            String editQuery = "UPDATE PRODUCT SET CODE= COALESCE(?,CODE),NAME= COALESCE(?,NAME),UNITCODE= COALESCE(?,UNITCODE),TYPE= COALESCE(?,TYPE),PRICE= COALESCE(?,PRICE) WHERE ID=? ";
            PreparedStatement editStatement = productConnection.prepareStatement(editQuery);
            editStatement.setString(1, product.getCode());
            editStatement.setString(2, product.getName());
            editStatement.setString(3, product.getunitcode());
            editStatement.setString(4, product.getType());
            if(product.getPrice() == 0) {
                editStatement.setNull(5, Types.NUMERIC);
            } else {
                editStatement.setDouble(5, product.getPrice());
            }
            editStatement.setInt(6, product.getId());
            if(editStatement.executeUpdate() > 0) {
                productConnection.commit();
                productConnection.setAutoCommit(true);
                return true;
            }
        } catch(SQLException e) {
            if(e.getSQLState().equals("23505")) {
                productConnection.rollback();
                if(e.getMessage().contains("product_code")) {
                    throw new UniqueConstraintException(">>Code must be unique!!!\n>>The code you have entered already exists!!!");
                } else if(e.getMessage().contains("product_name")) {
                    throw new UniqueConstraintException("Name must be unique!!!\n>>The Name you have entered already exists!!!");
                }
            } else if(e.getSQLState().equals("23503")) {
                productConnection.rollback();
                throw new UnitCodeViolationException(">>The unitcode you have entered doesnt exist!!!");
            } else {
                productConnection.rollback();
                e.printStackTrace();
                throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
            }
        }
        return false;
    }

    @Override
    public int delete(String parameter) throws ApplicationErrorException {
        try {
            Statement deleteStatement = productConnection.createStatement();
            ResultSet stockResultSet;
            if(Character.isAlphabetic(parameter.charAt(0)))
                stockResultSet = deleteStatement.executeQuery("SELECT STOCK FROM PRODUCT WHERE CODE='" + parameter + "'");
            else
                stockResultSet = deleteStatement.executeQuery("SELECT STOCK FROM PRODUCT WHERE ID='" + parameter + "'");
            if(! stockResultSet.next()) return - 1;
            float stock = stockResultSet.getFloat(1);
            if(stock > 0) return 0;
            else {
                if(Character.isAlphabetic(parameter.charAt(0)))
                    return deleteStatement.executeUpdate("UPDATE PRODUCT SET ISDELETED='TRUE' WHERE CODE ='" + parameter + "'");
                else
                    return deleteStatement.executeUpdate("UPDATE PRODUCT SET ISDELETED='TRUE' WHERE ID ='" + parameter + "'");
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }

    @Override
    public Product findByCode(String code) throws ApplicationErrorException {
        try {
            Statement getProductStatement = productConnection.createStatement();
            ResultSet getProductResultSet = getProductStatement.executeQuery("SELECT * FROM PRODUCT  WHERE CODE='" + code + "'");
            Product product = null;
            while(getProductResultSet.next()) {
                product = new Product(getProductResultSet.getInt(1), getProductResultSet.getString(2), getProductResultSet.getString(3), getProductResultSet.getString(4), getProductResultSet.getString(5), getProductResultSet.getFloat(6), getProductResultSet.getDouble(7), getProductResultSet.getDouble(8));
            }
            return product;
        } catch(Exception e) {
            throw new ApplicationErrorException(e.getMessage());
        }
    }
}