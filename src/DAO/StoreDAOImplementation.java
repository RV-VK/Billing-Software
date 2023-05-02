package DAO;

import DBConnection.DBHelper;
import Entity.Store;
import Entity.Unit;

import java.sql.*;

public class StoreDAOImplementation implements StoreDAO{

    @Override
    public Store create(Store store) throws ApplicationErrorException, SQLException {
        Connection storeCreateConnection= DBHelper.getConnection();
        try{
            storeCreateConnection.setAutoCommit(false);
                PreparedStatement unitCreateStatement=storeCreateConnection.prepareStatement("INSERT INTO STORE(NAME,PHONENUMBER,ADDRESS,GSTNUMBER) VALUES (?,?,?,?) RETURNING *");
                unitCreateStatement.setString(1,store.getName());
                unitCreateStatement.setLong(2,store.getPhoneNumber());
                unitCreateStatement.setString(3,store.getAddress());
                unitCreateStatement.setInt(4,store.getGstCode());
                ResultSet storeCreateResultSet=unitCreateStatement.executeQuery();
                storeCreateResultSet.next();
                Store createdStore=new Store(storeCreateResultSet.getString(1),storeCreateResultSet.getLong(2),storeCreateResultSet.getString(3),storeCreateResultSet.getInt(4));
                storeCreateConnection.commit();
                storeCreateConnection.setAutoCommit(true);
                return createdStore;

        }
        catch(Exception e)
        {
            storeCreateConnection.rollback();
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }

    @Override
    public int edit(String attribute, String value) throws SQLException, ApplicationErrorException {
        Connection editConnection=DBHelper.getConnection();
        try{
                String editQuery="UPDATE STORE SET "+attribute.toUpperCase()+"="+"'"+value+"'";
                Statement editStatement=editConnection.createStatement();
                editStatement.executeUpdate(editQuery);
                editConnection.commit();
                editConnection.setAutoCommit(true);
                return 1;
           }
        catch(Exception e)
        {
            editConnection.rollback();
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }

    @Override
    public int delete(String adminPassword) throws ApplicationErrorException {
        Connection deleteConnection=DBHelper.getConnection();
        try{
            Statement passwordCheckStatement=deleteConnection.createStatement();
            ResultSet adminPasswordResultSet=passwordCheckStatement.executeQuery("SELECT PASSWORD FROM USER WHERE USERTYPE='ADMIN'");
            adminPasswordResultSet.next();
            if(adminPasswordResultSet.getString(1).equals(adminPassword)) {
                Statement deleteStatement = deleteConnection.createStatement();
                deleteStatement.execute("DELETE FROM STORE");
                return 1;
            }
            else {
                return -1;
            }

        }
        catch(Exception e)
        {
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }

    }
}