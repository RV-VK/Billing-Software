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
  private String purchaseDate;
  private int invoice;
  private double grandTotal;
  private List<PurchaseItem> purchaseItemList = new ArrayList<>();
  private String code;
  private float quantity;
  private double costPrice;
  private int pageLength;
  private int pageNumber;
  private String attribute;
  private String searchText;
  private Purchase createdPurchase;
  private List<String> purchaseAttributes = Arrays.asList("id", "date", "invoice");
  private PurchaseService purchaseService = new PurchaseServiceImplementation();
  private List<Purchase> purchaseList;
  private Scanner scanner = new Scanner(System.in);
  private HashMap<String, String> listAttributesMap = new HashMap<>();

  public void purchaseCreateCLI(String command) {
    String productCodeRegex = "^[a-zA-Z0-9]{2,6}$";
    String[] commandEntities = command.split(",\\s*(?=\\[)");
    if (commandEntities.length < 1) {
      System.out.println(">> Insufficient arguments to start a purchase!!!");
      System.out.println(">> Try \"purchase help\" for proper syntax");
    } else {
      String[] commandArguments = commandEntities[0].split("\\s+");
      purchaseDate = commandArguments[1].trim().replace(",", "");
      try {
        invoice = Integer.parseInt(commandArguments[2].trim());
      } catch (Exception e) {
        System.out.println(">> Invoice must be a number!!!");
        System.out.println(">> Try \"purchase help\" for proper syntax!!");
        return;
      }
      for (int i = 1; i < commandEntities.length; i++) {
        String item = commandEntities[i].replaceAll("[\\[\\]]", "");
        String[] itemVariables = item.split(",");
        if (itemVariables.length < 3) {
          System.out.println(">> Please provide sufficient details for Product " + i);
          System.out.println(">> Try \"purchase help\" for proper syntax");
          return;
        }
        if (itemVariables.length > 3) {
          System.out.println(">> Improper format of product details given");
          System.out.println(">> Try \"purchase help\" for proper syntax");
          return;
        }
        code = itemVariables[0].trim();
        if (!code.matches(productCodeRegex)) {
          System.out.println("Invalid format for product code!!");
          System.out.println("Try \"purchase help\" for proper syntax");
        }
        try {
          quantity = Float.parseFloat(itemVariables[1].trim());
          costPrice = Double.parseDouble(itemVariables[2].trim());
        } catch (Exception e) {
          System.out.println(">> Quantity (or) Costprice must be a number!! Error in Product " + i);
          System.out.println(">> Try \"purchase help\" for proper syntax!!!");
          return;
        }
        purchaseItemList.add(new PurchaseItem(new Product(code), quantity, costPrice));
        grandTotal += costPrice * quantity;
      }
      Purchase purchase = new Purchase(purchaseDate, invoice, purchaseItemList, grandTotal);
      try {
        createdPurchase = purchaseService.createPurchaseService(purchase);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      if(createdPurchase.getDate()==null)
        System.out.println(">> The product code you have entered do not exist!! Please check the product codes");
      else if (createdPurchase != null) {
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
        System.out.printf(
            "GRAND TOTAL\t\t\t\t\t\t\t\t\t\t\t%.2f%n", createdPurchase.getGrandTotal());
        System.out.println(
            "----------------------------------------------------------------------------------");
      }
      else{
        System.out.println(">> Not a dividable Entity!!Please check the quantity you have entered");
      }
    }
  }

  public void purchaseCountCLI(List<String> arguments) throws ApplicationErrorException {
    int purchaseCount;
    if (arguments.size() == 3) {
      if (arguments.get(2).equals("help")) {
        System.out.println(
            "Count Purchase using the following Template\n"
                + "> purchase count -d <date>\n"
                + "\n"
                + ">> count : <number>\n"
                + "\n"
                + "> purchase count\n"
                + "\n"
                + ">> count : <number>\n"
                + "\n"
                + "> purchase count -c <category>\n"
                + "\n"
                + ">> count : <number>\n");
        return;
      } else {
        System.out.println(">> Invalid command given!!!");
        System.out.println(">> Try \"purchase count help\" for proper syntax!!");
        return;
      }
    }
    if (arguments.size() == 2) {
      purchaseCount = purchaseService.countPurchaseService(null);
      System.out.println(">> PurchaseCount " + purchaseCount);
      return;
    }
    if (arguments.size() == 4) {
      if (arguments.get(2).equals("-d")) {
        String parameter = arguments.get(3);
        purchaseCount = purchaseService.countPurchaseService(parameter);
        if (purchaseCount > 0) System.out.println(">> PurchaseCount " + purchaseCount);
        else {
          System.out.println(">> Given Date or Category not found!!!");
          System.out.println(">>Please Try with an existing searchtext");
        }
      } else {
        System.out.println(">> Invalid extension Given");
        System.out.println(">> Try \"purchase count help\" for proper syntax!!");
      }
    } else {
      System.out.println(">> Invalid Command Format!!!");
      System.out.println(">> Try \"purchase count help\" for proper syntax");
    }
  }

  public void purchaseListCLI(List<String> arguments)
      throws PageCountOutOfBoundsException, ApplicationErrorException {
    listAttributesMap.put("Pagelength", null);
    listAttributesMap.put("Pagenumber", null);
    listAttributesMap.put("Attribute", null);
    listAttributesMap.put("Searchtext", null);
    if (arguments.size() == 3)
      if (arguments.get(2).equals("help")) {
        System.out.println(
            ">> List purchase with the following options\n"
                + ">> purchase list - will list all the purchases default to maximum upto 20 purchases\n"
                + ">> purchase list -p 10 - pageable list shows 10 purchases as default\n"
                + ">> purchase list -p 10 3 - pageable list shows 10 purchases in 3rd page, ie., purchase from 21 to 30\n"
                + "\n"
                + ">> Use only the following attributes: id, date, invoice\n"
                + ">> purchase list -s <attr>: searchtext - search the purchase with the given search text in all the given attribute\n"
                + ">> purchase list -s <attr>: searchtext -p 10 6 - pageable list shows 10 purchases in 6th page with the given search text in the given attribute\n"
                + "\n"
                + "> purchase list -s <date> : <23-03-2023> -p 5 2 \n"
                + "> purchase list -s <invoice> : <785263>");
        return;
      }
    if (arguments.size() == 2) {
      listAttributesMap.put("Pagelength", "20");
      listAttributesMap.put("Pagenumber", "1");
      listAttributesMap.put("Attribute", "id");
      listHelper(listAttributesMap);
    } else if (arguments.size() == 4) {
      pageLength = 0;
      if (arguments.get(2).equals("-p")) {
        try {
          pageLength = Integer.parseInt(arguments.get(3));
        } catch (Exception e) {
          System.out.println(">> Invalid page Size input");
          System.out.println(">> Try \"purchase list help\" for proper syntax");
        }
        listAttributesMap.put("Pagelength", String.valueOf(pageLength));
        listAttributesMap.put("Pagenumber", String.valueOf(1));
        listAttributesMap.put("Attribute", "id");
        listHelper(listAttributesMap);
      } else if (arguments.get(2).equals("-s")) {
        String searchText = arguments.get(3).trim();
        listAttributesMap.put("Searchtext", searchText);
        listHelper(listAttributesMap);
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"purchase list help\" for proper syntax");
      }
    } else if (arguments.size() == 5) {
      if (arguments.get(2).equals("-p")) {
        try {
          pageLength = Integer.parseInt(arguments.get(3));
          pageNumber = Integer.parseInt(arguments.get(4));
        } catch (Exception e) {
          System.out.println(">> Invalid page Size (or) page Number input");
          System.out.println(">> Try \"purchase list help\" for proper syntax");
          return;
        }
        listAttributesMap.put("Pagelength", String.valueOf(pageLength));
        listAttributesMap.put("Pagenumber", String.valueOf(pageNumber));
        listAttributesMap.put("Attribute", "id");
        listHelper(listAttributesMap);
      } else if (arguments.get(2).equals("-s")) {
        attribute = arguments.get(3);
        attribute = attribute.replace(":", "");
        searchText = arguments.get(4);
        if (purchaseAttributes.contains(attribute)) {
          listAttributesMap.put("Attribute", attribute);
          listAttributesMap.put("Searchtext", searchText);
          listAttributesMap.put("Pagelength", "20");
          listAttributesMap.put("Pagenumber", String.valueOf(1));
          purchaseList = purchaseService.listPurchaseService(listAttributesMap);
          listHelper(listAttributesMap);
        } else {
          System.out.println("Given attribute is not a searchable attribute!!");
          System.out.println("Try \"purchase list help\" for proper syntax");
        }
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"purchase list help\" for proper syntax");
      }
    } else if (arguments.size() == 7) {
      if (arguments.get(2).equals("-s")) {
        attribute = arguments.get(3);
        attribute = attribute.replace(":", "");
        searchText = arguments.get(4);
        listAttributesMap.put("Attribute", attribute);
        listAttributesMap.put("Searchtext", searchText);
        if (purchaseAttributes.contains(attribute)) {
          if (arguments.get(5).equals("-p")) {
            try {
              pageLength = Integer.parseInt(arguments.get(6));
            } catch (Exception e) {
              System.out.println(">> Invalid page Size input");
              System.out.println(">> Try \"purchase list help\" for proper syntax");
              return;
            }
            listAttributesMap.put("Pagelength", String.valueOf(pageLength));
            listAttributesMap.put("Pagenumber", "1");
            listHelper(listAttributesMap);
          } else {
            System.out.println(">> Invalid Command Extension format !!!");
            System.out.println("Try \"purchase list help\" for proper syntax");
          }
        } else {
          System.out.println("Given attribute is not a searchable attribute!!");
          System.out.println("Try \"purchase list help\" for proper syntax");
        }
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"purchase list help\" for proper syntax");
      }
    } else if (arguments.size() == 8) {
      if (arguments.get(2).equals("-s")) {
        attribute = arguments.get(3);
        attribute = attribute.replace(":", "");
        searchText = arguments.get(4);
        listAttributesMap.put("Attribute", attribute);
        listAttributesMap.put("Searchtext", searchText);
        if (purchaseAttributes.contains(attribute)) {
          if (arguments.get(5).equals("-p")) {
            try {
              pageLength = Integer.parseInt(arguments.get(6));
              pageNumber = Integer.parseInt(arguments.get(7));
            } catch (Exception e) {
              System.out.println(">> Invalid page Size (or) page Number input");
              System.out.println(">> Try \"purchase list help\" for proper syntax");
              return;
            }
            listAttributesMap.put("Pagelength", String.valueOf(pageLength));
            listAttributesMap.put("Pagenumber", String.valueOf(pageNumber));
            listHelper(listAttributesMap);
          } else {
            System.out.println("Invalid Extension Given!!!");
            System.out.println("Try \"purchase list help\" for proper syntax");
          }
        } else {
          System.out.println("Given attribute is not a searchable attribute!!");
          System.out.println("Try \"purchase list help\" for proper syntax");
        }
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"purchase list help\" for proper syntax");
      }

    } else {
      System.out.println("Invalid command format!!!");
      System.out.println(">> Try \"purchase list help\" for proper syntax");
    }
  }

  private void listHelper(HashMap<String, String> listAttributesMap)  {
    try{
    purchaseList = purchaseService.listPurchaseService(listAttributesMap);
    if (purchaseList == null) {
      if(!listAttributesMap.get("Searchtext").equals("id")){
        System.out.println(">>Given SearchText does not exist!!!");
        return;
        }
      else
        return;
    }
    for (Purchase purchase : purchaseList) {
      System.out.print(
          "id: "
              + purchase.getId()
              + ", date: "
              + purchase.getDate()
              + ", invoice: "
              + purchase.getInvoice()
              + ", ");
      System.out.print("[");
      for (PurchaseItem purchaseItem : purchase.getPurchaseItemList()) {
        System.out.print(
            "[name: "
                + purchaseItem.getProduct().getName()
                + ", quantity: "
                + purchaseItem.getQuantity()
                + ", price: "
                + purchaseItem.getUnitPurchasePrice()
                + "], ");
      }
      System.out.print("]");
      System.out.println();
    }
    }
    catch(Exception e)
    {
      System.out.println(e.getMessage());
    }
  }


  public void purchaseDeleteCLI(List<String> arguments) throws ApplicationErrorException {
    PurchaseService purchaseDeleteService = new PurchaseServiceImplementation();
    String numberRegex = "^[0-9]{1,10}$";
    if (arguments.size() == 3) {
      if (arguments.get(2).equals("help")) {
        System.out.println(
            ">> Delete purchase using following command \n"
                + "\n"
                + ">> purchase delete <invoice>\n"
                + "\t\tinvoice - numeric, mandatory\n"
                + "\t\t");
        return;
      } else if (arguments.get(2).matches(numberRegex)) {
        System.out.println(">> Are you sure want to delete the Purchase Entry y/n ? : ");
        String prompt = scanner.nextLine();
        if (prompt.equals("y")) {
          int resultCode = purchaseDeleteService.deletePurchaseService(arguments.get(2));
          if (resultCode == 1) {
            System.out.println(">> Purchase Deleted Successfully!!");
          } else if (resultCode == -1) {
            System.out.println(">> Purchase Deletion Failed!!!");
            System.out.println(">> Please check the invoice you have entered!!!");
            System.out.println(">> Try \"purchase delete help\" for proper syntax");
          }
        } else if (prompt.equals("n")) {
          System.out.println(">> Delete operation cancelled!!!");
        } else {
          System.out.println(">> Invalid delete prompt!!! Please select between y/n");
        }
      } else {
        System.out.println(">> Invalid format for invoice!!");
        System.out.println("Try \"purchase delete help\" for proper syntax!!!");
      }
    }
  }
}
