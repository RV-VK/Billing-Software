package Service;

import DAO.*;
import Entity.Product;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface ProductService {
    int createProductService(Product product) throws SQLException, ApplicationErrorException, UniqueNameException;
     int countProductService() throws ApplicationErrorException;
     List<Product>  listProductService(HashMap<String,String> listattributes) throws ApplicationErrorException, PageCountOutOfBoundsException;
     int editProductService(HashMap<String,String> attributeMap) throws SQLException, ApplicationErrorException, UniqueNameException, UniqueConstraintException, UnitCodeViolationException;
     int deleteProductService(String parameter) throws ApplicationErrorException;
}
