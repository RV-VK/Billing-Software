package Service;

import DAO.ApplicationErrorException;
import Entity.Product;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface ProductService {
    int createProductService(Product product) throws SQLException, ApplicationErrorException;
     int countProductService() throws ApplicationErrorException;
     List<Product> listProductService(HashMap<String,String> listattributes) throws ApplicationErrorException;
     int editProductService(HashMap<String,String> attributeMap) throws SQLException, ApplicationErrorException;
     int deleteProductService(String parameter) throws ApplicationErrorException;
}
