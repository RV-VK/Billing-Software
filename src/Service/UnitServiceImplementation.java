package Service;
import DAO.ApplicationErrorException;
import DAO.UniqueConstraintException;
import DAO.UnitDAO;
import DAO.UnitDAOImplementation;
import Entity.Unit;
import Entity.User;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UnitServiceImplementation implements UnitService {
  @Override
  public int createUnitService(Unit unit) throws SQLException, ApplicationErrorException {
    UnitDAO unitCreateDAO = new UnitDAOImplementation();
    Unit createdUnit = unitCreateDAO.create(unit);
    if (createdUnit != null) {
      return 1;
    } else {
      return -1;
    }
  }

  @Override
  public List<Unit> listUnitService() throws ApplicationErrorException {
    UnitDAO unitListDAO = new UnitDAOImplementation();
    return unitListDAO.list();
  }

  @Override
  public int editUnitService( Unit unit)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    UnitDAO unitEditDAO = new UnitDAOImplementation();
    String unitCodeRegex = "^[a-zA-Z]{1,4}$";
    String nameRegex = "^[a-zA-Z\\s]{3,30}$";
    if (unit.getName() != null && !unit.getName().matches(nameRegex)
        || unit.getCode() != null
            && !unit.getCode().matches(unitCodeRegex)) {
      return 0;
    }
    return unitEditDAO.edit (unit);
  }

  @Override
  public int deleteUnitService(String code) throws ApplicationErrorException {
    UnitDAO unitDeleteDAO = new UnitDAOImplementation();
    return unitDeleteDAO.delete(code);
  }
}
