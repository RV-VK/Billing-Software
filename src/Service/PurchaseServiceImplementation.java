package Service;

import DAO.*;
import Entity.Purchase;
import Entity.PurchaseItem;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PurchaseServiceImplementation implements PurchaseService {
    @Override
    public int createPurchaseService (Purchase purchase) throws ApplicationErrorException, SQLException {
        ProductDAO unitCheckDAO = new ProductDAOImplementation ();
        int i = 1;
        for (PurchaseItem purchaseItem : purchase.getPurchaseItemList ()) {
            int isDividable = unitCheckDAO.checkIsdividable (purchaseItem.getProduct ().getCode ());
            if (isDividable == 0 && purchaseItem.getQuantity () % 1 != 0) {
                return i + 1;
            } else if (isDividable == - 1) {
                return isDividable;
            }
        }
        PurchaseDAO purchaseCreateDAO = new PurchaseDAOImplementation ();
        Purchase createdPurchase = purchaseCreateDAO.create (purchase);
        if (createdPurchase != null) {
            System.out.println ("**********************************************************************************");
            System.out.println ("\t\tPURCHASE BILL " + createdPurchase.getId () + "\t\tINVOICE NO " + createdPurchase.getInvoice ());
            System.out.println ("**********************************************************************************");
            System.out.println ("SNO\t\tPRODUCT NAME\t\t\tQTY\t\tPRICE\t\tTOTAL");
            System.out.println ("----------------------------------------------------------------------------------");
            for (int j = 0; j < createdPurchase.getPurchaseItemList ().size (); j++) {
                System.out.printf ("%d\t\t%-15s\t\t\t%.1f\t\t%.2f\t\t%.2f%n", j + 1, createdPurchase.getPurchaseItemList ().get (j).getProduct ().getName (), createdPurchase.getPurchaseItemList ().get (j).getQuantity (), createdPurchase.getPurchaseItemList ().get (j).getUnitPurchasePrice (), (createdPurchase.getPurchaseItemList ().get (j).getQuantity () * createdPurchase.getPurchaseItemList ().get (j).getUnitPurchasePrice ()));
            }
            System.out.println ("----------------------------------------------------------------------------------");
            System.out.printf ("GRAND TOTAL\t\t\t\t\t\t\t\t\t\t\t%.2f%n", createdPurchase.getGrandTotal ());
            System.out.println ("----------------------------------------------------------------------------------");

            return 1;
        } else {
            return - 2;
        }
    }

    @Override
    public int countPurchaseService (String parameter) throws ApplicationErrorException {
        PurchaseDAO countPurchaseDAO = new PurchaseDAOImplementation ();
        return countPurchaseDAO.count (parameter);
    }

    @Override
    public List<Purchase> listPurchaseService (HashMap<String, String> listattributes) throws PageCountOutOfBoundsException, ApplicationErrorException {
        List<Purchase> purchaseList;
        PurchaseDAO listPurchaseDAO = new PurchaseDAOImplementation ();
        if (Collections.frequency (listattributes.values (), null) == listattributes.size () - 2 && listattributes.get ("Pagelength") != null && listattributes.get ("Pagenumber") != null) {
            purchaseList = listPurchaseDAO.list (Integer.parseInt (listattributes.get ("Pagelength")), Integer.parseInt (listattributes.get ("Pagenumber")));
            return purchaseList;
        } else if (Collections.frequency (listattributes.values (), null) == 0) {
            int pageLength = Integer.parseInt (listattributes.get ("Pagelength"));
            int pageNumber = Integer.parseInt (listattributes.get ("Pagenumber"));
            int offset = (pageLength * pageNumber) - pageLength;
            purchaseList = listPurchaseDAO.list (listattributes.get ("Attribute"), listattributes.get ("Searchtext"), pageLength, offset);
            return purchaseList;
        } else if (Collections.frequency (listattributes.values (), null) == listattributes.size () - 1 && listattributes.get ("Searchtext") != null) {
            purchaseList = listPurchaseDAO.list (listattributes.get ("Searchtext"));
            return purchaseList;
        }
        return null;
    }

    @Override
    public int deletePurchaseService (String invoice) throws ApplicationErrorException {
        PurchaseDAO purchaseDeletDAO = new PurchaseDAOImplementation ();
        return purchaseDeletDAO.delete (Integer.parseInt (invoice));

    }
}
