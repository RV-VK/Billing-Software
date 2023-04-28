package Service;

import DAO.ApplicationErrorException;
import Entity.Product;
import Entity.User;

import java.sql.SQLException;
import java.util.HashMap;

public interface UserService {
     int createUserService(User user) throws SQLException, ApplicationErrorException;
     int countUserService() throws ApplicationErrorException;
     void listUserService(HashMap<String,String> listattributes) throws ApplicationErrorException;
     int editUserService(HashMap<String,String> attributeMap) throws SQLException, ApplicationErrorException;
     int deleteUserService(String username) throws ApplicationErrorException;
}
