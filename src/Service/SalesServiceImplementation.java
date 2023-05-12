package Service;
import DAO.*;
import Entity.Sales;
import Entity.SalesItem;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
public class SalesServiceImplementation implements SalesService {
  @Override
  public int createSalesService(Sales sales) throws ApplicationErrorException, SQLException {
    ProductDAO unitCheckDAO = new ProductDAOImplementation();
    boolean isDividable;
    ProductDAO getProductByCode = new ProductDAOImplementation();
    UnitDAO getUnitNyCode=new UnitDAOImplementation ();
    int i = 1;
    for (SalesItem salesItem : sales.getSalesItemList()) {
      try{
       isDividable = getUnitNyCode.findByCode ((getProductByCode.findByCode (salesItem.getProduct ().getCode ())).getunitcode ()).getIsDividable ();
     }catch ( NullPointerException e )
      {
        return -1;
      }
      if (!isDividable && salesItem.getQuantity() % 1 == 0) {
        return i + 1;
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
    String dateRegex="([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";
    if(parameter!=null)
    {
      if(!parameter.matches (dateRegex))
      {
        return-1;
      }
    }
    return salesCountDAO.count(parameter);
  }

  @Override
  public List<Sales> listSalesService(HashMap<String, String> listAttributes)
      throws ApplicationErrorException, PageCountOutOfBoundsException {
    List<Sales> salesList;
    SalesDAO listSalesDAO = new SalesDAOImplementation();
    if (Collections.frequency(listAttributes.values(), null) == 0) {
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
