package Service;

import DAO.*;
import Entity.User;
import java.sql.SQLException;
import java.util.*;

public class UserServiceImplementation implements UserService {
  @Override
  public int createUserService(User user) throws SQLException, ApplicationErrorException {
    UserDAO createUserDAO = new UserDAOImplementation();
    User userResult = createUserDAO.create(user);
    if (userResult != null) {
      return 1;
    } else {
      return -1;
    }
  }

  @Override
  public int countUserService() throws ApplicationErrorException {
    UserDAO countUserDAO = new UserDAOImplementation();
    return countUserDAO.count();
  }

  @Override
  public List listUserService(HashMap<String, String> listattributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException {
    List<User> userList;
    UserDAO listUserDAO = new UserDAOImplementation();

    if (Collections.frequency(listattributes.values(), null) == listattributes.size() - 2
        && listattributes.get("Pagelength") != null
        && listattributes.get("Pagenumber") != null) {
      userList =
          listUserDAO.list(
              Integer.parseInt(listattributes.get("Pagelength")),
              Integer.parseInt(listattributes.get("Pagenumber")));
      return userList;
    } else if (Collections.frequency(listattributes.values(), null) == 0) {
      int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
      int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
      int offset = (pageLength * pageNumber) - pageLength;
      userList =
          listUserDAO.list(
              listattributes.get("Attribute"),
              listattributes.get("Searchtext"),
              pageLength,
              offset);
      return userList;
    } else if (Collections.frequency(listattributes.values(), null) == listattributes.size() - 1
        && listattributes.get("Searchtext") != null) {
      userList = listUserDAO.list(listattributes.get("Searchtext"));
      return userList;
    }

    return null;
  }

  @Override
  public int editUserService(User user)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    UserDAO editUserDAO = new UserDAOImplementation();
    String numberRegex = "^[0-9]*$";
    String nameRegex = "^[a-zA-Z\\s]{1,30}$";
    String passwordRegex = "^[a-zA-Z0-9]{8,50}$";
    List<String> userTypeList = new ArrayList<>(Arrays.asList("Admin", "Purchase", "Sales"));
    if (user.getUserName () != null && !user.getUserName ().matches(nameRegex)
        || user.getPassWord () != null
            && !user.getPassWord ().matches(passwordRegex)
        || user.getUserType() != null
            && !userTypeList.contains(user.getUserType())
        || user.getFirstName() != null
            && !user.getFirstName().matches(nameRegex)
        || user.getLastName() != null
            && !user.getLastName().matches(nameRegex)) {
      return 0;
    }
    boolean status=editUserDAO.edit (user);
    if(status)
    {
      return 1;
    }
    else {
      return -1;
    }
  }

  @Override
  public int deleteUserService(String username) throws ApplicationErrorException {
    UserDAO deleteUserDAO = new UserDAOImplementation();
    return deleteUserDAO.delete(username);
  }
}
