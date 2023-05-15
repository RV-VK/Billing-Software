package DAO;

import Entity.Unit;
import java.sql.SQLException;
import java.util.List;

public interface UnitDAO {
  Unit create(Unit unit) throws SQLException, ApplicationErrorException, UniqueConstraintException;

  List<Unit> list() throws ApplicationErrorException;

  int edit(Unit unit) throws ApplicationErrorException, SQLException, UniqueConstraintException;

  int delete(String code) throws ApplicationErrorException;

  Unit findByCode(String code) throws ApplicationErrorException;
}
