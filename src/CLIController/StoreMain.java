package CLIController;
import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;
public class StoreMain{
    static Scanner scanner;
    public static void main(String[] args) throws ApplicationErrorException, PageCountOutOfBoundsException, SQLException {
        scanner=new Scanner(System.in);
        System.out.println("___________________WELCOME TO THE BILLING SOFTWARE_____________________");
        do{
            System.out.print("> ");
            String command=scanner.nextLine();
            String[] arguments=command.split("\\s+");
            for(int index=0;index< arguments.length;index++)
            {
                arguments[index]=arguments[index].replace(",","");
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