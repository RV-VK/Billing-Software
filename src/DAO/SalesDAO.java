package DAO;

import Entity.Sales;

import java.util.List;

public interface SalesDAO {
    Sales create(Sales sales);
    int count(String parameter);
    List list(int pageLength, int pageNumber);
    List list(String attribute,String searchText,int pageLength,int pageNumber);
    List list(String searchText);
    int delete(int id);
}
