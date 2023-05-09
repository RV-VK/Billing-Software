package DAO;

import CLIController.SalesCLI;
import Entity.Sales;

import java.util.List;

public class SalesDAOImplementation implements SalesDAO {
    @Override
    public Sales create(Sales sales) {
        return null;
    }

    @Override
    public int count(String parameter) {
        return 0;
    }

    @Override
    public List list(int pageLength, int pageNumber) {
        return null;
    }

    @Override
    public List list(String attribute, String searchText, int pageLength, int pageNumber) {
        return null;
    }

    @Override
    public List list(String searchText) {
        return null;
    }

    @Override
    public int delete(int id) {
        return 0;
    }
}
