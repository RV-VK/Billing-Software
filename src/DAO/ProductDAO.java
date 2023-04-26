package DAO;

import Entity.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    Product create(Product product) throws SQLException;
    int count();
    List list();
    List list(int pageLength);
    List list(int pageLength,int pageNumber);
    List list(String attribute,String searchText);
    List list(String attribute,String searchText,int pageLength,int offset );
    boolean edit(int id,String attribute,String value) throws SQLException;
    boolean delete(String parameter);
}
