package Service;

import DAO.*;
import Entity.Product;
import org.checkerframework.checker.units.qual.C;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProductServiceImplementation implements ProductService {
    public int createProductService (Product product) throws SQLException, ApplicationErrorException, UniqueNameException {
        ProductDAO productcreateDAO = new ProductDAOImplementation ();
        Product productResult = productcreateDAO.create (product);
        if (productResult != null) {
            if (! (productResult.getName () == null)) {
                return 1;
            } else {
                return - 1;
            }
        } else {
            return 0;
        }
    }

    public int countProductService () throws ApplicationErrorException {
        ProductDAO productCountDAO = new ProductDAOImplementation ();
        return productCountDAO.count ();
    }

    public List<Product> listProductService (HashMap<String, String> listattributes) throws ApplicationErrorException, PageCountOutOfBoundsException {
        List<Product> productList;
        ProductDAO listProductDAO = new ProductDAOImplementation ();

        if (Collections.frequency (listattributes.values (), null) == listattributes.size () - 2 && listattributes.get ("Pagelength") != null && listattributes.get ("Pagenumber") != null) {
            productList = listProductDAO.list (Integer.parseInt (listattributes.get ("Pagelength")), Integer.parseInt (listattributes.get ("Pagenumber")));
            return productList;
        } else if (Collections.frequency (listattributes.values (), null) == 0) {
            int pageLength = Integer.parseInt (listattributes.get ("Pagelength"));
            int pageNumber = Integer.parseInt (listattributes.get ("Pagenumber"));
            int offset = (pageLength * pageNumber) - pageLength;
            productList = listProductDAO.list (listattributes.get ("Attribute"), listattributes.get ("Searchtext"), pageLength, offset);
            return productList;
        } else if (Collections.frequency (listattributes.values (), null) == listattributes.size () - 1 && listattributes.get ("Searchtext") != null) {
            productList = listProductDAO.list (listattributes.get ("Searchtext"));
            return productList;
        }
        return null;
    }

    public int editProductService (HashMap<String, String> attributeMap) throws SQLException, ApplicationErrorException, UniqueNameException, UniqueConstraintException, UnitCodeViolationException {
        ProductDAO productEditDAO = new ProductDAOImplementation ();
        String numberRegex = "^[0-9]*$";
        String procodeRegex = "^[a-zA-Z0-9]{2,6}$";
        String nameRegex = "^[a-zA-Z\\s]{3,30}$";
        int id;
        try {
            id = Integer.parseInt (attributeMap.get ("id").trim ());
        } catch (NumberFormatException e) {
            return 0;
        }
        if (attributeMap.get ("name") != null && ! attributeMap.get ("name").matches (nameRegex) || attributeMap.get ("code") != null && ! attributeMap.get ("code").matches (procodeRegex) || attributeMap.get ("type") != null && ! attributeMap.get ("type").matches (nameRegex) || attributeMap.get ("price") != null && ! attributeMap.get ("price").matches (numberRegex)) {
            return 0;
        }
        boolean status;
        int updateCount = 0;
        if (attributeMap.get ("name") != null) {
            status = productEditDAO.edit (id, "name", attributeMap.get ("name"));
            if (! status) {
                return - 1;
            } else {
                updateCount++;
            }
        }
        if (attributeMap.get ("code") != null) {
            if (productEditDAO.edit (id, "code", attributeMap.get ("code"))) {
                updateCount++;
            }
        }
        if (attributeMap.get ("unitcode") != null) {
            if (productEditDAO.edit (id, "unitcode", attributeMap.get ("unitcode"))) {
                updateCount++;
            }
        }
        if (attributeMap.get ("type") != null) {
            if (productEditDAO.edit (id, "type", attributeMap.get ("type"))) {
                updateCount++;
            }
        }
        if (attributeMap.get ("price") != null) {
            if (productEditDAO.edit (id, "price", attributeMap.get ("price"))) {
                updateCount++;
            }
        }
        if (updateCount == (attributeMap.size () - Collections.frequency (attributeMap.values (), null) - 1)) {
            return 1;
        } else {
            return - 1;
        }
    }

    public int deleteProductService (String parameter) throws ApplicationErrorException {
        ProductDAO deleteProductDAO = new ProductDAOImplementation ();
        return deleteProductDAO.delete (parameter);
    }
}
