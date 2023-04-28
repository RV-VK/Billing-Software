package DAO;

import Entity.Store;

import java.sql.SQLException;

public interface StoreDAO {
    Store create(Store store) throws ApplicationErrorException, SQLException;
    int edit(String attribute,String value) throws SQLException, ApplicationErrorException;
    int delete(String password) throws ApplicationErrorException;
}
