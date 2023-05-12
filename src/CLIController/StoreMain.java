package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class StoreMain {
  static Scanner scanner;

  public static void main(String[] args)
      throws ApplicationErrorException, PageCountOutOfBoundsException, SQLException {
    scanner = new Scanner(System.in);
    System.out.println("___________________WELCOME TO THE BILLING SOFTWARE_____________________");
    do {
      System.out.print("> ");
      String command = scanner.nextLine();
      String[] parts;
      String[] commandlet;
      if (command.contains(",")) {
        parts = command.split("[,:]");
        commandlet = parts[0].split(" ");
      } else {
        parts = command.split(",");
        commandlet = command.split(" ");
      }
      ArrayList<String> commandlist = new ArrayList<>();
      if (parts.length == 1) {
        Collections.addAll(commandlist, commandlet);
      } else {
        Collections.addAll(commandlist, commandlet);
        commandlist.addAll(Arrays.asList(parts).subList(1, parts.length));
      }
      commandlist.stream().forEach(string-> string.trim());
      String commandString = commandlist.get(0);
      String operationString = commandlist.get(1);
      switch (commandString) {
        case "product":
          ProductCLI productCLI = new ProductCLI();
          switch (operationString) {
            case "create":
              productCLI.Create(commandlist);
              break;
            case "count":
              productCLI.count();
              break;
            case "list":
              productCLI.list(commandlist);
              break;
            case "edit":
              productCLI.edit(commandlist);
              break;
            case "delete":
              productCLI.delete(commandlist);
              break;
            default:
              System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
              System.out.println("Try \"help\" for proper syntax");
          }
          break;
//        case "user":
//          UserCLI userCLI = new UserCLI();
//          switch (operationString) {
//            case "create":
//              userCLI.userCreateCLI(commandlist);
//              break;
//            case "count":
//              userCLI.userCountCLI(commandlist);
//              break;
//            case "list":
//              userCLI.userListCLI(commandlist);
//              break;
//            case "edit":
//              userCLI.userEditCLI(commandlist);
//              break;
//            case "delete":
//              userCLI.userDeleteCLI(commandlist);
//              break;
//            default:
//              System.out.println(">> Invalid operation for command " + "\"" + commandString + "\"");
//              System.out.println(">> Try \"help\" for proper syntax");
//          }
//          break;
//        case "store":
//          StoreCLI storeCLI = new StoreCLI();
//          switch (operationString) {
//            case "create":
//              storeCLI.storeCreateCLI(commandlist);
//              break;
//            case "edit":
//              storeCLI.storeEditCLI(commandlist);
//              break;
//            case "delete":
//              storeCLI.storeDeleteCLI(commandlist);
//              break;
//            default:
//              System.out.println(">> Invalid operation for command " + "\"" + commandString + "\"");
//              System.out.println(">> Try \"help\" for proper syntax");
//          }
//          break;
//        case "unit":
//          UnitCLI unitCLI = new UnitCLI();
//          switch (operationString) {
//            case "create":
//              unitCLI.unitCreateCLI(commandlist);
//              break;
//            case "list":
//              unitCLI.unitListCLI(commandlist);
//              break;
//            case "edit":
//              unitCLI.unitEditCLI(commandlist);
//              break;
//            case "delete":
//              unitCLI.unitDeleteCLI(commandlist);
//              break;
//            default:
//              System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
//              System.out.println("Try \"help\" for proper syntax");
//          }
//          break;
//        case "purchase":
//          PurchaseCLI purchaseCLI = new PurchaseCLI();
//          switch (operationString) {
//            case "count":
//              purchaseCLI.purchaseCountCLI(commandlist);
//              break;
//            case "list":
//              purchaseCLI.purchaseListCLI(commandlist);
//              break;
//            case "delete":
//              purchaseCLI.purchaseDeleteCLI(commandlist);
//              break;
//            case "help":
//              System.out.println(
//                  ">> purchase products using following command\n"
//                      + "purchase date, invoice, [code1, quantity1, costprice1], [code2, quantity2, costprice2]....\n"
//                      + "\n"
//                      + "\t  date - format( YYYY-MM-DD ), mandatory\n"
//                      + "\t\tinvoice - numbers, mandatory\n"
//                      + "\t\t\n"
//                      + "\t\tThe following purchase items should be given as array of items\n"
//                      + "\t\tcode - text, min 2 - 6 char, mandatory\n"
//                      + "\t\tquantity - numbers, mandatory\n"
//                      + "\t\tcostprice - numbers, mandatory");
//            default:
//              if (operationString.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")) {
//                purchaseCLI.purchaseCreateCLI(command);
//              } else {
//                System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
//                System.out.println(
//                    "Try either \"help\" for proper syntax or \"purchase help\" if you are trying to start a purchase!");
//              }
//          }
//          break;
//        case "sales":
//          SalesCLI salesCLI = new SalesCLI();
//          switch (operationString) {
//            case "count":
//              salesCLI.salesCountCLI(commandlist);
//              break;
//            case "list":
//              salesCLI.salesListCLI(commandlist);
//              break;
//            case "delete":
//              salesCLI.salesDeleteCLI(commandlist);
//              break;
//            case "help":
//              System.out.println(
//                  ">> sell products using following command\n"
//                      + "\n"
//                      + "sales date, [code1, quantity1], [code2, quantity2]....\n"
//                      + "\n"
//                      + "\t\tcode - text, min 3 - 30 char, mandatory\n"
//                      + "\t\tquantity - numbers, mandatory");
//              break;
//            default:
//              if (operationString.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")) {
//                salesCLI.salesCreateCLI(command);
//              } else {
//                System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
//                System.out.println(
//                    "Try either \"help\" for proper syntax or \"sales help\" if you are trying to start a purchase!");
//              }
//          }
//          break;
        case "price":
          break;
        case "stock":
          break;
        case "help":
          String help =
              "product\n"
                  + "\t    create - productname,unit,type,costprice\n"
                  + "\t    count\n"
                  + "\t    list\n"
                  + "\t    edit - productname,unit,type,costprice\n"
                  + "\t    delete - y/n with productname or productid";
          System.out.println(help);
          break;
        default:
          System.out.println("Invalid Command! Not Found!");
      }
    } while (true);
  }
}
