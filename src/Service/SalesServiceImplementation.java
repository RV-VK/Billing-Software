package Service;
import DAO.*;
import Entity.Sales;
import Entity.SalesItem;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
public class SalesServiceImplementation implements SalesService {
  @Override
  public int createSalesService(Sales sales) throws ApplicationErrorException {
    ProductDAO unitCheckDAO = new ProductDAOImplementation();
    int i = 1;
    for (SalesItem salesItem : sales.getSalesItemList()) {
      int isDividable = unitCheckDAO.checkIsdividable(salesItem.getProduct().getCode());
      if (isDividable == 0 && salesItem.getQuantity() % 1 == 0) {
        return i + 1;
      } else if (isDividable == -1) {
        return isDividable;
      }
    }
    SalesDAO salesCreateDAO = new SalesDAOImplementation();
    Sales createdSale = salesCreateDAO.create(sales);
    if (createdSale != null) {
      return 1;
    } else {
      return -2;
    }
  }

  @Override
  public int countSalesService(String parameter) throws ApplicationErrorException {
    SalesDAO salesCountDAO = new SalesDAOImplementation();
    return salesCountDAO.count(parameter);
  }

  @Override
  public List<Sales> listSalesService(HashMap<String, String> listAttributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException {
    List<Sales> salesList;
    SalesDAO listSalesDAO = new SalesDAOImplementation();
    if (Collections.frequency(listAttributes.values(), null) == listAttributes.size() - 2
        && listAttributes.get("Pagelength") != null
        && listAttributes.get("Pagenumber") != null) {
      salesList =
          listSalesDAO.list(
              Integer.parseInt(listAttributes.get("Pagelength")),
              Integer.parseInt(listAttributes.get("Pagenumber")));
      return salesList;
    } else if (Collections.frequency(listAttributes.values(), null) == 0) {
      int pageLength = Integer.parseInt(listAttributes.get("Pagelength"));
      int pageNumber = Integer.parseInt(listAttributes.get("Pagenumber"));
      int offset = (pageLength * pageNumber) - pageLength;
      salesList =
          listSalesDAO.list(
              listAttributes.get("Attribute"),
              listAttributes.get("Searchtext"),
              pageLength,
              offset);
      return salesList;
    } else if (Collections.frequency(listAttributes.values(), null) == listAttributes.size() - 1
        && listAttributes.get("Searchtext") != null) {
      salesList = listSalesDAO.list(listAttributes.get("Searchtext"));
      return salesList;
    }
    return null;
  }

  @Override
  public int deleteSalesService(String id) throws ApplicationErrorException {
    SalesDAO salesDeleteDAO = new SalesDAOImplementation();
    return salesDeleteDAO.delete(Integer.parseInt(id));
  }
}
