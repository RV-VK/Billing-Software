package Service;

import DAO.ApplicationErrorException;
import DAO.StoreDAO;
import DAO.StoreDAOImplementation;
import Entity.Store;

import java.sql.SQLException;
import java.util.HashMap;

public class StoreServiceImplementation implements StoreService{
    @Override
    public int createStoreService(Store store) throws SQLException, ApplicationErrorException {
        StoreDAO storeCreateDAO=new StoreDAOImplementation();
        Store createdStore=storeCreateDAO.create(store);
        if(createdStore!=null)
        {
            return 1;
        }
        else {
            return -1;
        }

    }

    @Override
    public int editStoreService(HashMap<String, String> attributeMap) throws SQLException, ApplicationErrorException {
        StoreDAO storeEditDAO=new StoreDAOImplementation();
        String numberRegex="^[0-9]*$";
        String nameRegex="^[a-zA-Z]{3,30}$";
        int id;
        try {
            id = Integer.parseInt(attributeMap.get("id").trim());
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
        if (attributeMap.get("name") != null && !attributeMap.get("name").matches(nameRegex) || attributeMap.get("phonenumber") != null && !attributeMap.get("phonenumber").matches(numberRegex) ||  attributeMap.get("gstnumber") != null && !attributeMap.get("gstnumber").matches(numberRegex) || attributeMap.get("address") != null && !attributeMap.get("address").matches(nameRegex)) {
            return 0;
        }
        int status;
        if(attributeMap.get("name")!=null)
        {
            status=storeEditDAO.edit("name",attributeMap.get("name"));
            if(status==-1)
            {
                return -1;
            }
        }
        if(attributeMap.get("address") != null)
        {
            storeEditDAO.edit("address",attributeMap.get("address"));
        }
        if(attributeMap.get("phonenumber") != null)
        {
            storeEditDAO.edit("phonenumber",attributeMap.get("phonenumber"));
        }
        if(attributeMap.get("gstnumber") != null)
        {
            storeEditDAO.edit("gstnumber",attributeMap.get("gstnumber"));
        }
        return 1;
    }

    @Override
    public int deleteStoreService(String adminPassword) throws ApplicationErrorException {
        StoreDAO storeDeleteDAO=new StoreDAOImplementation();
        return storeDeleteDAO.delete(adminPassword);
    }
}
