package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.*;
import Service.SalesService;
import Service.SalesServiceImplementation;
import java.util.*;

public class SalesCLI {
  private String salesDate;
  private List<SalesItem> salesItemList = new ArrayList<>();
  private double grandTotal;
  private String code;
  private float quantity;
  private int pageLength;
  private int pageNumber;
  private String attribute;
  private String searchText;
  private HashMap<String, String> listAttributesMap = new HashMap<>();
  private List<String> saleAttributes = Arrays.asList("id", "date");
  private SalesService salesService = new SalesServiceImplementation();
  private Scanner scanner = new Scanner(System.in);
  private List<Sales> salesList;

  public void salesCreateCLI(String command) {
    String productcodeRegex = "^[a-zA-Z0-9]{2,6}$";
    String[] commandEntities = command.split(",\\s*(?=\\[)");
    if (commandEntities.length < 1) {
      System.out.println(">> Insufficient arguments to start a Sale!!!");
      System.out.println(">> Try \"sales help\" for proper Syntax!!!");
    } else {
      String[] commandArguments = commandEntities[0].split("\\s+");
      salesDate = commandArguments[1].trim().replace(",", "");
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
        code = itemVariables[0].trim();
        if (!code.matches(productcodeRegex)) {
          System.out.println(">> Invalid format for product code in product :" + i);
          System.out.println(">> Try \"sales help\" for proper syntax!!");
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
      Sales createdSale;
      try {
        createdSale = salesService.createSalesService(sales);
      } catch (Exception e) {
        System.out.println(e.getMessage());
        return;
      }
      if (createdSale == null) {
        System.out.println(
            ">> Out of Stock Product Entered Please check the entered products!!");
      } else if (createdSale.getDate() != null) {
        System.out.println(
            "**********************************************************************************");
        System.out.println("\t\tSALES BILL " + createdSale.getId());
        System.out.println(
            "**********************************************************************************");
        System.out.println("SNO\t\tPRODUCT NAME\t\t\tQTY\t\tPRICE\t\tTOTAL");
        System.out.println(
            "----------------------------------------------------------------------------------");
        for (int i = 0; i < createdSale.getSalesItemList().size(); i++) {
          System.out.printf(
              "%d\t\t%-20s\t\t\t%.1f\t\t%.2f\t\t%.2f%n",
              i + 1,
              createdSale.getSalesItemList().get(i).getProduct().getName(),
              createdSale.getSalesItemList().get(i).getQuantity(),
              createdSale.getSalesItemList().get(i).getUnitSalesPrice(),
              (createdSale.getSalesItemList().get(i).getQuantity()
                  * createdSale.getSalesItemList().get(i).getUnitSalesPrice()));
        }
        System.out.println(
            "----------------------------------------------------------------------------------");
        System.out.printf("GRAND TOTAL\t\t\t\t\t\t\t\t\t\t\t%.2f%n", createdSale.getGrandTotal());
        System.out.println(
            "----------------------------------------------------------------------------------");
      }
      else if(createdSale.getDate()==null){
        System.out.println(">> Non-Existing Product Code Entered!! Please check the Product codes!");
      }
    }
  }

  /**
   * To count the number of Sales Entry Recorded in the Sales Table.
   *
   * @param arguments The arguments provided for the command.
   * @throws ApplicationErrorException Exception thrown when there is an error with DB Persistence
   */
  public void salesCountCLI(List<String> arguments) throws ApplicationErrorException {
    if (arguments.size() == 3) {
      if (arguments.get(2).equals("help")) {
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
      } else {
        System.out.println(">> Invalid command given!!!");
        System.out.println(">> Try \"sales count  help\" for proper syntax!!");
      }
      return;
    }
    if (arguments.size() == 2) {
      int salesCount = salesService.countSalesService(null);
      System.out.println(">> SalesCount :" + salesCount);
      return;
    }
    if (arguments.size() == 4) {
      if (arguments.get(2).equals("-d")) {
        String parameter = arguments.get(3);
        int salesCount = salesService.countSalesService(parameter);
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

  public void salesListCLI(List<String> arguments) {
    listAttributesMap.put("Pagelength", null);
    listAttributesMap.put("Pagenumber", null);
    listAttributesMap.put("Attribute", null);
    listAttributesMap.put("Searchtext", null);
    if (arguments.size() == 3) {
      if (arguments.get(2).equals("help")) {
        System.out.println(
            " >> List sales with the following options\n"
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
          System.out.println(">> Try \"sales list help\" for proper syntax");
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
        System.out.println(">> Try \"sales list help\" for proper syntax");
      }
    } else if (arguments.size() == 5) {
      if (arguments.get(2).equals("-p")) {
        try {
          pageLength = Integer.parseInt(arguments.get(3));
          pageNumber = Integer.parseInt(arguments.get(4));
        } catch (Exception e) {
          System.out.println(">> Invalid page Size (or) page Number input");
          System.out.println(">> Try \"sales list help\" for proper syntax");
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
        if (saleAttributes.contains(attribute)) {
          listAttributesMap.put("Attribute", attribute);
          listAttributesMap.put("Searchtext", searchText);
          listAttributesMap.put("Pagelength", "20");
          listAttributesMap.put("Pagenumber", String.valueOf(1));
          listHelper(listAttributesMap);
        } else {
          System.out.println("Given attribute is not a searchable attribute!!");
          System.out.println("Try \"sales list help\" for proper syntax");
        }
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"sales list help\" for proper syntax");
      }
    } else if (arguments.size() == 7) {
      if (arguments.get(2).equals("-s")) {
        attribute = arguments.get(3);
        attribute = attribute.replace(":", "");
        searchText = arguments.get(4);
        listAttributesMap.put("Attribute", attribute);
        listAttributesMap.put("Searchtext", searchText);
        if (saleAttributes.contains(attribute)) {
          if (arguments.get(5).equals("-p")) {
            try {
              pageLength = Integer.parseInt(arguments.get(6));
            } catch (Exception e) {
              System.out.println(">> Invalid page Size input");
              System.out.println(">> Try \"sales list help\" for proper syntax");
              return;
            }
            listAttributesMap.put("Pagelength", String.valueOf(pageLength));
            listAttributesMap.put("Pagenumber", "1");
            listHelper(listAttributesMap);
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
    } else if (arguments.size() == 8) {
      if (arguments.get(2).equals("-s")) {
        attribute = arguments.get(3);
        attribute = attribute.replace(":", "");
        searchText = arguments.get(4);
        listAttributesMap.put("Attribute", attribute);
        listAttributesMap.put("Searchtext", searchText);
        if (saleAttributes.contains(attribute)) {
          if (arguments.get(5).equals("-p")) {
            try {
              pageLength = Integer.parseInt(arguments.get(6));
              pageNumber = Integer.parseInt(arguments.get(7));
            } catch (Exception e) {
              System.out.println(">> Invalid page Size (or) page Number input");
              System.out.println(">> Try \"sales list help\" for proper syntax");
              return;
            }
            listAttributesMap.put("Pagelength", String.valueOf(pageLength));
            listAttributesMap.put("Pagenumber", String.valueOf(pageNumber));
            listHelper(listAttributesMap);
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

  private void listHelper(HashMap<String, String> listAttributesMap) {
    try{
    salesList = salesService.listSalesService(listAttributesMap);
    if (salesList == null) {
        if (!listAttributesMap.get("Searchtext").equals("id")) {
          System.out.println(">> Given SearchText does not exist!!!");
          return;
        }
      else
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
      System.out.print("] ");
        System.out.print(sales.getGrandTotal());
      System.out.println();
    }

    }
    catch(Exception e)
    {
      System.out.println(e.getMessage());
    }
  }

  public void salesDeleteCLI(List<String> arguments) throws ApplicationErrorException {
    String numberRegex = "^[0-9]{1,10}$";
    if (arguments.size() == 3) {
      if (arguments.get(2).equals("help")) {
        System.out.println(
            ">> >> Delete sales using following command \n"
                + "\n"
                + ">> sales delete <id>\n"
                + "\t\tid - numeric, mandatory");
      } else if (arguments.get(2).matches(numberRegex)) {
        System.out.println(">> Are you sure you want to delete the Sales Entry y/n : ");
        String prompt = scanner.nextLine();
        if (prompt.equals("y")) {
          int resultCode = salesService.deleteSalesService(arguments.get(2));
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
