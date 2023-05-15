package Service;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.Purchase;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface PurchaseService {
  Purchase createPurchaseService(Purchase purchase) throws ApplicationErrorException, SQLException;

  int countPurchaseService(String parameter) throws ApplicationErrorException;

  List<Purchase> listPurchaseService(HashMap<String, String> listattributes)
      throws PageCountOutOfBoundsException, ApplicationErrorException;

  int deletePurchaseService(String invoice) throws ApplicationErrorException;
}
