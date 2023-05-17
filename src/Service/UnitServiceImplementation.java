package Service;

import DAO.ApplicationErrorException;
import DAO.UniqueConstraintException;
import DAO.UnitDAO;
import DAO.UnitDAOImplementation;
import Entity.Unit;

import java.sql.SQLException;
import java.util.List;

public class UnitServiceImplementation implements UnitService {
  private UnitDAO unitDAO = new UnitDAOImplementation();
  private final String NAME_REGEX = "^[a-zA-Z\\s]{1,30}$";
  private final String CODE_REGEX = "^[a-zA-Z]{1,4}$";

  @Override
  public Unit createUnitService(Unit unit)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    if (validate(unit)) return unitDAO.create(unit);
    else return null;
  }

  @Override
  public List<Unit> listUnitService() throws ApplicationErrorException {
    return unitDAO.list();
  }

  @Override
  public int editUnitService(Unit unit)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    if (!validate(unit)) {
      return 0;
    }
    return unitDAO.edit(unit);
  }

  @Override
  public int deleteUnitService(String code) throws ApplicationErrorException {
    UnitDAO unitDeleteDAO = new UnitDAOImplementation();
    return unitDeleteDAO.delete(code);
  }

  private boolean validate(Unit unit) {
    if ((unit.getName()!=null&&!unit.getName().matches(NAME_REGEX)) || (unit.getCode()!=null&&!unit.getCode().matches(CODE_REGEX))) return false;
    else return true;
  }
}
