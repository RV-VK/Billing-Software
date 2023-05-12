package CLIController;
import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.*;
import Service.SalesService;
import Service.SalesServiceImplementation;
import java.util.*;
public class SalesCLI {
  HashMap<String, String> listAttributesMap = new HashMap<>();

  public void salesCreateCLI(String command) {
    String codeRegex = "^[a-zA-Z0-9]{2,6}$";
    String[] commandEntities = command.split(",\\s*(?=\\[)");
    if (commandEntities.length < 1) {
      System.out.println(">> Insufficient arguments to start a Sale!!!");
      System.out.println(">> Try \"sales help\" for proper Syntax!!!");

    } else {
      String[] commandArguments = commandEntities[0].split("\\s+");
      String salesDate = commandArguments[1].trim().replace(",", "");
      List<SalesItem> salesItemList = new ArrayList<>();
      double grandTotal = 0;
      for (int i = 1; i < commandEntities.length; i++) {
        String item = commandEntities[i].replaceAll("[\\[\\]]", "");
        String[] itemVariables = item.split(",");
        if (itemVariables.length < 2) {
          System.out.println(">> Please provide sufficient detailes for product " + i);
          System.out.println(">> Try \"sales help\" for proper syntax");
        }
        if (itemVariables.length > 2) {
          System.out.println(">> Improper format of product details given!!!");
          System.out.println(">> Try \"sales help\" for proper syntax");
          return;
        }
        String code = itemVariables[0].trim();
        float quantity;
        if (!code.matches(codeRegex) || code.equals("")) {
          System.out.println(">> Invalid Format for product code in Product " + i);
          return;
        }
        try {
          quantity = Float.parseFloat(itemVariables[1].trim());
        } catch (Exception e) {
          System.out.println(">> Quantity must be a number !! Error in Product " + i);
          System.out.println(">> Try \"sales help\" for proper syntax");
          return;
        }
        salesItemList.add(new SalesItem(new Product(code), quantity));
        grandTotal = 0;
      }
      Sales sales = new Sales(salesDate, salesItemList, grandTotal);
      SalesService salesCreateService = new SalesServiceImplementation();
      try {
        salesCreateService.createSalesService(sales);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * To count the number of Sales Entry in the Recorded in the Sales Table.
   * @param arguments
   * @throws ApplicationErrorException
   */
  public void salesCountCLI(String[] arguments) throws ApplicationErrorException {
    SalesService countSalesService = new SalesServiceImplementation();
    if (arguments.length == 3) {
      if (arguments[2].equals("help")) {
        System.out.println(
            "Count Sales using the Following Template\n sales count -d <date>\n"
                + "\n"
                + ">> count : <number>\n"
                + "\n"
                + "> sales count\n"
                + "\n"
                + ">> count : <number>\n"
                + "\n"
                + "> sales count -c <category>\n"
                + "\n"
                + ">> count : <number>\n");
        return;
      } else {
        System.out.println(">> Invalid command given!!!");
        System.out.println(">> Try \"sales count  help\" for proper syntax!!");
        return;
      }
    }
    if (arguments.length == 2) {
      int salesCount = countSalesService.countSalesService(null);
      System.out.println(">> SalesCount :" + salesCount);
      return;
    }
    if (arguments.length == 4) {
      if (arguments[2].equals("-d")) {
        String parameter = arguments[3];
        int salesCount = countSalesService.countSalesService(parameter);
        if (salesCount > 0) System.out.println(">> SalesCount " + salesCount);
        else {
          System.out.println(">> Given Date or Category not found!!!");
          System.out.println(">> Please Try with an existing searchtext");
        }
      } else {
        System.out.println(">> Invalid extension Given");
        System.out.println(">> Try \"sales count help\" for proper syntax!!");
      }
    } else {
      System.out.println(">> Invalid Command Format!!!");
      System.out.println(">> Try \"sales count help\" for proper syntax");
    }
  }

  public void salesListCLI(String[] arguments)
      throws PageCountOutOfBoundsException, ApplicationErrorException {
    listAttributesMap.put("Pagelength", null);
    listAttributesMap.put("Pagenumber", null);
    listAttributesMap.put("Attribute", null);
    listAttributesMap.put("Searchtext", null);
    SalesService salesListService = new SalesServiceImplementation();
    List<Sales> salesList;
    if (arguments.length == 3) {
      if (arguments[2].equals("help")) {
        System.out.println(
            ">> >> List sales with the following options\n"
                + ">> sales list - will list all the sales default to maximum upto 20 sales\n"
                + ">> sales list -p 10 - pageable list shows 10 sales as default\n"
                + ">> sales list -p 10 3 - pagable list shows 10 sales in 3rd page, ie., sale from 21 to 30\n"
                + "\n"
                + ">> Use only the following attributes: id, date\n"
                + ">> sales list -s <attr>: searchtext - search the sale with the given search text in all the given attribute\n"
                + ">> sales list -s <attr>: searchtext -p 10 6 - pagable list shows 10 sales in 6th page with the given search text in the given attribute\n"
                + "\n"
                + "> sales list -s <date> : <23/03/2023> -p 5 2 \n"
                + "> sales list -s <id> : <10>\n");
        return;
      }
    }
    if (arguments.length == 2) {
      listAttributesMap.put("Pagelength", "20");
      listAttributesMap.put("Pagenumber", "1");
      listAttributesMap.put("Attribute","id");
      salesList = salesListService.listSalesService(listAttributesMap);
      for (Sales sales : salesList) {
        System.out.print("id: " + sales.getId() + ", date: " + sales.getDate() + ", ");
        System.out.print("[");
        for (SalesItem salesItem : sales.getSalesItemList()) {
          System.out.print(
              "[name: "
                  + salesItem.getProduct().getName()
                  + ", quantity: "
                  + salesItem.getQuantity()
                  + ", price: "
                  + salesItem.getUnitSalesPrice()
                  + "], ");
        }
        System.out.print("]");
        System.out.println();
      }

    } else if (arguments.length == 4) {
      int pageLength = 0;
      if (arguments[2].equals("-p")) {
        try {
          pageLength = Integer.parseInt(arguments[3]);
        } catch (Exception e) {
          System.out.println(">> Invalid page Size input");
          System.out.println(">> Try \"sales list help\" for proper syntax");
        }
        listAttributesMap.put("Pagelength", String.valueOf(pageLength));
        listAttributesMap.put("Pagenumber", String.valueOf(1));
        listAttributesMap.put("Attribute","id");
        salesList = salesListService.listSalesService(listAttributesMap);
        for (Sales sales : salesList) {
          System.out.print("id: " + sales.getId() + ", date: " + sales.getDate() + ", ");
          System.out.print("[");
          for (SalesItem salesItem : sales.getSalesItemList()) {
            System.out.print(
                "[name: "
                    + salesItem.getProduct().getName()
                    + ", quantity: "
                    + salesItem.getQuantity()
                    + ", price: "
                    + salesItem.getUnitSalesPrice()
                    + "], ");
          }
          System.out.print("]");
          System.out.println();
        }
      } else if (arguments[2].equals("-s")) {
        String searchText = arguments[3].trim();
        listAttributesMap.put("Searchtext", searchText);
        salesList = salesListService.listSalesService(listAttributesMap);
        if (salesList.size() == 0) {
          System.out.println(">> Given SearchText does not exist!!!");
        }
        for (Sales sales : salesList) {
          System.out.print("id: " + sales.getId() + ", date: " + sales.getDate() + ", ");
          System.out.print("[");
          for (SalesItem salesItem : sales.getSalesItemList()) {
            System.out.print(
                "[name: "
                    + salesItem.getProduct().getName()
                    + ", quantity: "
                    + salesItem.getQuantity()
                    + ", price: "
                    + salesItem.getUnitSalesPrice()
                    + "], ");
          }
          System.out.print("]");
          System.out.println();
        }
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"sales list help\" for proper syntax");
      }
    } else if (arguments.length == 5) {
      int pageLength;
      int pageNumber;
      if (arguments[2].equals("-p")) {
        try {
          pageLength = Integer.parseInt(arguments[3]);
          pageNumber = Integer.parseInt(arguments[4]);
        } catch (Exception e) {
          System.out.println(">> Invalid page Size (or) page Number input");
          System.out.println(">> Try \"sales list help\" for proper syntax");
          return;
        }
        listAttributesMap.put("Pagelength", String.valueOf(pageLength));
        listAttributesMap.put("Pagenumber", String.valueOf(pageNumber));
        listAttributesMap.put("Attribute","id");
        try {
          salesList = salesListService.listSalesService(listAttributesMap);
        } catch (Exception e) {
          System.out.println(e.getMessage());
          return;
        }
        for (Sales sales : salesList) {
          System.out.print("id: " + sales.getId() + ", date: " + sales.getDate() + ", ");
          System.out.print("[");
          for (SalesItem salesItem : sales.getSalesItemList()) {
            System.out.print(
                "[name: "
                    + salesItem.getProduct().getName()
                    + ", quantity: "
                    + salesItem.getQuantity()
                    + ", price: "
                    + salesItem.getUnitSalesPrice()
                    + "], ");
          }
          System.out.print("]");
          System.out.println();
        }
      } else if (arguments[2].equals("-s")) {
        List<String> saleAttributes = Arrays.asList("id", "date");
        String attribute = arguments[3];
        attribute = attribute.replace(":", "");
        String searchText = arguments[4];
        if (saleAttributes.contains(attribute)) {
          listAttributesMap.put("Attribute", attribute);
          listAttributesMap.put("Searchtext", searchText);
          listAttributesMap.put("Pagelength", "20");
          listAttributesMap.put("Pagenumber", String.valueOf(1));
          salesList = salesListService.listSalesService(listAttributesMap);
          if (salesList == null) {
            System.out.println(">>Given SearchText does not exist!!!");
            return;
          }
          for (Sales sales : salesList) {
            System.out.print("id: " + sales.getId() + ", date: " + sales.getDate() + ", ");
            System.out.print("[");
            for (SalesItem salesItem : sales.getSalesItemList()) {
              System.out.print(
                  "[name: "
                      + salesItem.getProduct().getName()
                      + ", quantity: "
                      + salesItem.getQuantity()
                      + ", price: "
                      + salesItem.getUnitSalesPrice()
                      + "], ");
            }
            System.out.print("]");
            System.out.println();
          }
        } else {
          System.out.println("Given attribute is not a searchable attribute!!");
          System.out.println("Try \"sales list help\" for proper syntax");
        }
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"sales list help\" for proper syntax");
      }
    } else if (arguments.length == 7) {
      if (arguments[2].equals("-s")) {
        List<String> saleAttributes = Arrays.asList("id", "date");
        String attribute = arguments[3];
        attribute = attribute.replace(":", "");
        String searchText = arguments[4];
        listAttributesMap.put("Attribute", attribute);
        listAttributesMap.put("Searchtext", searchText);
        if (saleAttributes.contains(attribute)) {
          int pageLength;
          if (arguments[5].equals("-p")) {
            try {
              pageLength = Integer.parseInt(arguments[6]);
            } catch (Exception e) {
              System.out.println(">> Invalid page Size input");
              System.out.println(">> Try \"sales list help\" for proper syntax");
              return;
            }
            listAttributesMap.put("Pagelength", String.valueOf(pageLength));
            listAttributesMap.put("Pagenumber", "1");
            salesList = salesListService.listSalesService(listAttributesMap);
            if (salesList == null) {
              System.out.println(">>Given SearchText does not exists");
              return;
            }
            for (Sales sales : salesList) {
              System.out.print("id: " + sales.getId() + ", date: " + sales.getDate() + ", ");
              System.out.print("[");
              for (SalesItem salesItem : sales.getSalesItemList()) {
                System.out.print(
                    "[name: "
                        + salesItem.getProduct().getName()
                        + ", quantity: "
                        + salesItem.getQuantity()
                        + ", price: "
                        + salesItem.getUnitSalesPrice()
                        + "], ");
              }
              System.out.print("]");
              System.out.println();
            }
          } else {
            System.out.println(">> Invalid Command Extension format !!!");
            System.out.println("Try \"sales list help\" for proper syntax");
          }
        } else {
          System.out.println("Given attribute is not a searchable attribute!!");
          System.out.println("Try \"sales list help\" for proper syntax");
        }

      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"sales list help\" for proper syntax");
      }
    } else if (arguments.length == 8) {
      if (arguments[2].equals("-s")) {
        List<String> saleAttributes = Arrays.asList("id", "date");
        String attribute = arguments[3];
        attribute = attribute.replace(":", "");
        String searchText = arguments[4];
        int pageLength;
        int pageNumber;
        listAttributesMap.put("Attribute", attribute);
        listAttributesMap.put("Searchtext", searchText);
        if (saleAttributes.contains(attribute)) {
          if (arguments[5].equals("-p")) {
            try {
              pageLength = Integer.parseInt(arguments[6]);
              pageNumber = Integer.parseInt(arguments[7]);
            } catch (Exception e) {
              System.out.println(">> Invalid page Size (or) page Number input");
              System.out.println(">> Try \"sales list help\" for proper syntax");
              return;
            }
            listAttributesMap.put("Pagelength", String.valueOf(pageLength));
            listAttributesMap.put("Pagenumber", String.valueOf(pageNumber));
            salesList = salesListService.listSalesService(listAttributesMap);
            if (salesList == null) {
              System.out.println(">>Given SearchText does not exist!!!");
              return;
            }
            for (Sales sales : salesList) {
              System.out.print("id: " + sales.getId() + ", date: " + sales.getDate() + ", ");
              System.out.print("[");
              for (SalesItem salesItem : sales.getSalesItemList()) {
                System.out.print(
                    "[name: "
                        + salesItem.getProduct().getName()
                        + ", quantity: "
                        + salesItem.getQuantity()
                        + ", price: "
                        + salesItem.getUnitSalesPrice()
                        + "], ");
              }
              System.out.print("]");
              System.out.println();
            }
          } else {
            System.out.println("Invalid Extension Given!!!");
            System.out.println("Try \"sales list help\" for proper syntax");
          }
        } else {
          System.out.println("Given attribute is not a searchable attribute!!");
          System.out.println("Try \"sales list help\" for proper syntax");
        }
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"sales list help\" for proper syntax");
      }

    } else {
      System.out.println("Invalid command format!!!");
      System.out.println(">> Try \"sales list help\" for proper syntax");
    }
  }
  public void salesDeleteCLI(String[] arguments) throws ApplicationErrorException {
    Scanner scanner = new Scanner(System.in);
    SalesService salesDeleteService = new SalesServiceImplementation();
    String numberRegex = "^[0-9]{1,10}$";
    if (arguments.length == 3) {
      if (arguments[2].equals("help")) {
        System.out.println(
            ">> >> Delete sales using following command \n"
                + "\n"
                + ">> sales delete <id>\n"
                + "\t\tid - numeric, mandatory");
        return;
      } else if (arguments[2].matches(numberRegex)) {
        System.out.println(">> Are you sure you want to delete the Sales Entry y/n : ");
        String prompt = scanner.nextLine();
        if (prompt.equals("y")) {
          int resultCode = salesDeleteService.deleteSalesService(arguments[2]);
          if (resultCode == 1) {
            System.out.println(">> Sales Entry Deleted Successfully!!!");
          } else if (resultCode == -1) {
            System.out.println(">> Sales Entry Deletion Failed!!");
            System.out.println(">> Please check the id you have entered!!!");
            System.out.println(">> Try \"sales delete help\" for proper syntax");
          }
        } else if (prompt.equals("n")) {
          System.out.println(">> Delete operation cancelled!!!");
        } else {
          System.out.println(">> Invalid delete prompt !! Please select between y/n");
        }
      } else {
        System.out.println(">> Invalid format for id");
        System.out.println(">> Try \"sales delete help\" for proper syntax");
      }
    }
  }
}
