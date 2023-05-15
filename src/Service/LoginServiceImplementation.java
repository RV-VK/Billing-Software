package Service;

import DAO.ApplicationErrorException;
import DAO.UniqueConstraintException;
import DAO.UserDAO;
import DAO.UserDAOImplementation;
import Entity.User;

import java.sql.SQLException;

public class LoginServiceImplementation implements  LoginService{
    private UserDAO userDAO=new UserDAOImplementation();
    @Override
    public boolean checkIfInitialSetup() throws SQLException {
        return userDAO.checkIfInitialSetup();
    }

    @Override
    public User createUser(User user) throws UniqueConstraintException, SQLException, ApplicationErrorException {
        return userDAO.create(user);
    }

    @Override
    public String  login(String userName, String passWord) throws SQLException {
        return userDAO.login(userName,passWord);
    }


}