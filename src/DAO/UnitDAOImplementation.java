package DAO;

import DBConnection.DBHelper;
import Entity.Unit;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitDAOImplementation implements UnitDAO{

    @Override
    public Unit create(Unit unit) throws SQLException, ApplicationErrorException {
        Connection unitCreateConnection= DBHelper.getConnection();
        try{
            unitCreateConnection.setAutoCommit(false);
            String codeQuery="SELECT * FROM UNIT WHERE CODE='"+unit.getCode()+"'";
            Statement codeRepeatCheckStatement=unitCreateConnection.createStatement();
            ResultSet codeResultSet=codeRepeatCheckStatement.executeQuery(codeQuery);
            if(codeResultSet.next())
            {
                return null;
            }
            else {
                PreparedStatement unitCreateStatement=unitCreateConnection.prepareStatement("INSERT INTO UNIT(NAME,CODE,DESCRIPTION,ISDIVIDABLE) VALUES (?,?,?,?) RETURNING *");
                unitCreateStatement.setString(1,unit.getCode());
                unitCreateStatement.setString(2,unit.getName());
                unitCreateStatement.setString(3,unit.getDescription());
                unitCreateStatement.setBoolean(4,unit.getIsDividable());
                ResultSet unitCreateResultSet=unitCreateStatement.executeQuery();
                unitCreateResultSet.next();
                Unit createdUnit=new Unit(unitCreateResultSet.getString(1),unitCreateResultSet.getString(2),unitCreateResultSet.getString(3),unitCreateResultSet.getBoolean(4));
                unitCreateConnection.commit();
                unitCreateConnection.setAutoCommit(true);
                return createdUnit;
            }
        }
        catch(Exception e)
        {
            unitCreateConnection.rollback();
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }

    }

    @Override
    public List<Unit> list() throws ApplicationErrorException {
        Connection listConnection=DBHelper.getConnection();
        List<Unit> unitList=new ArrayList<>();
        try{
            Statement listStatement=listConnection.createStatement();
            ResultSet listResultSet=listStatement.executeQuery("SELECT * FROM UNIT ORDER BY CODE");
            while(listResultSet.next())
            {
                Unit listedUnit=new Unit(listResultSet.getString(1),listResultSet.getString(2),listResultSet.getString(3),listResultSet.getBoolean(4));
                unitList.add(listedUnit);
            }
            return unitList;
        }
        catch(Exception e)
        {
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }

    @Override
    public int edit(int id, String atrribute, String value) throws ApplicationErrorException, SQLException {
        Connection editConnection=DBHelper.getConnection();
        try{
            editConnection.setAutoCommit(false);
            String idCheckQuery="SELECT * FROM UNIT WHERE ID="+id;
            Statement idCheckStatement=editConnection.createStatement();
            ResultSet idCheckResultSet=idCheckStatement.executeQuery(idCheckQuery);
            if(idCheckResultSet.next())
            {
                String editQuery="UPDATE UNIT SET "+atrribute.toUpperCase()+"="+"'"+value+"'"+" WHERE ID="+id;
                Statement editStatement=editConnection.createStatement();
                editStatement.executeUpdate(editQuery);
                editConnection.commit();
                editConnection.setAutoCommit(true);
                return 1;
            }
            else {
                return -1;
            }
        }
        catch(Exception e)
        {
            editConnection.rollback();
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");
        }
    }

    @Override
    public int delete(String code) throws ApplicationErrorException {
        Connection deleteConnection=DBHelper.getConnection();
        try{
            Statement deleteStatement=deleteConnection.createStatement();
            if(deleteStatement.execute("DELETE FROM UNIT WHERE CODE='"+code+"'"))
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

    public boolean isAvailable(String unitcode) throws ApplicationErrorException {
        Connection checkCodeConnection=DBHelper.getConnection();
        try{
            Statement checkCodeStatment=checkCodeConnection.createStatement();
            String checkCodeQuery="SELECT * FROM UNIT WHERE CODE='"+unitcode+"'";
            if(checkCodeStatment.execute(checkCodeQuery))
            {
                return true;
            }
            else{
                return false;
            }
        }
        catch(Exception e)
        {
            throw new ApplicationErrorException("Application has went into an Error!!!\n Please Try again");

        }
    }
}
