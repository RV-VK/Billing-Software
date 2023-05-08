package DAO;

import Entity.Purchase;

import java.util.List;

public interface PurchaseDAO {
    Purchase create(Purchase purchase) throws ApplicationErrorException;
    int count(String paramter);
    List list(int pageLength,int pageNumber);
    List list(String attribute,String searchText,int pageLength,int pageNumber);
    List list(String searchText);
    int delete(int invoice);

}
