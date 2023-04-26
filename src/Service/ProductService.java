package Service;

import Entity.Product;

import java.sql.SQLException;
import java.util.HashMap;

public interface ProductService {
    public int createProductService(Product product) throws SQLException;
    public int countProductService();
    public void listProductService(HashMap<String,String> listattributes);
    public void editProductService(HashMap<String,String> attributeMap) throws SQLException;
    public void deleteProductService(String parameter);




}
