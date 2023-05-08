package Service;

import Entity.Purchase;

import java.util.HashMap;
import java.util.List;

public interface PurchaseService {
    int createPurchaseService(Purchase purchase);
    int countPurchaseService(String parameter);
    List<Purchase> listPurchaseService(HashMap<String,String> listattributes);
    int deletePurchaseService(String invoice);


}
