package Service;

import DAO.ApplicationErrorException;
import Entity.Store;
import java.sql.SQLException;
import java.util.HashMap;

public interface StoreService {
  Store createStoreService(Store store) throws SQLException, ApplicationErrorException;

  int editStoreService(Store store) throws SQLException, ApplicationErrorException;

  int deleteStoreService(String adminPassword) throws ApplicationErrorException;
}
