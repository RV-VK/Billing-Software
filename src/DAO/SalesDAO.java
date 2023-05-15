package DAO;

import Entity.Sales;

import java.sql.SQLException;
import java.util.List;

public interface SalesDAO {
  Sales create(Sales sales) throws ApplicationErrorException, SQLException;

  int count(String parameter) throws ApplicationErrorException;

  List list(String attribute, String searchText, int pageLength, int pageNumber)
      throws ApplicationErrorException;

  List list(String searchText) throws ApplicationErrorException;

  int delete(int id) throws ApplicationErrorException;
}
