package Service;

import DAO.ApplicationErrorException;
import DAO.StoreDAO;
import DAO.StoreDAOImplementation;
import Entity.Store;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;

public class StoreServiceImplementation implements StoreService {
  @Override
  public int createStoreService(Store store) throws SQLException, ApplicationErrorException {
    StoreDAO storeCreateDAO = new StoreDAOImplementation();
    Store createdStore = storeCreateDAO.create(store);
    if (createdStore != null) {
      return 1;
    } else {
      return -1;
    }
  }

  @Override
  public int editStoreService(Store store)
      throws SQLException, ApplicationErrorException {
    StoreDAO storeEditDAO = new StoreDAOImplementation();
    String nameRegex = "^[a-zA-Z\\s]{3,30}$";
    String addressRegex="^[a-zA-Z0-9\\s]{3,30}$";
    if( (store.getName() != null && !store.getName().matches(nameRegex))
        || (store.getAddress()) != null && !store.getAddress().matches(addressRegex)){
      return 0;
    }
   return storeEditDAO.edit (store);
  }

  @Override
  public int deleteStoreService(String adminPassword) throws ApplicationErrorException {
    StoreDAO storeDeleteDAO = new StoreDAOImplementation();
    return storeDeleteDAO.delete(adminPassword);
  }
}
