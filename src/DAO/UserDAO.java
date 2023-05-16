package DAO;

import Entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
  User create(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException;

  int count() throws ApplicationErrorException;

  List list(String searchText) throws ApplicationErrorException;

  List list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException;

  boolean edit(User user) throws SQLException, ApplicationErrorException, UniqueConstraintException;

  int delete(String parameter) throws ApplicationErrorException;

  boolean checkIfInitialSetup() throws SQLException;

  String login(String user, String passWord) throws SQLException, ApplicationErrorException;
}
