package Service;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.Sales;
import java.util.HashMap;
import java.util.List;

public interface SalesService {
  int createSalesService(Sales sales) throws ApplicationErrorException;

  int countSalesService(String parameter) throws ApplicationErrorException;

  List<Sales> listSalesService(HashMap<String, String> listAttributes) throws ApplicationErrorException, PageCountOutOfBoundsException;

  int deleteSalesService(String id) throws ApplicationErrorException;
}
