package Service;

import DAO.*;
import Entity.Purchase;
import Entity.PurchaseItem;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PurchaseServiceImplementation implements PurchaseService {
  @Override
  public int createPurchaseService(Purchase purchase)
      throws ApplicationErrorException, SQLException {
    ProductDAO getProductByCode = new ProductDAOImplementation();
    UnitDAO getUnitNyCode=new UnitDAOImplementation ();
    boolean isDividable;
    int i = 1;
    for (PurchaseItem purchaseItem : purchase.getPurchaseItemList()) {
      try{
      isDividable = getUnitNyCode.findByCode ((getProductByCode.findByCode (purchaseItem.getProduct ().getCode ())).getunitcode ()).getIsDividable ();
        }catch ( NullPointerException e )
      {
        return -1;
      }
      if (!isDividable  && purchaseItem.getQuantity() % 1 != 0) {
        return i + 1;
      }
    }
    PurchaseDAO purchaseCreateDAO = new PurchaseDAOImplementation();
    Purchase createdPurchase = purchaseCreateDAO.create(purchase);
    if (createdPurchase != null) {
      System.out.println(
          "**********************************************************************************");
      System.out.println(
          "\t\tPURCHASE BILL "
              + createdPurchase.getId()
              + "\t\tINVOICE NO "
              + createdPurchase.getInvoice());
      System.out.println(
          "**********************************************************************************");
      System.out.println("SNO\t\tPRODUCT NAME\t\t\tQTY\t\tPRICE\t\tTOTAL");
      System.out.println(
          "----------------------------------------------------------------------------------");
      for (int j = 0; j < createdPurchase.getPurchaseItemList().size(); j++) {
        System.out.printf(
            "%d\t\t%-15s\t\t\t%.1f\t\t%.2f\t\t%.2f%n",
            j + 1,
            createdPurchase.getPurchaseItemList().get(j).getProduct().getName(),
            createdPurchase.getPurchaseItemList().get(j).getQuantity(),
            createdPurchase.getPurchaseItemList().get(j).getUnitPurchasePrice(),
            (createdPurchase.getPurchaseItemList().get(j).getQuantity()
                * createdPurchase.getPurchaseItemList().get(j).getUnitPurchasePrice()));
      }
      System.out.println(
          "----------------------------------------------------------------------------------");
      System.out.printf("GRAND TOTAL\t\t\t\t\t\t\t\t\t\t\t%.2f%n", createdPurchase.getGrandTotal());
      System.out.println(
          "----------------------------------------------------------------------------------");

      return 1;
    } else {
      return -2;
    }
  }

  @Override
  public int countPurchaseService(String parameter) throws ApplicationErrorException {
    PurchaseDAO countPurchaseDAO = new PurchaseDAOImplementation();
    String dateRegex="([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";
    if(parameter!=null)
    {
      if(!parameter.matches (dateRegex))
        return -1;
    }
    return countPurchaseDAO.count(parameter);
  }

  @Override
  public List<Purchase> listPurchaseService(HashMap<String, String> listattributes)
      throws PageCountOutOfBoundsException, ApplicationErrorException {
    List<Purchase> purchaseList;
    PurchaseDAO listPurchaseDAO = new PurchaseDAOImplementation();
      if (Collections.frequency(listattributes.values(), null) == 0) {
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
