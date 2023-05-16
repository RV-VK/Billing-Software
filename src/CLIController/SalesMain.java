package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class SalesMain {
    static Scanner scanner;
    public static void SalesView() throws ApplicationErrorException, PageCountOutOfBoundsException {
        scanner=new Scanner(System.in);
        System.out.println(" TO THE BILLING SOFTWARE_____________________");
        System.out.println(">> Try \"help\" to know better!\n");
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
            String commandString = commandlist.get(0);
            String operationString = commandlist.get(1);
            switch(commandString)
            {
                case "sales":
                    SalesCLI salesCLI = new SalesCLI();
                    switch (operationString) {
                        case "count":
                            salesCLI.salesCountCLI(commandlist);
                            break;
                        case "list":
                            salesCLI.salesListCLI(commandlist);
                            break;
                        case "delete":
                            salesCLI.salesDeleteCLI(commandlist);
                            break;
                        case "help":
                            System.out.println(
                                    ">> sell products using following command\n"
                                            + "\n"
                                            + "sales date, [code1, quantity1], [code2, quantity2]....\n"
                                            + "\n"
                                            + "\t\tcode - text, min 3 - 30 char, mandatory\n"
                                            + "\t\tquantity - numbers, mandatory");
                            break;
                        default:
                            if (operationString.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")) {
                                salesCLI.salesCreateCLI(command);
                            } else {
                                System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
                                System.out.println(
                                        "Try either \"help\" for proper syntax or \"sales help\" if you are trying to start a purchase!");
                            }
                        }
                case "product":
                    ProductCLI productCLI=new ProductCLI();
                        switch(operationString)
                        {
                            case "list":
                                productCLI.list(commandlist);
                                break;
                            default:
                                System.out.println("Invalid operation for command \""+commandString+"\"");
                                System.out.println("Try \"Help\" for proper syntax");
                        }
                case "help":
                        System.out.println("product\n"
                                + "\t    list\n");
                        System.out.println("sales\n"
                            + "\t\t\tcreate - date, [name1, quantity1, costprice1], [name2, quantity2, costprice2]....\n"
                            + "\t\t\tcount\n"
                            + "\t\t\tlist\n"
                            + "\t\t\tdelete - id");
                            break;
                default:
                        System.out.println("Invalid Command! Not found!");
      }
    }while(true);
   }
}