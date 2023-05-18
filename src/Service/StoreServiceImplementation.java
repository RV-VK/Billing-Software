package Service;

import DAO.ApplicationErrorException;
import DAO.StoreDAO;
import DAO.StoreDAOImplementation;
import Entity.Store;

import java.sql.SQLException;

public class StoreServiceImplementation implements StoreService {
 private final StoreDAO storeDAO = new StoreDAOImplementation();
  private final String NAME_REGEX = "^[a-zA-Z\\s]{1,30}$";
  private final String PHONE_NUMBER_REGEX = "^[6789]\\d{9}$";
  private final String GST_NUMBER_REGEX = "^[a-zA-Z0-9]{15}$";

  @Override
  public Store createStoreService(Store store) throws SQLException, ApplicationErrorException {
    if (validate(store)) return storeDAO.create(store);
    else return new Store();
  }

  @Override
  public int editStoreService(Store store) throws SQLException, ApplicationErrorException {
    if (!validate(store)) {
      return 0;
    }
    return storeDAO.edit(store);
  }

  @Override
  public int deleteStoreService(String adminPassword) throws ApplicationErrorException {
    return storeDAO.delete(adminPassword);
  }

  private boolean validate(Store store) {
    if ((store.getName()!=null&&!store.getName().matches(NAME_REGEX))
        || (store.getPhoneNumber()!=0&&!String.valueOf(store.getPhoneNumber()).matches(PHONE_NUMBER_REGEX))
        || (store.getGstCode()!=null&&!String.valueOf(store.getGstCode()).matches(GST_NUMBER_REGEX))) return false;
    else return true;
  }
}
