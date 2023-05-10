package DAO;

import DBConnection.DBHelper;
import Entity.Unit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitDAOImplementation implements UnitDAO {

    @Override
    public Unit create (Unit unit) throws SQLException, ApplicationErrorException {
        Connection unitCreateConnection = DBHelper.getConnection ();
        try {
            unitCreateConnection.setAutoCommit (false);
            String codeQuery = "SELECT * FROM UNIT WHERE CODE='" + unit.getCode () + "'";
            Statement codeRepeatCheckStatement = unitCreateConnection.createStatement ();
            ResultSet codeResultSet = codeRepeatCheckStatement.executeQuery (codeQuery);
            if (codeResultSet.next ()) {
                return null;
            } else {
                PreparedStatement unitCreateStatement = unitCreateConnection.prepareStatement ("INSERT INTO UNIT(NAME,CODE,DESCRIPTION,ISDIVIDABLE) VALUES (?,?,?,?) RETURNING *");
                unitCreateStatement.setString (1, unit.getName ());
                unitCreateStatement.setString (2, unit.getCode ());
                unitCreateStatement.setString (3, unit.getDescription ());
                unitCreateStatement.setBoolean (4, unit.getIsDividable ());
                ResultSet unitCreateResultSet = unitCreateStatement.executeQuery ();
                unitCreateResultSet.next ();
                boolean isDividable;
                if (unitCreateResultSet.getString (4).equals ("t")) {
                    isDividable = true;
                } else {
                    isDividable = false;
                }
                Unit createdUnit = new Unit (unitCreateResultSet.getString (1), unitCreateResultSet.getString (2), unitCreateResultSet.getString (3), isDividable);
                unitCreateConnection.commit ();
                unitCreateConnection.setAutoCommit (true);
                return createdUnit;
            }
        } catch (Exception e) {
            unitCreateConnection.rollback ();
            throw new ApplicationErrorException ("Application has went into an Error!!!\n Please Try again");
        }

    }

    @Override
    public List<Unit> list () throws ApplicationErrorException {
        Connection listConnection = DBHelper.getConnection ();
        List<Unit> unitList = new ArrayList<> ();
        try {
            Statement listStatement = listConnection.createStatement ();
            ResultSet listResultSet = listStatement.executeQuery ("SELECT * FROM UNIT ORDER BY CODE");
            while (listResultSet.next ()) {
                int id = listResultSet.getInt (1);
                String name = listResultSet.getString (2);
                String code = listResultSet.getString (3);
                String description = listResultSet.getString (4);
                boolean isdividable;
                if (listResultSet.getString (5).equals ("t")) {
                    isdividable = true;
                } else {
                    isdividable = false;
                }
                Unit listedUnit = new Unit (id, name, code, description, isdividable);
                unitList.add (listedUnit);
            }
            return unitList;
        } catch (Exception e) {
            e.printStackTrace ();
            throw new ApplicationErrorException ("Application has went into an Error!!!\n Please Try again");
        }
    }

    @Override
    public int edit (int id, String atrribute, String value) throws ApplicationErrorException, SQLException, UniqueConstraintException {
        Connection editConnection = DBHelper.getConnection ();
        try {
            editConnection.setAutoCommit (false);
            String idCheckQuery = "SELECT * FROM UNIT WHERE ID=" + id;
            Statement idCheckStatement = editConnection.createStatement ();
            ResultSet idCheckResultSet = idCheckStatement.executeQuery (idCheckQuery);
            if (idCheckResultSet.next ()) {
                String editQuery = "UPDATE UNIT SET " + atrribute.toUpperCase () + "=" + "'" + value + "'" + " WHERE ID=" + id;
                Statement editStatement = editConnection.createStatement ();
                editStatement.executeUpdate (editQuery);
                editConnection.commit ();
                editConnection.setAutoCommit (true);
                return 1;
            } else {
                return - 1;
            }
        } catch (SQLException e) {
            if (e.getSQLState ().equals ("23505")) {
                editConnection.rollback ();
                throw new UniqueConstraintException (">>Unit Code must be unique!!!\n The unit code you have entered already exists!!!");
            } else {
                editConnection.rollback ();
                e.printStackTrace ();
                throw new ApplicationErrorException ("Application has went into an Error!!!\n Please Try again");
            }
        }
    }

    @Override
    public int delete (String code) throws ApplicationErrorException {
        Connection deleteConnection = DBHelper.getConnection ();
        try {
            Statement deleteStatement = deleteConnection.createStatement ();
            if (! (deleteStatement.executeUpdate ("DELETE FROM UNIT WHERE CODE='" + code + "'") > 0)) {
                return - 1;
            } else {
                return 1;
            }
        } catch (Exception e) {
            throw new ApplicationErrorException ("Application has went into an Error!!!\n Please Try again");
        }

    }


}
