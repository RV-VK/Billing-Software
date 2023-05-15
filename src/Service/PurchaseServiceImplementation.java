package Service;

import DAO.*;
import Entity.Purchase;
import Entity.PurchaseItem;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PurchaseServiceImplementation implements PurchaseService {

  private PurchaseDAO purchaseDAO = new PurchaseDAOImplementation();

  @Override
  public Purchase createPurchaseService(Purchase purchase)
      throws ApplicationErrorException, SQLException {
    ProductDAO productDAO = new ProductDAOImplementation();
    UnitDAO getUnitByCode = new UnitDAOImplementation();
    boolean isDividable;
    for (PurchaseItem purchaseItem : purchase.getPurchaseItemList()) {
      try {
        isDividable =
            getUnitByCode
                .findByCode(
                    (productDAO.findByCode(purchaseItem.getProduct().getCode())).getunitcode())
                .getIsDividable();
      } catch (NullPointerException e) {
        return new Purchase();
      }
      if (!isDividable && purchaseItem.getQuantity() % 1 != 0) {
        return null;
      }
    }
    Purchase createdPurchase = purchaseDAO.create(purchase);
    return createdPurchase;
  }

  @Override
  public int countPurchaseService(String parameter) throws ApplicationErrorException {
    PurchaseDAO countPurchaseDAO = new PurchaseDAOImplementation();
    String dateRegex = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";
    if (parameter != null) {
      if (!parameter.matches(dateRegex)) return -1;
    }
    return countPurchaseDAO.count(parameter);
  }

  @Override
  public List<Purchase> listPurchaseService(HashMap<String, String> listattributes)
      throws PageCountOutOfBoundsException, ApplicationErrorException {
    List<Purchase> purchaseList;
    PurchaseDAO listPurchaseDAO = new PurchaseDAOImplementation();
    if (Collections.frequency(listattributes.values(), null) == 0
        || Collections.frequency(listattributes.values(), null) == 1) {
      int pageLength = Integer.parseInt(listattributes.get("Pagelength"));
      int pageNumber = Integer.parseInt(listattributes.get("Pagenumber"));
      int offset = (pageLength * pageNumber) - pageLength;
      purchaseList =
          listPurchaseDAO.list(
              listattributes.get("Attribute"),
              listattributes.get("Searchtext"),
              pageLength,
              offset);
      return purchaseList;
    } else if (Collections.frequency(listattributes.values(), null) == listattributes.size() - 1
        && listattributes.get("Searchtext") != null) {
      purchaseList = listPurchaseDAO.list(listattributes.get("Searchtext"));
      return purchaseList;
    }
    return null;
  }

  @Override
  public int deletePurchaseService(String invoice) throws ApplicationErrorException {
    PurchaseDAO purchaseDeletDAO = new PurchaseDAOImplementation();
    return purchaseDeletDAO.delete(Integer.parseInt(invoice));
  }
}
