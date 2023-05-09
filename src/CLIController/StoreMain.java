package CLIController;
import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import org.checkerframework.checker.units.qual.A;

import java.sql.SQLException;
import java.util.*;

public class StoreMain{
    static Scanner scanner;
    public static void main(String[] args) throws ApplicationErrorException, PageCountOutOfBoundsException, SQLException {
        scanner=new Scanner(System.in);
        System.out.println("___________________WELCOME TO THE BILLING SOFTWARE_____________________");
        do{
            System.out.print("> ");
            String command=scanner.nextLine();
            String parts[];
            String commandlet[];
            if(command.contains(","))
            {
                parts=command.split("[,:]");
                commandlet=parts[0].split(" ");
            }
            else {
                parts=command.split(",");
                commandlet= command.split(" ");
            }
            ArrayList<String> commandlist=new ArrayList<>();
            if(parts.length==1)
            {
                Collections.addAll(commandlist,commandlet);
            }
            else{
                Collections.addAll(commandlist,commandlet);
                commandlist.addAll(Arrays.asList(parts).subList(1,parts.length));
            }
            String[] arguments= commandlist.toArray(new String[0]);
//            String[] arguments=command.split("\\s+");
            for(int index=0;index< arguments.length;index++)
            {
                arguments[index]=arguments[index].trim();
            }
            String commandString=arguments[0];
            switch(commandString) {
                case "product":
                    ProductCLI productCLI = new ProductCLI();
                    String operationString1 = arguments[1];
                    switch (operationString1) {
                        case "create":
                            productCLI.productCreateCLI(arguments);
                            break;
                        case "count":
                            productCLI.productCountCLI(arguments);
                            ;
                            break;
                        case "list":
                            productCLI.productListCLI(arguments);
                            break;
                        case "edit":
                            productCLI.productEditCLI(arguments);
                            break;
                        case "delete":
                            productCLI.productDeleteCLI(arguments);
                            break;
                        default:
                            System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
                            System.out.println("Try \"help\" for proper syntax");
                    }
                    break;
                case "user":
                    UserCLI userCLI = new UserCLI();
                    String operationString2 = arguments[1];
                    switch (operationString2)
                    {
                        case "create":
                            userCLI.userCreateCLI(arguments);
                            break;
                        case "count":
                            userCLI.userCountCLI(arguments);
                            break;
                        case "list":
                            userCLI.userListCLI(arguments);
                            break;
                        case "edit":
                            userCLI.userEditCLI(arguments);
                            break;
                        case "delete":
                            userCLI.userDeleteCLI(arguments);
                            break;
                        default:
                            System.out.println(">> Invalid operation for command " + "\"" + commandString + "\"");
                            System.out.println(">> Try \"help\" for proper syntax");
                    }
                    break;
                case "store":
                    StoreCLI storeCLI=new StoreCLI();
                    String operationString3=arguments[1];
                    switch (operationString3)
                    {
                        case "create":
                            storeCLI.storeCreateCLI(arguments);
                            break;
                        case "edit":
                            storeCLI.storeEditCLI(arguments);
                            break;
                        case "delete":
                            storeCLI.storeDeleteCLI(arguments);
                            break;
                        default:
                            System.out.println(">> Invalid operation for command " + "\"" + commandString + "\"");
                            System.out.println(">> Try \"help\" for proper syntax");
                    }
                    break;
                case "unit":
                    UnitCLI unitCLI=new UnitCLI();
                    String operationString4=arguments[1];
                    switch(operationString4)
                    {
                        case "create":
                            unitCLI.unitCreateCLI(arguments);
                            break;
                        case "list":
                            unitCLI.unitListCLI(arguments);
                            break;
                        case "edit":
                            unitCLI.unitEditCLI(arguments);
                            break;
                        case "delete":
                            unitCLI.unitDeleteCLI(arguments);
                            break;
                        default:
                            System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
                            System.out.println("Try \"help\" for proper syntax");
                    }
                    break;
                case "purchase":
                    PurchaseCLI purchaseCLI=new PurchaseCLI();
                    String operationString5=arguments[1];
                    switch(operationString5)
                    {

                        case "count":
                            purchaseCLI.purchaseCountCLI(arguments);
                            break;
                        case "list":
                            purchaseCLI.purchaseListCLI(arguments);
                            break;
                        case "delete":
                            purchaseCLI.purchaseDeleteCLI(arguments);
                            break;
                        case "help":
                            System.out.println(">> purchase products using following command\n" +
                                    "purchase date, invoice, [code1, quantity1, costprice1], [code2, quantity2, costprice2]....\n" +
                                    "\n" +
                                    "\t  date - format( YYYY-MM-DD ), mandatory\n" +
                                    "\t\tinvoice - numbers, mandatory\n" +
                                    "\t\t\n" +
                                    "\t\tThe following purchase items should be given as array of items\n" +
                                    "\t\tcode - text, min 2 - 6 char, mandatory\n" +
                                    "\t\tquantity - numbers, mandatory\n" +
                                    "\t\tcostprice - numbers, mandatory");
                        default:
                            if(operationString5.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))"))
                            {
                                purchaseCLI.purchaseCreateCLI(command);
                            }
                            else {
                                System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
                                System.out.println("Try either \"help\" for proper syntax or \"purchase help\" if you are trying to start a purchase!");
                            }
                    }
                    break;
                case "help":
                    String help="product\n" +
                            "\t    create - productname,unit,type,costprice\n" +
                            "\t    count\n" +
                            "\t    list\n" +
                            "\t    edit - productname,unit,type,costprice\n" +
                            "\t    delete - y/n with productname or productid";
                    System.out.println(help);
                    break;
                default:
                    System.out.println("Invalid Command! Not Found!");
            }
        }while(true);
    }
}