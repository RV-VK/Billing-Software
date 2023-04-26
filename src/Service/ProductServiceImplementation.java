package Service;

import DAO.ProductDAO;
import DAO.ProductDAOImplementation;
import Entity.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProductServiceImplementation implements ProductService{
    public int createProductService(Product product) throws SQLException {
        ProductDAO productcreateDAO=new ProductDAOImplementation();
        Product productResult=productcreateDAO.create(product);
        if(productResult!=null)
        {
          return 1;
        }
        else{
            return -1;
        }
    }
    public int countProductService()
    {
        ProductDAO productCountDAO=new ProductDAOImplementation();
        return productCountDAO.count();
    }
    public void listProductService(HashMap<String,String> listattributes)
    {
        List<Product> productList=new ArrayList<>();
        ProductDAO listProductDAO=new ProductDAOImplementation();
        if(Collections.frequency(listattributes.values(),null)==listattributes.size())
        {
            productList=listProductDAO.list();
            for(Product product: productList)
            {
                System.out.println(">> id: "+product.getId()+", code: "+product.getCode()+", name: "+product.getName()+", type: "+product.getType()+", unitcode: "+product.getunitcode()+", quantity: "+product.getAvailableQuantity()+", price: "+product.getPrice()+", costprice: "+product.getCostPrice());
            }
        }
        else if(Collections.frequency(listattributes.values(),null)==listattributes.size()-1&&listattributes.get("Pagelength")!=null)
        {
            productList=listProductDAO.list(Integer.parseInt(listattributes.get("Pagelength")));
            for(Product product: productList)
            {
                System.out.println(">> id: "+product.getId()+", code: "+product.getCode()+", name: "+product.getName()+", type: "+product.getType()+", unitcode: "+product.getunitcode()+", quantity: "+product.getAvailableQuantity()+", price: "+product.getPrice()+", costprice: "+product.getCostPrice());
            }
        }
        else if(Collections.frequency(listattributes.values(),null)==listattributes.size()-2&&listattributes.get("Pagelength")!=null&&listattributes.get("Pagenumber")!=null)
        {
            productList=listProductDAO.list(Integer.parseInt(listattributes.get("Pagelength")),Integer.parseInt(listattributes.get("Pagenumber")));
            for(Product product: productList)
            {
                System.out.println(">> id: "+product.getId()+", code: "+product.getCode()+", name: "+product.getName()+", type: "+product.getType()+", unitcode: "+product.getunitcode()+", quantity: "+product.getAvailableQuantity()+", price: "+product.getPrice()+", costprice: "+product.getCostPrice());
            }
        }
        else if(Collections.frequency(listattributes.values(),null)==listattributes.size()-2 && listattributes.get("Attribute")!=null &&listattributes.get("Searchtext")!=null)
        {
            productList=listProductDAO.list(listattributes.get("Attribute"),listattributes.get("Searchtext"));
            for(Product product: productList)
            {
                System.out.println(">> id: "+product.getId()+", code: "+product.getCode()+", name: "+product.getName()+", type: "+product.getType()+", unitcode: "+product.getunitcode()+", quantity: "+product.getAvailableQuantity()+", price: "+product.getPrice()+", costprice: "+product.getCostPrice());
            }
        }
        else if(Collections.frequency(listattributes.values(),null)==listattributes.size()-3&&listattributes.get("Pagenumber")==null)
        {
            productList=listProductDAO.list(listattributes.get("Attribute"),listattributes.get("Searchtext"));
            for(Product product: productList)
            {
                System.out.println(">> id: "+product.getId()+", code: "+product.getCode()+", name: "+product.getName()+", type: "+product.getType()+", unitcode: "+product.getunitcode()+", quantity: "+product.getAvailableQuantity()+", price: "+product.getPrice()+", costprice: "+product.getCostPrice());
            }
        }

        else if(Collections.frequency(listattributes.values(),null)==0)
        {
            int pageLength=Integer.parseInt(listattributes.get("Pagelength"));
            int pageNumber=Integer.parseInt(listattributes.get("Pagenumber"));
            int offset=(pageLength*pageNumber)-pageLength;
            productList=listProductDAO.list(listattributes.get("Attribute"),listattributes.get("Searchtext"),pageLength,offset);
            for(Product product: productList)
            {
                System.out.println(">> id: "+product.getId()+", code: "+product.getCode()+", name: "+product.getName()+", type: "+product.getType()+", unitcode: "+product.getunitcode()+", quantity: "+product.getAvailableQuantity()+", price: "+product.getPrice()+", costprice: "+product.getCostPrice());
            }
        }

    }
    public void editProductService(HashMap<String,String> attributeMap) throws SQLException {
        ProductDAO productEditDAO=new ProductDAOImplementation();
        int id=Integer.parseInt(attributeMap.get("id").trim());
        if(attributeMap.get("name") != null)
        {
            productEditDAO.edit(id,"name",attributeMap.get("name"));
        }
        if(attributeMap.get("code") != null)
        {
            productEditDAO.edit(id,"code",attributeMap.get("code"));
        }
        if(attributeMap.get("unitcode") != null)
        {
            productEditDAO.edit(id,"unitcode",attributeMap.get("unitcode"));
        }
        if(attributeMap.get("type") != null)
        {
            productEditDAO.edit(id,"type",attributeMap.get("type"));
        }
        if(attributeMap.get("price") != null)
        {
            productEditDAO.edit(id,"price",attributeMap.get("price"));
        }

    }
    public void deleteProductService(String parameter)
    {
        ProductDAO deleteProductDAO=new ProductDAOImplementation();
        deleteProductDAO.delete(parameter);

    }
}
