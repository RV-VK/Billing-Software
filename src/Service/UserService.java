package Service;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import DAO.UniqueConstraintException;
import Entity.User;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface UserService {
  User createUserService(User user)
      throws SQLException, ApplicationErrorException, UniqueConstraintException;

  int countUserService() throws ApplicationErrorException;

  List<User> listUserService(HashMap<String, String> listattributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException;

  int editUserService(User user)
      throws SQLException, ApplicationErrorException, UniqueConstraintException;

  int deleteUserService(String username) throws ApplicationErrorException;
}
