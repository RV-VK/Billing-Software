package Service;

import DAO.ApplicationErrorException;
import Entity.Product;

import java.sql.SQLException;
import java.util.HashMap;

public interface ProductService {
    int createProductService(Product product) throws SQLException, ApplicationErrorException;
     int countProductService() throws ApplicationErrorException;
     void listProductService(HashMap<String,String> listattributes) throws ApplicationErrorException;
     int editProductService(HashMap<String,String> attributeMap) throws SQLException, ApplicationErrorException;
     int deleteProductService(String parameter) throws ApplicationErrorException;
}
