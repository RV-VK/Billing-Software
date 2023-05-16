package Service;

import DAO.*;
import Entity.User;
import java.sql.SQLException;
import java.util.*;

public class UserServiceImplementation implements UserService {

  private UserDAO userDAO = new UserDAOImplementation();
  private final String NAME_REGEX = "^[a-zA-Z\\s]{1,30}$";
  private final String PASSWORD_REGEX = "^[a-zA-Z0-9]{8,30}$";
  private final List<String> userTypeList = Arrays.asList("Sales", "Purchase", "Admin");
  private final String PHONE_NUMBER_REGEX = "^[6789]\\d{9}$";

  @Override
  public User createUserService(User user)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    if (validate(user)) return userDAO.create(user);
    else return null;
  }

  @Override
  public int countUserService() throws ApplicationErrorException {
    return userDAO.count();
  }

  @Override
  public List listUserService(HashMap<String, String> listattributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException {
    List<User> userList;
    if (Collections.frequency(listattributes.values(), null) == 0
        || Collections.frequency(listattributes.values(), null) == 1) {
      int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
      int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
      int offset = (pageLength * pageNumber) - pageLength;
      userList =
          userDAO.list(
              listattributes.get("Attribute"),
              listattributes.get("Searchtext"),
              pageLength,
              offset);
      return userList;
    } else if (Collections.frequency(listattributes.values(), null) == listattributes.size() - 1
        && listattributes.get("Searchtext") != null) {
      userList = userDAO.list(listattributes.get("Searchtext"));
      return userList;
    }
    return null;
  }

  @Override
  public int editUserService(User user)
      throws SQLException, ApplicationErrorException, UniqueConstraintException {
    if (!validate(user)) return 0;
    boolean status = userDAO.edit(user);
    if (status) {
      return 1;
    } else {
      return -1;
    }
  }

  @Override
  public int deleteUserService(String username) throws ApplicationErrorException {
    return userDAO.delete(username);
  }

  private boolean validate(User user) {
    if ((user.getUserName()!=null&&!user.getUserName().matches(NAME_REGEX))
        || (user.getFirstName()!=null&&!user.getFirstName().matches(NAME_REGEX))
        || (user.getLastName()!=null&&!user.getLastName().matches(NAME_REGEX))
        || (user.getPassWord()!=null&&!user.getPassWord().matches(PASSWORD_REGEX))
        || (user.getPhoneNumber()!=0&&!String.valueOf(user.getPhoneNumber()).matches(PHONE_NUMBER_REGEX))
        || (user.getUserType()!=null&&!userTypeList.contains(user.getUserType()))) return false;
    else return true;
  }
}
