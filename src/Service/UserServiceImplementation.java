package Service;

import DAO.ApplicationErrorException;
import Entity.User;

import java.sql.SQLException;
import java.util.HashMap;

public class UserServiceImplementation implements UserService{
    @Override
    public int createUserService(User user) throws SQLException, ApplicationErrorException {
        return 0;
    }

    @Override
    public int countUserService() throws ApplicationErrorException {
        return 0;
    }

    @Override
    public void listUserService(HashMap<String, String> listattributes) throws ApplicationErrorException {

    }

    @Override
    public int editUserService(HashMap<String, String> attributeMap) throws SQLException, ApplicationErrorException {
        return 0;
    }

    @Override
    public int deleteUserService(String username) throws ApplicationErrorException {
        return 0;
    }
}
