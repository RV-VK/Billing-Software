package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class PurchaseMain {
    static Scanner scanner;
    public static void PurchaseView() throws PageCountOutOfBoundsException, ApplicationErrorException {
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
                case "purchase":
                    PurchaseCLI purchaseCLI = new PurchaseCLI();
                    switch (operationString) {
                        case "count":
                            purchaseCLI.purchaseCountCLI(commandlist);
                            break;
                        case "list":
                            purchaseCLI.purchaseListCLI(commandlist);
                            break;
                        case "delete":
                            purchaseCLI.purchaseDeleteCLI(commandlist);
                            break;
                        case "help":
                            System.out.println(
                                    ">> purchase products using following command\n"
                                            + "purchase date, invoice, [code1, quantity1, costprice1], [code2, quantity2, costprice2]....\n"
                                            + "\n"
                                            + "\t  date - format( YYYY-MM-DD ), mandatory\n"
                                            + "\t\tinvoice - numbers, mandatory\n"
                                            + "\t\t\n"
                                            + "\t\tThe following purchase items should be given as array of items\n"
                                            + "\t\tcode - text, min 2 - 6 char, mandatory\n"
                                            + "\t\tquantity - numbers, mandatory\n"
                                            + "\t\tcostprice - numbers, mandatory");
                        default:
                            if (operationString.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")) {
                                purchaseCLI.purchaseCreateCLI(command);
                            } else {
                                System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
                                System.out.println(
                                        "Try either \"help\" for proper syntax or \"purchase help\" if you are trying to start a purchase!");
                            }
                    }
                    break;
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
                    System.out.println("purchase \n"
                         + "\t\t\tcreate - date, invoice, [name1, quantity1, costprice1], [name2, quantity2, costprice2]....\n"
                         + "\t\t\tcount\n"
                         + "\t\t\tlist\n"
                         + "\t\t\tdelete - invoice");
                    break;
                default:
                    System.out.println("Invalid Command! Not found!");
            }
        }while(true);
    }
}
