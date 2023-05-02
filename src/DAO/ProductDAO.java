package DAO;

import Entity.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    Product create(Product product) throws SQLException, ApplicationErrorException, UniqueNameException;
    int count() throws ApplicationErrorException;
    List list() throws ApplicationErrorException;
    List list(int pageLength) throws ApplicationErrorException;
    List list(int pageLength,int pageNumber) throws ApplicationErrorException, PageCountOutOfBoundsException;
    List list(String attribute,String searchText) throws ApplicationErrorException;
    List list(String attribute,String searchText,int pageLength,int offset ) throws ApplicationErrorException;
    List list(String searchText) throws ApplicationErrorException;
    boolean edit(int id,String attribute,String value) throws SQLException, ApplicationErrorException, UniqueNameException, UniqueCodeException, UnitCodeViolationException;
    int delete(String parameter) throws ApplicationErrorException;
}
