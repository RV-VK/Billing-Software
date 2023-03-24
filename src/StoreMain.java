import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
public class StoreMain{
    static Scanner scanner;
    public static void main(String[] args) throws SQLException {
        scanner=new Scanner(System.in);
        do{
            System.out.print("> ");
            String command=scanner.nextLine();
            String[] arguments=command.split("\\,");
            String[] commandlet=arguments[0].split(" ");
            String commandString=commandlet[0];
            switch(commandString)
            {
                case "product":
                    String operationString=commandlet[1];
                    switch(operationString)
                    {
                        case "create":
                            String nameRegex="^[a-zA-Z]*{3,30}$";
                            String codeRegex="^[a-zA-Z0-9]+{2,6}$";
                            if(arguments.length==1&&arguments[0].equals("product create help"))
                            {
                                System.out.println(">> create product using the following template\n" +
                                        ">> code, name, unit, type, price, stock\n" +
                                        "\t\n" +
                                        "\tcode - text, min - 2 - 6, mandatory\n" +
                                        "\tname - text, min 3 - 30 char, mandatory\n" +
                                        "\tunitcode - text, kg/l/piece/combo, mandatory\n" +
                                        "\ttype - text, between enumerated values, mandatory \n" +
                                        "\tprice - number, mandatory\n" +
                                        "\tstock - number, default 0\n" +
                                        "\t\n" +
                                        ">\tproduct create code, productname, unitcode, type, price, stock\n" +
                                        "                         or\n" +
                                        "> product create :enter\n" +
                                        "code, name, unitcode, type, price, stock\n");
                                break;
                            }
                            else if(arguments.length==1&&arguments[0].equals("product create"))
                            {
                                System.out.print("> ");
                                String paramaters=scanner.nextLine();
                                String[] productAttributes=paramaters.split("\\,");
                                if(productAttributes.length<5)
                                {
                                    System.out.println(">>Insufficient Arguments for command \""+arguments[0]+"\"");
                                    System.out.println(">>Try \"product create help\" for proper syntax");
                                    break;
                                }
                                else if(productAttributes.length>6)
                                {
                                    System.out.println(">>Too many arguments for command \""+arguments[0]+"\"");
                                    System.out.println(">>Try \"product create help\" for proper syntax");
                                    break;
                                }
                                String code=productAttributes[0];
                                String name=productAttributes[1];
                                String unitcode=productAttributes[2];
                                String type=productAttributes[3];
                                double price=0;
                                try {
                                    price = Double.parseDouble(productAttributes[4]);
                                }
                                catch(Exception e)
                                {
                                    System.out.println(">>Invalid format for 4th Argument \"price\"");
                                    System.out.println(">>Try \"product create help\" for proper syntax");
                                    break;
                                }
                                float stock=0;
                                if(productAttributes.length==6)
                                {
                                    try {
                                        stock = Float.parseFloat(arguments[5]);
                                    }
                                    catch(Exception e){
                                        System.out.println(">>Invalid format for 5th argument \"stock\"");
                                        System.out.println(">>Try \"product create help\" for proper syntax");
                                        break;
                                    }
                                }
                                Product product=new Product(code,name,unitcode,type,stock,price);
                                Product.create(product);
                                break;

                            }

                            else if(arguments.length<5)
                            {
                                System.out.println(">>Insufficient or Invalid Arguments for command "+commandString+" "+operationString);
                                System.out.println(">>Try \""+commandString+" "+operationString+ " help\" for Proper Syntax");
                                break;
                            }
                            else if(arguments.length>6)
                            {
                                System.out.println(">>Too many Arguments for command "+commandString+" "+operationString);
                                break;
                            }
                            String code=commandlet[2];
                            String name=arguments[1];
                            String unitcode=arguments[2];
                            String type=arguments[3];
                            double price=0;
                            try {
                                 price = Double.parseDouble(arguments[4]);
                            }
                            catch(Exception e)
                            {
                                System.out.println(">>Invalid format for 4th Argument \"price\"");
                                System.out.println(">>Try \"product create help\" for proper syntax");
                                break;
                            }
                            float stock=0;
                            if(arguments.length==6)
                            {
                                try {
                                    stock = Float.parseFloat(arguments[5]);
                                }
                                catch(Exception e){
                                    System.out.println(">>Invalid format for 5th argument \"stock\"");
                                    System.out.println(">>Try \"product create help\" for proper syntax");
                                    break;
                                }
                            }
                            Product product=new Product(code,name,unitcode,type,stock,price);
                            Product.create(product);
                            break;
                        case "count":
                            Product.count();
                            break;
                        case "list":
                            if(commandlet.length==3&&commandlet[2].equals("help"))
                            {
                                System.out.println(">> List product with the following options\n" +
                                        ">> product list - will list all the products default to maximum upto 20 products\n" +
                                        ">> product list -p 10 - pageable list shows 10 products as default\n" +
                                        ">> product list -p 10 3 - pagable list shows 10 products in 3rd page, ie., product from 21 to 30\n" +
                                        ">> product list -s searchtext - search the product with the given search text in all the searchable attributes\n" +
                                        ">> product list -s <attr>: searchtext - search the product with the given search text in all the given attribute\n" +
                                        ">> product list -s <attr>: searchtext -p 10 6 - pagable list shows 10 products in 6th page with the given search text in the given attribute\n");
                            }
                            else if(commandlet.length==2)
                            {
                                 Product.list();
                                 break;
                            }
                            else if(commandlet.length==4)
                            {
                                int pageLength=0;
                                if(commandlet[2].equals("-p"))
                                {   try {
                                     pageLength = Integer.parseInt(commandlet[3]);
                                }
                                catch(Exception e)
                                {
                                    System.out.println(">> Invalid page Size input");
                                    System.out.println(">> Try \"product list help\" for proper syntax");
                                }
                                    Product.list(pageLength);
                                }
                               else if(commandlet[2].equals("-s"))
                                {

                                }
                               else
                                {
                                    System.out.println(">> Invalid Extension given");
                                    System.out.println(">> Try \"product list help\" for proper syntax");
                                }

                            }

                            else if(commandlet.length==5)
                            {
                                int pageLength=0;
                                int pageNumber=0;
                                if(commandlet[2].equals("-p"))
                                {
                                    try{
                                        pageLength=Integer.parseInt(commandlet[3]);
                                        pageNumber=Integer.parseInt(commandlet[4]);
                                    }
                                    catch(Exception e)
                                    {
                                        System.out.println(">> Invalid page Size (or) page Number input");
                                        System.out.println(">> Try \"product list help\" for proper syntax");
                                    }
                                    Product.list(pageLength,pageNumber);
                                }
                                else if(commandlet[2].equals("-s"))
                                {
                                    List<String> productAttributes= Arrays.asList("id","code","name","unitcode","type","price","stock","costprice");
                                    String attribute=commandlet[3];
                                    attribute=attribute.replace(":","");
                                    String searchText=commandlet[4];
                                    if(productAttributes.contains(attribute))
                                    {
                                           Product.list(attribute,searchText);
                                    }
                                    else
                                    {
                                        System.out.println("Given attribute is not a searchable attribute!!");
                                        System.out.println("Try \"product list help\" for proper syntax");
                                    }
                                    break;

                                }
                                else {
                                    System.out.println(">> Invalid Extension given");
                                    System.out.println(">> Try \"product list help\" for proper syntax");
                                }
                            }
                            break;
                        case "edit":
                            break;
                        case "delete":
                            break;
                        default:
                            System.out.println("Invalid operation for command "+"\""+commandString+"\"");
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