package DAO;

import DBConnection.DBHelper;
import Entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImplementation implements ProductDAO{
    @Override
    public Product create(Product product) throws ApplicationErrorException, SQLException, UniqueNameException {
        Connection productCreateConnection = DBHelper.getConnection();
        try {
            productCreateConnection.setAutoCommit(false);
            String codeQuery = "SELECT * FROM PRODUCT WHERE CODE='" +product.getCode()+"'";
            Statement codeRepeatCheckStatement = productCreateConnection.createStatement();
            ResultSet codeResultSet=codeRepeatCheckStatement.executeQuery(codeQuery);
            if(codeResultSet.next())
            {
                return new Product();
            }
            else {
                PreparedStatement productCreateStatement=productCreateConnection.prepareStatement("INSERT INTO PRODUCT(CODE,NAME,unitcode,TYPE,PRICE,STOCK) VALUES (?,?,?,?,?,?) RETURNING *");
                productCreateStatement.setString(1,product.getCode());
                productCreateStatement.setString(2,product.getName());
                productCreateStatement.setString(3, product.getunitcode());
                productCreateStatement.setString(4,product.getType());
                productCreateStatement.setDouble(5,product.getPrice());
                productCreateStatement.setFloat(6,product.getAvailableQuantity());
                ResultSet productCreateResultSet=productCreateStatement.executeQuery();
                productCreateResultSet.next();
                Product createdProduct=new Product(productCreateResultSet.getInt(1),productCreateResultSet.getString(2),productCreateResultSet.getString(3),productCreateResultSet.getString(4),productCreateResultSet.getString(5),productCreateResultSet.getFloat(6),productCreateResultSet.getDouble(7),productCreateResultSet.getDouble(8));
                productCreateConnection.commit();
                productCreateConnection.setAutoCommit(true);
                return createdProduct;
            }
        }
        catch(SQLException e){
            if(e.getSQLState().equals("23503"))
            {
                productCreateConnection.rollback();
                return null;
            }
            else if(e.getSQLState().equals("23505"))
            {
                productCreateConnection.rollback();
                throw new UniqueNameException(">>Name must be unique!!!\n>>The Name you have entered already exists!!!");
            }
            else {
                productCreateConnection.rollback();
                throw new ApplicationErrorException(">>Application has went into an Error!!!\n>>Please Try again");
            }
        }

    }
    @Override
    public int count() throws ApplicationErrorException {
        Connection getCountConnection= DBHelper.getConnection();
        try {
            Statement countStatement = getCountConnection.createStatement();
            ResultSet countResultSet=countStatement.executeQuery("SELECT COUNT(ID) FROM PRODUCT");
            int count=0;
            while(countResultSet.next()) {
                count = countResultSet.getInt(1);
            }
            return count;
        }
        catch(Exception e) {
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }
    public List<Product> list(int pageLength, int pageNumber) throws ApplicationErrorException, PageCountOutOfBoundsException {
        Connection listConnection= DBHelper.getConnection();
        List<Product> productList=new ArrayList<>();
        int count=0;
        try {
            Statement countStatement = listConnection.createStatement();
            ResultSet countResultSet=countStatement.executeQuery("SELECT COUNT(ID) FROM PRODUCT");
            while(countResultSet.next()) {
                count = countResultSet.getInt(1);
            }
        }
        catch(Exception e) {
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
        if(count<=((pageLength*pageNumber)-pageLength))
        {
            throw new PageCountOutOfBoundsException(">> Requested page doesnt exist !!!\n>> Existing page count with given pagination "+((count/pageLength)+1));
        }
        else
        {
            try {
                int begin=(pageLength*pageNumber)-pageLength;
                PreparedStatement listStatement = listConnection.prepareStatement("SELECT * FROM PRODUCT ORDER BY ID LIMIT "+pageLength+" OFFSET "+begin);
                ResultSet listResultSet=listStatement.executeQuery();
                while(listResultSet.next())
                {
                    Product listedProduct=new Product(listResultSet.getInt(1),listResultSet.getString(2),listResultSet.getString(3),listResultSet.getString(4),listResultSet.getString(5),listResultSet.getFloat(6),listResultSet.getDouble(7),listResultSet.getDouble(8));
                    productList.add(listedProduct);
                }
                return productList;
            }

            catch(Exception e) {
                e.printStackTrace();
                throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
            }
        }

    }
    public List<Product> list(String searchText) throws ApplicationErrorException{
        Connection listConnection=DBHelper.getConnection();
        String codeRegex="^[a-zA-Z\\s]{0,50}$";
        String numberRegex="^[0-9]*$";

        List<Product> productList=new ArrayList<>();
        try{
            if(searchText.matches(codeRegex)) {
                Statement listStatement = listConnection.createStatement();
                String listQuery = "SELECT * FROM PRODUCT WHERE NAME ILIKE '" + searchText + "' OR CODE ILIKE '" + searchText  + "' OR UNITCODE ILIKE '" + searchText + "' OR TYPE ILIKE '" + searchText + "'";
                ResultSet listresultSet = listStatement.executeQuery(listQuery);
                while (listresultSet.next()) {
                    Product listedProduct = new Product(listresultSet.getInt(1), listresultSet.getString(2), listresultSet.getString(3), listresultSet.getString(4), listresultSet.getString(5), listresultSet.getFloat(6), listresultSet.getDouble(7), listresultSet.getDouble(8));
                    productList.add(listedProduct);
                }
                return productList;
            }
            else if(searchText.matches(numberRegex)){
                Statement listStatement=listConnection.createStatement();
                String listQuery="SELECT * FROM PRODUCT WHERE CAST(ID AS TEXT) ILIKE '" + searchText +"' OR CAST(PRICE AS TEXT) ILIKE '"+searchText+"' OR CAST(STOCK AS TEXT) ILIKE '"+searchText+"' OR CAST(COSTPRICE AS TEXT) ILIKE '"+searchText+"'";
                ResultSet listresultSet=listStatement.executeQuery(listQuery);
                while (listresultSet.next()){
                    Product listedProduct = new Product(listresultSet.getInt(1), listresultSet.getString(2), listresultSet.getString(3), listresultSet.getString(4), listresultSet.getString(5), listresultSet.getFloat(6), listresultSet.getDouble(7), listresultSet.getDouble(8));
                    productList.add(listedProduct);
                }
                return  productList;
            }
            else{
                Statement listStatement = listConnection.createStatement();
                String listQuery = "SELECT * FROM PRODUCT WHERE CODE ILIKE '" + searchText+"'";
                ResultSet listresultSet = listStatement.executeQuery(listQuery);
                while (listresultSet.next()) {
                    Product listedProduct = new Product(listresultSet.getInt(1), listresultSet.getString(2), listresultSet.getString(3), listresultSet.getString(4), listresultSet.getString(5), listresultSet.getFloat(6), listresultSet.getDouble(7), listresultSet.getDouble(8));
                    productList.add(listedProduct);
                }
                return productList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }
    @Override
    public List<Product> list(String attribute, String searchText, int pageLength, int offset) throws ApplicationErrorException {
        Connection listConnection= DBHelper.getConnection();
        List<Product> productList=new ArrayList<>();
        try {
            Statement listStatement = listConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String listQuery = "SELECT * FROM PRODUCT WHERE "+attribute+" = '"+searchText+"'"+" ORDER BY ID LIMIT "+pageLength+" OFFSET "+offset;
            ResultSet listResultSet = listStatement.executeQuery(listQuery);
            if(listResultSet.next()) {
                listResultSet.beforeFirst();
                while(listResultSet.next()){
                    Product listedProduct=new Product(listResultSet.getInt(1),listResultSet.getString(2),listResultSet.getString(3),listResultSet.getString(4),listResultSet.getString(5),listResultSet.getFloat(6),listResultSet.getDouble(7),listResultSet.getDouble(8));
                    productList.add(listedProduct);
                }
                return productList;
                }

            else {
                System.out.println(">> SearchText not Found ! Please try with an existing attribute value");
                return null;
            }
        } catch (SQLException e) {
                throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }
    @Override
    public boolean edit(int id, String attribute, String value) throws SQLException, ApplicationErrorException, UniqueNameException, UniqueConstraintException, UnitCodeViolationException {
        Connection editConnection= DBHelper.getConnection();
        try{
            editConnection.setAutoCommit(false);
            String idCheckQuery="SELECT * FROM PRODUCT WHERE ID="+id;
            Statement idCheckStatement=editConnection.createStatement();
            ResultSet idCheckResultSet=idCheckStatement.executeQuery(idCheckQuery);
            if(idCheckResultSet.next())
            {
                String editQuery="UPDATE PRODUCT SET "+attribute.toUpperCase()+"="+"'"+value+"'"+" WHERE ID="+id;
                Statement editStatement=editConnection.createStatement();
                if(editStatement.executeUpdate(editQuery)>0);
                {
                    editConnection.commit();
                    editConnection.setAutoCommit(true);
                    return true;
                }
            }
            else {
                System.out.println(">> The id you have entered does not exist");
                System.out.println(">> Please try with an existing id");
                return false;
            }
        }
        catch(SQLException e)
        {
            if(e.getSQLState().equals("23505"))
            {
                editConnection.rollback();
                if(e.getMessage().contains("product_code"))
                {
                    throw new UniqueConstraintException(">>Code must be unique!!!\n>>The code you have entered already exists!!!");
                }
                else if(e.getMessage().contains("product_name"))
                {
                    throw new UniqueNameException("Name must be unique!!!\n>>The Name you have entered already exists!!!");
                }
                else{
                    throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
                }
            }
            else if(e.getSQLState().equals("23503"))
            {
                editConnection.rollback();
                throw new UnitCodeViolationException(">>The unitcode you have entered doesnt exist!!!");
            }
            else {
                editConnection.rollback();
                e.printStackTrace();
                throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
            }
            }
    }
    @Override
    public int delete(String parameter) throws ApplicationErrorException {
        Connection getCountConnection= DBHelper.getConnection();
        String numberRegex="^[0-9]*$";
        String procodeRegex="^[a-zA-Z0-9]{2,6}$";
        try {
            Statement countStatement = getCountConnection.createStatement();
            if(parameter.matches(numberRegex)) {
                ResultSet stockResultSet=countStatement.executeQuery("SELECT STOCK FROM PRODUCT WHERE ID='"+parameter+"'");
                if(!stockResultSet.next())
                {
                    return -1;
                }
                float stock=stockResultSet.getFloat(1);
                if(stock>0)
                {
                  return 0;
                }
                else {
                    if(countStatement.execute("UPDATE PRODUCT SET ISDELETED='TRUE' WHERE ID='" + parameter + "'"))
                    {
                        return 1;
                    }
                }
                }
            else if(parameter.matches(procodeRegex)) {
                ResultSet stockResultSet=countStatement.executeQuery("SELECT STOCK FROM PRODUCT WHERE CODE='"+parameter+"'");
                if(!stockResultSet.next())
                {
                    return -1;
                }
                float stock=stockResultSet.getFloat(1);
                if(stock>0)
                {
                    return 0;
                }
                else {
                    if (countStatement.execute("UPDATE PRODUCT SET ISDELETED='TRUE' WHERE CODE='" + parameter + "'")) {
                        return 1;
                    }
                }
            }
        }
        catch(Exception e) {
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
        return 1;
    }
}
