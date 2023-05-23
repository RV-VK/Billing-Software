package Service;

import DAO.ApplicationErrorException;
import DAO.UniqueConstraintException;
import Entity.User;

import java.sql.SQLException;

public interface LoginService {
    int checkIfInitialSetup() throws SQLException;
    User createUser(User user) throws UniqueConstraintException, SQLException, ApplicationErrorException;

  String login(String userName, String passWord) throws SQLException, ApplicationErrorException;
}
