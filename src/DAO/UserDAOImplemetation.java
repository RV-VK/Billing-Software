package DAO;

import DBConnection.DBHelper;
import Entity.Product;
import Entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImplemetation implements UserDAO{
    @Override
    public User create(User user) throws SQLException, ApplicationErrorException {
        Connection userCreateConnection = DBHelper.getConnection();
        try {
            userCreateConnection.setAutoCommit(false);
            String codeQuery = "SELECT * FROM USERS WHERE USERNAME='" +user.getUserName()+"'";
            Statement codeRepeatCheckStatement = userCreateConnection.createStatement();
            ResultSet codeResultSet=codeRepeatCheckStatement.executeQuery(codeQuery);
            if(codeResultSet.next())
            {
                return null;
            }
            else {
                PreparedStatement userCreateStatement=userCreateConnection.prepareStatement("INSERT INTO USERS(USERNAME,USERTYPE,PASSWORD,FIRSTNAME,LASTNAME,PHONENUMBER) VALUES (?,?,?,?,?,?) RETURNING *");
                userCreateStatement.setString(1,user.getUserName());
                userCreateStatement.setString(2,user.getUserType());
                userCreateStatement.setString(3, user.getPassWord());
                userCreateStatement.setString(4,user.getFirstName());
                userCreateStatement.setString(5,user.getLastName());
                userCreateStatement.setLong(6,user.getPhoneNumber());
                ResultSet userCreateResultSet=userCreateStatement.executeQuery();
                userCreateResultSet.next();
                User createdUser=new User(userCreateResultSet.getString(3),userCreateResultSet.getString(2),userCreateResultSet.getString(4),userCreateResultSet.getString(5),userCreateResultSet.getString(6),userCreateResultSet.getLong(7));
                userCreateConnection.commit();
                userCreateConnection.setAutoCommit(true);
                return createdUser;
            }
        }
        catch(Exception e){
            userCreateConnection.rollback();
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }

    @Override
    public int count() throws ApplicationErrorException {
        Connection getCountConnection= DBHelper.getConnection();
        try {
            Statement countStatement = getCountConnection.createStatement();
            ResultSet countResultSet=countStatement.executeQuery("SELECT COUNT(ID) FROM USERS");
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
    public List list(int pageLength, int pageNumber) throws ApplicationErrorException, PageCountOutOfBoundsException {
        Connection listConnection= DBHelper.getConnection();
        List<User> userList=new ArrayList<>();
        int count=0;
        try {
            Statement countStatement = listConnection.createStatement();
            ResultSet countResultSet=countStatement.executeQuery("SELECT COUNT(ID) FROM USERS");
            count=0;
            while(countResultSet.next()) {
                count = countResultSet.getInt(1);
            }
        }
        catch(Exception e) {
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
        if(count<=((pageLength*pageNumber)-pageLength))
        {

            throw new PageCountOutOfBoundsException(">> Requested page doesnt exist !!!\nExisting page count with given pagination "+(count/pageLength)+1);
        }
        else
        {
            try {
                int begin=(pageLength*pageNumber)-pageLength;
                int end=(pageLength*pageNumber);
                PreparedStatement listStatement = listConnection.prepareStatement("SELECT * FROM USERS WHERE ID BETWEEN ? AND ?");
                listStatement.setInt(1,begin);
                listStatement.setInt(2,end);
                ResultSet listResultSet=listStatement.executeQuery();
                while(listResultSet.next())
                {
                    User listedUser=new User(listResultSet.getInt(1),listResultSet.getString(3),listResultSet.getString(2),listResultSet.getString(4),listResultSet.getString(5),listResultSet.getString(6),listResultSet.getLong(7));
                    userList.add(listedUser);
                }
                return userList;
            }

            catch(Exception e) {
                throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
            }
        }


    }
    @Override
    public List<User> list(String searchText) throws ApplicationErrorException{
        Connection listConnection=DBHelper.getConnection();
        String nameRegex="^[a-zA-Z\\s]{0,50}$";
        String numberRegex="^[0-9]*$";
        List<User> userList=new ArrayList<>();
        try{
            if(searchText.matches(nameRegex)) {
                Statement listStatement = listConnection.createStatement();
                String listQuery = "SELECT * FROM USERS WHERE USERTYPE ILIKE '" +searchText + "' OR USERNAME ILIKE '" + searchText + "' OR FIRSTNAME ILIKE '" + searchText + "' OR LASTNAME ILIKE '" + searchText + "'";
                ResultSet listresultSet = listStatement.executeQuery(listQuery);
                while (listresultSet.next()) {
                    User listedUser = new User(listresultSet.getInt(1), listresultSet.getString(3), listresultSet.getString(2), listresultSet.getString(4), listresultSet.getString(5), listresultSet.getString(6), listresultSet.getLong(7));
                    userList.add(listedUser);
                }
                return userList;
            }
            else
            {
                Statement listStatement=listConnection.createStatement();
                String listQuery="SELECT * FROM USERS WHERE CAST(ID AS TEXT) ILIKE '"+searchText+"' OR CAST(PHONENUMBER AS TEXT) ILIKE '"+searchText+"'";
                ResultSet listresultSet=listStatement.executeQuery(listQuery);
                while (listresultSet.next()) {
                    User listedUser = new User(listresultSet.getInt(1), listresultSet.getString(3), listresultSet.getString(2), listresultSet.getString(4), listresultSet.getString(5), listresultSet.getString(6), listresultSet.getLong(7));
                    userList.add(listedUser);
                }
                return userList;

            }
        } catch (SQLException e) {
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }
    @Override
    public List list(String attribute, String searchText, int pageLength, int offset) throws ApplicationErrorException {
        Connection listConnection= DBHelper.getConnection();
        List<User> userList=new ArrayList<>();
        try {
            Statement listStatement = listConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String listQuery = "SELECT * FROM USERS WHERE "+attribute+" = '"+searchText+"'"+" ORDER BY ID LIMIT "+pageLength+" OFFSET "+offset;
            ResultSet listResultSet = listStatement.executeQuery(listQuery);
            if(listResultSet.next()) {
                listResultSet.beforeFirst();
                while(listResultSet.next()){
                    User listedUser=new User(listResultSet.getInt(1),listResultSet.getString(3),listResultSet.getString(2),listResultSet.getString(4),listResultSet.getString(5),listResultSet.getString(6),listResultSet.getLong(7));
                    userList.add(listedUser);
                }
                return userList;
            }

            else {
                System.out.println(">> SearchText not Found ! Please try with an existing attribute value");
                return null;
            }
        } catch (Exception e) {
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }
    @Override
    public boolean edit(int id, String attribute, String value) throws SQLException, ApplicationErrorException, UniqueConstraintException {
        Connection editConnection= DBHelper.getConnection();
        try{
            editConnection.setAutoCommit(false);
            String idCheckQuery="SELECT * FROM USERS WHERE ID="+id;
            Statement idCheckStatement=editConnection.createStatement();
            ResultSet idCheckResultSet=idCheckStatement.executeQuery(idCheckQuery);
            if(idCheckResultSet.next())
            {
                String editQuery="UPDATE USERS SET "+attribute.toUpperCase()+"="+"'"+value+"'"+" WHERE ID="+id;
                Statement editStatement=editConnection.createStatement();
                editStatement.executeUpdate(editQuery);
                editConnection.commit();
                editConnection.setAutoCommit(true);
                return true;
            }
            else {
                return false;
            }
        }
        catch(SQLException e)
        {
            if(e.getSQLState().equals("23505"))
            {
                throw new UniqueConstraintException(">> Username must be unique!!!\n>>The username you have entered already exists!!!");
            }
            editConnection.rollback();
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }
    @Override
    public int delete(String username) throws ApplicationErrorException {
        Connection deleteConnection=DBHelper.getConnection();
        try{
            Statement deleteStatement=deleteConnection.createStatement();
            if(deleteStatement.execute("DELETE FROM USERS WHERE CODE='"+username+"'"))
            {
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
