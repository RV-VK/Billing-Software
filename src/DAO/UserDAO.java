package DAO;

import Entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User create(User user) throws SQLException, ApplicationErrorException;
    int count() throws ApplicationErrorException;
    List list() throws ApplicationErrorException;
    List list(int pageLength) throws ApplicationErrorException;
    List list(String searchText) throws ApplicationErrorException;
    List list(int pageLength,int pageNumber) throws ApplicationErrorException;
    List list(String attribute,String searchText) throws ApplicationErrorException;
    List list(String attribute,String searchText,int pageLength,int offset) throws ApplicationErrorException;
    boolean edit(int id,String attribute,String value) throws SQLException, ApplicationErrorException;
    int delete(String parameter) throws ApplicationErrorException;

}
