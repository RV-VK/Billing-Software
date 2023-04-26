package DAO;

import DBConnection.DBHelper;
import Entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImplementation implements ProductDAO{
    @Override
    public Product create(Product product) throws SQLException {
        Connection productCreateConnection = DBHelper.getConnection();
        try {
            productCreateConnection.setAutoCommit(false);
            String codeQuery = "SELECT * FROM PRODUCT WHERE CODE='" +product.getCode()+"'";
            Statement codeRepeatCheckStatement = productCreateConnection.createStatement();
            ResultSet codeResultSet=codeRepeatCheckStatement.executeQuery(codeQuery);
            if(codeResultSet.next())
            {
                return null;
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
                return createdProduct;
            }
        }
        catch(Exception e){
            productCreateConnection.rollback();
            return null;
        }

    }

    @Override
    public int count() {
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
            return -1;
        }

    }

    @Override
    public List<Product> list() {
        Connection listConnection= DBHelper.getConnection();
        List<Product> productList=new ArrayList<>();
        try{
            Statement listStatement=listConnection.createStatement();
            ResultSet listresultSet=listStatement.executeQuery("SELECT * FROM PRODUCT ORDER BY ID LIMIT 20 ");
            while(listresultSet.next()) {
                Product listedProduct=new Product(listresultSet.getInt(1),listresultSet.getString(2),listresultSet.getString(3),listresultSet.getString(4),listresultSet.getString(5),listresultSet.getFloat(6),listresultSet.getDouble(7),listresultSet.getDouble(8));
                productList.add(listedProduct);
            }
            return productList;
        }
        catch(Exception e)
        {
            return null;
        }

    }

    @Override
    public List<Product> list(int pageLength) {
        Connection listConnection= DBHelper.getConnection();
        List<Product> productList=new ArrayList<>();
        try{
            Statement listStatement=listConnection.createStatement();
            ResultSet listresultSet=listStatement.executeQuery("SELECT * FROM PRODUCT ORDER BY ID");
            while(listresultSet.next()) {
                Product listedProduct=new Product(listresultSet.getInt(1),listresultSet.getString(2),listresultSet.getString(3),listresultSet.getString(4),listresultSet.getString(5),listresultSet.getFloat(6),listresultSet.getDouble(7),listresultSet.getDouble(8));
                productList.add(listedProduct);
            }
            return productList;
        }
        catch(Exception e)
        {
            return null;
        }


    }

    @Override
    public List<Product> list(int pageLength, int pageNumber) {
        Connection listConnection= DBHelper.getConnection();
        List<Product> productList=new ArrayList<>();
        int count=0;
        try {
            Statement countStatement = listConnection.createStatement();
            ResultSet countResultSet=countStatement.executeQuery("SELECT COUNT(ID) FROM PRODUCT");
            count=0;
            while(countResultSet.next()) {
                count = countResultSet.getInt(1);
            }
        }
        catch(Exception e) {
            System.out.println(">> Something went Wrong! Please Try Again");
        }
        if(count<=((pageLength*pageNumber)-pageLength))
        {
            System.out.println(">> Requested page doesnt exist !!!");
            System.out.println(">> Existing page count with given pagination "+(count/pageLength)+1);
            return null;
        }
        else
        {
            try {
                int begin=(pageLength*pageNumber)-pageLength;
                int end=(pageLength*pageNumber);
                PreparedStatement listStatement = listConnection.prepareStatement("SELECT * FROM PRODUCT WHERE ID BETWEEN ? AND ?");
                listStatement.setInt(1,begin);
                listStatement.setInt(2,end);
                ResultSet listResultSet=listStatement.executeQuery();
                while(listResultSet.next())
                {
                    Product listedProduct=new Product(listResultSet.getInt(1),listResultSet.getString(2),listResultSet.getString(3),listResultSet.getString(4),listResultSet.getString(5),listResultSet.getFloat(6),listResultSet.getDouble(7),listResultSet.getDouble(8));
                    productList.add(listedProduct);
                }
                return productList;
            }

            catch(Exception e) {
                System.out.println(">> Something Went Wrong !!!");
                return null;
            }
        }

    }

    @Override
    public List<Product> list(String attribute, String searchText) {
        Connection listConnection= DBHelper.getConnection();
        List<Product> productList=new ArrayList<>();
        try{
            Statement listStatement=listConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String listQuery="SELECT * FROM PRODUCT WHERE "+attribute+" = '"+searchText+"'"+" ORDER BY ID";
            ResultSet listResultSet=listStatement.executeQuery(listQuery);
            if(listResultSet.next()) {
                listResultSet.beforeFirst();
                while (listResultSet.next()) {
                    Product listedProduct=new Product(listResultSet.getInt(1),listResultSet.getString(2),listResultSet.getString(3),listResultSet.getString(4),listResultSet.getString(5),listResultSet.getFloat(6),listResultSet.getDouble(7),listResultSet.getDouble(8));
                    productList.add(listedProduct);
                }
                return productList;
            }
            else {
                System.out.println(">> SearchText not Found ! Please try with an existing attribute value");
                return null;
            }
        }
        catch(Exception e)
        {
            System.out.println(">> "+e);
            return null;
        }

    }
    @Override
    public List<Product> list(String attribute, String searchText, int pageLength, int offset) {
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
        } catch (Exception e) {
            System.out.println(">> " + e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean edit(int id, String attribute, String value) throws SQLException {
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
                editStatement.executeUpdate(editQuery);
                editConnection.commit();
                return true;
            }
            else {
                System.out.println(">> The id you have entered does not exist");
                System.out.println(">> Please try with an existing id");
                return false;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(">> Template Mismatch! Please Try \"product edit help\" for proper attribute Constraints");
            editConnection.rollback();
            return false;
        }
    }
    @Override
    public boolean delete(String parameter) {
        Connection getCountConnection= DBHelper.getConnection();
        String numberRegex="^[0-9]*$";
        String procodeRegex="^[a-zA-Z0-9]{2,6}$";
        try {
            Statement countStatement = getCountConnection.createStatement();
            if(parameter.matches(numberRegex)) {
                boolean status = countStatement.execute("DELETE FROM PRODUCT WHERE ID='" + parameter + "'");
                return status;
            }
            else if(parameter.matches(procodeRegex)) {
                boolean status=countStatement.execute("DELETE FROM PRODUCT WHERE CODE='"+parameter+"'");
                return status;
            }
        }
        catch(Exception e) {
            return false;
        }
        return false;
    }
}
