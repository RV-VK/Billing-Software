package Service;

import Entity.Store;

import java.util.HashMap;

public interface StoreService {
    Store createStoreService(Store store);
    int editStoreService(HashMap<String,String> attributeMap);
    int deleteStoreService(String adminPassword);

}
