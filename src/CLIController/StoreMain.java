package CLIController;
import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import DAO.UserDAO;

import java.util.HashMap;
import java.util.Scanner;
public class StoreMain{
    static Scanner scanner;
    public static void main(String[] args) throws ApplicationErrorException, PageCountOutOfBoundsException {
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
            HashMap<String,String> attributeMap=new HashMap<>();
            attributeMap.put("id",null);
            attributeMap.put("code",null);
            attributeMap.put("name",null);
            attributeMap.put("unitcode",null);
            attributeMap.put("type",null);
            attributeMap.put("price",null);

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
                            System.out.println("Invalid operation for command " + "\"" + commandString + "\"");
                            System.out.println("Try \"help\" for proper syntax");
                    }
                    break;
                case "store":
                    break;
                case "unit":
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