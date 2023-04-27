package Service;

import DAO.ApplicationErrorException;
import Entity.Product;

import java.sql.SQLException;
import java.util.HashMap;

public interface ProductService {
    public int createProductService(Product product) throws SQLException, ApplicationErrorException;
    public int countProductService() throws ApplicationErrorException;
    public void listProductService(HashMap<String,String> listattributes) throws ApplicationErrorException;
    public int editProductService(HashMap<String,String> attributeMap) throws SQLException, ApplicationErrorException;
    public int deleteProductService(String parameter) throws ApplicationErrorException;




}
