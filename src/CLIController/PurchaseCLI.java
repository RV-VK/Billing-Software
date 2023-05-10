package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.Product;
import Entity.Purchase;
import Entity.PurchaseItem;
import Service.PurchaseService;
import Service.PurchaseServiceImplementation;
import java.util.*;

public class PurchaseCLI {
    HashMap<String, String> listAttributesMap = new HashMap<> ();

    public void purchaseCreateCLI (String command) {
        String codeRegex = "^[a-zA-Z0-9]{2,6}$";
        String[] commandEntities = command.split (",\\s*(?=\\[)");
        if (commandEntities.length < 1) {
            System.out.println (">> Insufficient arguments to start a purchase!!!");
            System.out.println (">> Try \"purchase help\" for proper syntax");
        } else {
            String[] commandArguments = commandEntities[0].split ("\\s+");
            String purchaseDate = commandArguments[1].trim ().replace (",", "");
            int invoice;
            try {
                invoice = Integer.parseInt (commandArguments[2].trim ());
            } catch (Exception e) {
                System.out.println (">> Invoice must be a number!!!");
                System.out.println (">> Try \"purchase help\" for proper syntax!!");
                return;
            }
            List<PurchaseItem> purchaseItemList = new ArrayList<> ();
            double grandTotal = 0;
            for (int i = 1; i < commandEntities.length; i++) {
                String item = commandEntities[i].replaceAll ("[\\[\\]]", "");
                String[] itemVariables = item.split (",");
                if (itemVariables.length < 3) {
                    System.out.println (">> Please provide sufficient details for Product " + i);
                    System.out.println (">> Try \"purchase help\" for proper syntax");
                    return;
                }
                if (itemVariables.length > 3) {
                    System.out.println (">> Improper format of product details given");
                    System.out.println (">> Try \"purchase help\" for proper syntax");
                    return;
                }
                String code = itemVariables[0].trim ();
                float quantity;
                double costPrice;
                if (! code.matches (codeRegex) || code.equals ("")) {
                    System.out.println (">> Invalid Format for product code in Product " + i);
                    return;
                }
                try {
                    quantity = Float.parseFloat (itemVariables[1].trim ());
                    costPrice = Double.parseDouble (itemVariables[2].trim ());
                } catch (Exception e) {
                    System.out.println (">> Quantity (or) Costprice must be a number!! Error in Product " + i);
                    System.out.println (">> Try \"purchase help\" for proper syntax!!!");
                    return;
                }
                purchaseItemList.add (new PurchaseItem (new Product (code), quantity, costPrice));
                grandTotal += costPrice * quantity;
            }
            Purchase purchase = new Purchase (purchaseDate, invoice, purchaseItemList, grandTotal);
            PurchaseService purchaseCreateService = new PurchaseServiceImplementation ();
            try {
                purchaseCreateService.createPurchaseService (purchase);
            } catch (Exception e) {
                System.out.println (e.getMessage ());
            }
        }
    }

    public void purchaseCountCLI (String[] arguments) throws ApplicationErrorException {
        PurchaseService countPurchaseService = new PurchaseServiceImplementation ();
        if (arguments.length == 3) {
            if (arguments[2].equals ("help")) {
                System.out.println ("Count Product using the following Template\n" +
                        "> purchase count -d <date>\n" +
                        "\n" +
                        ">> count : <number>\n" +
                        "\n" +
                        "> purchase count\n" +
                        "\n" +
                        ">> count : <number>\n" +
                        "\n" +
                        "> purchase count -c <category>\n" +
                        "\n" +
                        ">> count : <number>\n");
                return;
            } else {
                System.out.println (">> Invalid command given!!!");
                System.out.println (">> Try \"purchase count help\" for proper syntax!!");
                return;
            }
        } else {
            System.out.println (">> Invalid command given!!!");
            System.out.println (">> Try \"purchase count help\" for proper syntax!!");
        }
        if (arguments.length == 2) {
            int purchaseCount = countPurchaseService.countPurchaseService (null);
            System.out.println (">> PurchaseCount " + purchaseCount);
            return;
        }
        if (arguments.length == 4) {
            if (arguments[2].equals ("-d") || arguments[2].equals ("-c")) {
                String parameter = arguments[3];
                int purchaseCount = countPurchaseService.countPurchaseService (parameter);
                if (purchaseCount > 0)
                    System.out.println (">> PurchaseCount " + purchaseCount);
                else {
                    System.out.println (">> Given Date or Category not found!!!");
                    System.out.println (">>Please Try with an existing searchtext");
                }
            } else {
                System.out.println (">> Invalid extension Given");
                System.out.println (">> Try \"purchase count help\" for proper syntax!!");
            }
        } else {
            System.out.println (">> Invalid Command Format!!!");
            System.out.println (">> Try \"purchase count help\" for proper syntax");
        }
    }

    public void purchaseListCLI (String[] arguments) throws PageCountOutOfBoundsException, ApplicationErrorException {
        listAttributesMap.put ("Pagelength", null);
        listAttributesMap.put ("Pagenumber", null);
        listAttributesMap.put ("Attribute", null);
        listAttributesMap.put ("Searchtext", null);
        PurchaseService purchaseListService = new PurchaseServiceImplementation ();
        List<Purchase> purchaseList;
        if (arguments.length == 3)
            if (arguments[2].equals ("help")) {
                System.out.println (">> List purchase with the following options\n" +
                        ">> purchase list - will list all the purchases default to maximum upto 20 purchases\n" +
                        ">> purchase list -p 10 - pageable list shows 10 purchases as default\n" +
                        ">> purchase list -p 10 3 - pageable list shows 10 purchases in 3rd page, ie., purchase from 21 to 30\n" +
                        "\n" +
                        ">> Use only the following attributes: id, date, invoice\n" +
                        ">> purchase list -s <attr>: searchtext - search the purchase with the given search text in all the given attribute\n" +
                        ">> purchase list -s <attr>: searchtext -p 10 6 - pageable list shows 10 purchases in 6th page with the given search text in the given attribute\n" +
                        "\n" +
                        "> purchase list -s <date> : <23-03-2023> -p 5 2 \n" +
                        "> purchase list -s <invoice> : <785263>");
                return;
            }

        if (arguments.length == 2) {
            listAttributesMap.put ("Pagelength", "20");
            listAttributesMap.put ("Pagenumber", "1");
            purchaseList = purchaseListService.listPurchaseService (listAttributesMap);
            for (Purchase purchase : purchaseList) {
                System.out.print ("id: " + purchase.getId () + ", date: " + purchase.getDate () + ", invoice: " + purchase.getInvoice () + ", ");
                System.out.print ("[");
                for (PurchaseItem purchaseItem : purchase.getPurchaseItemList ()) {
                    System.out.print ("[name: " + purchaseItem.getProduct ().getName () + ", quantity: " + purchaseItem.getQuantity () + ", price: " + purchaseItem.getUnitPurchasePrice () + "], ");
                }
                System.out.print ("]");
                System.out.println ();
            }
        } else if (arguments.length == 4) {
            int pageLength = 0;
            if (arguments[2].equals ("-p")) {
                try {
                    pageLength = Integer.parseInt (arguments[3]);
                } catch (Exception e) {
                    System.out.println (">> Invalid page Size input");
                    System.out.println (">> Try \"purchase list help\" for proper syntax");
                }
                listAttributesMap.put ("Pagelength", String.valueOf (pageLength));
                listAttributesMap.put ("Pagenumber", String.valueOf (1));
                purchaseList = purchaseListService.listPurchaseService (listAttributesMap);
                for (Purchase purchase : purchaseList) {
                    System.out.print ("id: " + purchase.getId () + ", date: " + purchase.getDate () + ", invoice: " + purchase.getInvoice () + ", ");
                    System.out.print ("[");
                    for (PurchaseItem purchaseItem : purchase.getPurchaseItemList ()) {
                        System.out.print ("[name: " + purchaseItem.getProduct ().getName () + ", quantity: " + purchaseItem.getQuantity () + ", price: " + purchaseItem.getUnitPurchasePrice () + "], ");
                    }
                    System.out.print ("]");
                    System.out.println ();
                }
            } else if (arguments[2].equals ("-s")) {
                String searchText = arguments[3].trim ();
                listAttributesMap.put ("Searchtext", searchText);
                purchaseList = purchaseListService.listPurchaseService (listAttributesMap);
                if (purchaseList.size () == 0) {
                    System.out.println (">> Given SearchText does not exist!!!");
                }
                for (Purchase purchase : purchaseList) {
                    System.out.print ("id: " + purchase.getId () + ", date: " + purchase.getDate () + ", invoice: " + purchase.getInvoice () + ", ");
                    System.out.print ("[");
                    for (PurchaseItem purchaseItem : purchase.getPurchaseItemList ()) {
                        System.out.print ("[name: " + purchaseItem.getProduct ().getName () + ", quantity: " + purchaseItem.getQuantity () + ", price: " + purchaseItem.getUnitPurchasePrice () + "], ");
                    }
                    System.out.print ("]");
                    System.out.println ();
                }
            } else {
                System.out.println (">> Invalid Extension given");
                System.out.println (">> Try \"purchase list help\" for proper syntax");
            }

        } else if (arguments.length == 5) {
            int pageLength = 0;
            int pageNumber = 0;
            if (arguments[2].equals ("-p")) {
                try {
                    pageLength = Integer.parseInt (arguments[3]);
                    pageNumber = Integer.parseInt (arguments[4]);
                } catch (Exception e) {
                    System.out.println (">> Invalid page Size (or) page Number input");
                    System.out.println (">> Try \"purchase list help\" for proper syntax");
                    return;
                }
                listAttributesMap.put ("Pagelength", String.valueOf (pageLength));
                listAttributesMap.put ("Pagenumber", String.valueOf (pageNumber));
                try {
                    purchaseList = purchaseListService.listPurchaseService (listAttributesMap);
                } catch (Exception e) {
                    System.out.println (e.getMessage ());
                    return;
                }
                for (Purchase purchase : purchaseList) {
                    System.out.print ("id: " + purchase.getId () + ", date: " + purchase.getDate () + ", invoice: " + purchase.getInvoice () + ", ");
                    System.out.print ("[");
                    for (PurchaseItem purchaseItem : purchase.getPurchaseItemList ()) {
                        System.out.print ("[name: " + purchaseItem.getProduct ().getName () + ", quantity: " + purchaseItem.getQuantity () + ", price: " + purchaseItem.getUnitPurchasePrice () + "], ");
                    }
                    System.out.print ("]");
                    System.out.println ();
                }
            } else if (arguments[2].equals ("-s")) {
                List<String> purchaseAttributes = Arrays.asList ("id", "date", "invoice");
                String attribute = arguments[3];
                attribute = attribute.replace (":", "");
                String searchText = arguments[4];
                if (purchaseAttributes.contains (attribute)) {
                    listAttributesMap.put ("Attribute", attribute);
                    listAttributesMap.put ("Searchtext", searchText);
                    listAttributesMap.put ("Pagelength", "20");
                    listAttributesMap.put ("Pagenumber", String.valueOf (1));
                    purchaseList = purchaseListService.listPurchaseService (listAttributesMap);
                    if (purchaseList == null) {
                        System.out.println (">>Given SearchText does not exist!!!");
                        return;
                    }
                    for (Purchase purchase : purchaseList) {
                        System.out.print ("id: " + purchase.getId () + ", date: " + purchase.getDate () + ", invoice: " + purchase.getInvoice () + ", ");
                        System.out.print ("[");
                        for (PurchaseItem purchaseItem : purchase.getPurchaseItemList ()) {
                            System.out.print ("[name: " + purchaseItem.getProduct ().getName () + ", quantity: " + purchaseItem.getQuantity () + ", price: " + purchaseItem.getUnitPurchasePrice () + "], ");
                        }
                        System.out.print ("]");
                        System.out.println ();
                    }
                } else {
                    System.out.println ("Given attribute is not a searchable attribute!!");
                    System.out.println ("Try \"purchase list help\" for proper syntax");
                }

            } else {
                System.out.println (">> Invalid Extension given");
                System.out.println (">> Try \"purchase list help\" for proper syntax");
            }
        } else if (arguments.length == 7) {
            if (arguments[2].equals ("-s")) {
                List<String> purchaseAttributes = Arrays.asList ("id", "date", "invoice");
                String attribute = arguments[3];
                attribute = attribute.replace (":", "");
                String searchText = arguments[4];
                listAttributesMap.put ("Attribute", attribute);
                listAttributesMap.put ("Searchtext", searchText);
                if (purchaseAttributes.contains (attribute)) {
                    int pageLength;
                    if (arguments[5].equals ("-p")) {
                        try {
                            pageLength = Integer.parseInt (arguments[6]);
                        } catch (Exception e) {
                            System.out.println (">> Invalid page Size input");
                            System.out.println (">> Try \"purchase list help\" for proper syntax");
                            return;
                        }
                        listAttributesMap.put ("Pagelength", String.valueOf (pageLength));
                        listAttributesMap.put ("Pagenumber", "1");
                        purchaseList = purchaseListService.listPurchaseService (listAttributesMap);
                        if (purchaseList == null) {
                            System.out.println (">>Given SearchText does not exists");
                            return;
                        }
                        for (Purchase purchase : purchaseList) {
                            System.out.print ("id: " + purchase.getId () + ", date: " + purchase.getDate () + ", invoice: " + purchase.getInvoice () + ", ");
                            System.out.print ("[");
                            for (PurchaseItem purchaseItem : purchase.getPurchaseItemList ()) {
                                System.out.print ("[name: " + purchaseItem.getProduct ().getName () + ", quantity: " + purchaseItem.getQuantity () + ", price: " + purchaseItem.getUnitPurchasePrice () + "], ");
                            }
                            System.out.print ("]");
                            System.out.println ();
                        }
                    } else {
                        System.out.println (">> Invalid Command Extension format !!!");
                        System.out.println ("Try \"purchase list help\" for proper syntax");
                    }
                } else {
                    System.out.println ("Given attribute is not a searchable attribute!!");
                    System.out.println ("Try \"purchase list help\" for proper syntax");
                }

            } else {
                System.out.println (">> Invalid Extension given");
                System.out.println (">> Try \"purchase list help\" for proper syntax");
            }
        } else if (arguments.length == 8) {
            if (arguments[2].equals ("-s")) {
                List<String> purchaseAttributes = Arrays.asList ("id", "date", "invoice");
                String attribute = arguments[3];
                attribute = attribute.replace (":", "");
                String searchText = arguments[4];
                int pageLength = 0;
                int pageNumber = 0;
                listAttributesMap.put ("Attribute", attribute);
                listAttributesMap.put ("Searchtext", searchText);
                if (purchaseAttributes.contains (attribute)) {
                    if (arguments[5].equals ("-p")) {
                        try {
                            pageLength = Integer.parseInt (arguments[6]);
                            pageNumber = Integer.parseInt (arguments[7]);
                        } catch (Exception e) {
                            System.out.println (">> Invalid page Size (or) page Number input");
                            System.out.println (">> Try \"purchase list help\" for proper syntax");
                            return;
                        }
                        listAttributesMap.put ("Pagelength", String.valueOf (pageLength));
                        listAttributesMap.put ("Pagenumber", String.valueOf (pageNumber));
                        purchaseList = purchaseListService.listPurchaseService (listAttributesMap);
                        if (purchaseList == null) {
                            System.out.println (">>Given SearchText does not exist!!!");
                            return;
                        }
                        for (Purchase purchase : purchaseList) {
                            System.out.print ("id: " + purchase.getId () + ", date: " + purchase.getDate () + ", invoice: " + purchase.getInvoice () + ", ");
                            System.out.print ("[");
                            for (PurchaseItem purchaseItem : purchase.getPurchaseItemList ()) {
                                System.out.print ("[name: " + purchaseItem.getProduct ().getName () + ", quantity: " + purchaseItem.getQuantity () + ", price: " + purchaseItem.getUnitPurchasePrice () + "], ");
                            }
                            System.out.print ("]");
                            System.out.println ();
                        }
                    } else {
                        System.out.println ("Invalid Extension Given!!!");
                        System.out.println ("Try \"purchase list help\" for proper syntax");
                    }
                } else {
                    System.out.println ("Given attribute is not a searchable attribute!!");
                    System.out.println ("Try \"purchase list help\" for proper syntax");
                }
            } else {
                System.out.println (">> Invalid Extension given");
                System.out.println (">> Try \"purchase list help\" for proper syntax");
            }

        } else {
            System.out.println ("Invalid command format!!!");
            System.out.println (">> Try \"purchase list help\" for proper syntax");
        }
    }

    public void purchaseDeleteCLI (String[] arguments) throws ApplicationErrorException {
        Scanner scanner = new Scanner (System.in);
        PurchaseService purchaseDeleteService = new PurchaseServiceImplementation ();
        String numberRegex = "^[0-9]{1,10}$";
        if (arguments.length == 3) {
            if (arguments[2].equals ("help")) {
                System.out.println (">> Delete purchase using following command \n" +
                        "\n" +
                        ">> purchase delete <invoice>\n" +
                        "\t\tinvoice - numeric, mandatory\n" +
                        "\t\t");
                return;
            } else if (arguments[2].matches (numberRegex)) {
                System.out.println (">> Are you sure want to delete the User y/n ? : ");
                String prompt = scanner.nextLine ();
                if (prompt.equals ("y")) {
                    int resultCode = purchaseDeleteService.deletePurchaseService (arguments[2]);
                    if (resultCode == 1) {
                        System.out.println (">> Purchase Deleted Successfully!!");
                    } else if (resultCode == - 1) {
                        System.out.println (">> Purchase Deletion Failed!!!");
                        System.out.println (">> Please check the invoice you have entered!!!");
                        System.out.println (">> Try \"purhase delete help\" for proper syntax");
                    }
                } else if (prompt.equals ("n")) {
                    System.out.println (">> Delete operation cancelled!!!");
                } else {
                    System.out.println (">> >> Invalid delete prompt!!! Please select between y/n");
                }
            } else {
                System.out.println (">> Invalid format for invoice!!");
                System.out.println ("Try \"purchase delete help\" for proper syntax!!!");
            }
        }
    }
}
