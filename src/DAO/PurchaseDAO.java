package DAO;

import Entity.Purchase;

import java.sql.SQLException;
import java.util.List;

public interface PurchaseDAO {
  Purchase create(Purchase purchase) throws ApplicationErrorException, SQLException;

  int count(String parameter) throws ApplicationErrorException;

  List list(String attribute, String searchText, int pageLength, int pageNumber)
      throws ApplicationErrorException;

  List list(String searchText) throws ApplicationErrorException;

  int delete(int invoice) throws ApplicationErrorException;
}
