package Service;

import DAO.ApplicationErrorException;
import DAO.UniqueConstraintException;
import Entity.Unit;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface UnitService {
  Unit createUnitService(Unit unit)
      throws SQLException, ApplicationErrorException, UniqueConstraintException;

  List<Unit> listUnitService() throws ApplicationErrorException;

  int editUnitService(Unit unit)
      throws SQLException, ApplicationErrorException, UniqueConstraintException;

  int deleteUnitService(String code) throws ApplicationErrorException;
}
