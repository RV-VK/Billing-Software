package DAO;

import Entity.Unit;

import java.sql.SQLException;
import java.util.List;

public interface UnitDAO {
    Unit create(Unit unit) throws SQLException, ApplicationErrorException;
    List<Unit> list() throws ApplicationErrorException;
    int edit(int id,String atrribute, String value) throws ApplicationErrorException, SQLException, UniqueConstraintException;
    int delete(String code) throws ApplicationErrorException;
    boolean isAvailable(String unitcode) throws ApplicationErrorException;
}
