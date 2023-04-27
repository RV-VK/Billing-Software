package CLIController;

import DAO.ApplicationErrorException;
import Entity.Product;
import Service.ProductService;
import Service.ProductServiceImplementation;

import java.sql.SQLException;
import java.util.*;

public class StoreMain{
    static Scanner scanner;
    public static void main(String[] args) throws SQLException, ApplicationErrorException {
        scanner=new Scanner(System.in);
        System.out.println("___________________WELCOME TO THE BILLING SOFTWARE_____________________");
        do{
            System.out.print("> ");
            String command=scanner.nextLine();
            String[] arguments=command.split("\\,");
            String[] commandlet=arguments[0].split(" ");
            String commandString=commandlet[0];
            HashMap<String,String> attributeMap=new HashMap<>();
            attributeMap.put("id",null);
            attributeMap.put("code",null);
            attributeMap.put("name",null);
            attributeMap.put("unitcode",null);
            attributeMap.put("type",null);
            attributeMap.put("price",null);
            HashMap<String,String> listAttributesMap=new HashMap<>();
            listAttributesMap.put("Pagelength",null);
            listAttributesMap.put("Pagenumber",null);
            listAttributesMap.put("Attribute",null);
            listAttributesMap.put("Searchtext",null);
            switch(commandString)
            {
                case "product":
                    String operationString=commandlet[1];
                    switch(operationString)
                    {
                        case "create":
                            String nameRegex="^[a-zA-Z]{3,30}$";
                            String codeRegex="^[a-zA-Z0-9]{2,6}$";
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
                                if(!code.matches(codeRegex))
                                {
                                    System.out.println(">> Invalid format for 1st argument \"code\"");
                                    System.out.println(">> Try \"product create help\" for proper syntax");
                                }
                                if(!name.matches(nameRegex))
                                {
                                    System.out.println(">> Invalid format for 2nd argument \"name\"");
                                    System.out.println(">> Try \"product create help\" for proper syntax");
                                }
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
                                ProductService productService=new ProductServiceImplementation();
                                int resultCode=productService.createProductService(product);
                                if(resultCode==1)
                                {
                                    System.out.println(">> Product Created Successfully!");
                                }
                                else
                                {
                                    System.out.println(">> Product Creation failed");
                                }
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
                            ProductService productService=new ProductServiceImplementation();
                            int resultCode=productService.createProductService(product);
                            if(resultCode==1)
                            {
                                System.out.println(">> Product Created Successfully!");
                            }
                            else
                            {
                                System.out.println(">> Product Creation failed");
                            }
                            break;
                        case "count":
                            ProductService countProduct=new ProductServiceImplementation();
                            int productCount=countProduct.countProductService();
                            System.out.println(">> ProductCount "+productCount);
                            break;
                        case "list":
                            ProductService listService=new ProductServiceImplementation();
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
                                 listService.listProductService(listAttributesMap);
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
                                    listAttributesMap.put("Pagelength", String.valueOf(pageLength));
                                    listService.listProductService(listAttributesMap);
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
                                    listAttributesMap.put("Pagelength", String.valueOf(pageLength));
                                    listAttributesMap.put("Pagenumber", String.valueOf(pageNumber));
                                    listService.listProductService(listAttributesMap);
                                }
                                else if(commandlet[2].equals("-s"))
                                {
                                    List<String> productAttributes= Arrays.asList("id","code","name","unitcode","type","price","stock","costprice");
                                    String attribute=commandlet[3];
                                    attribute=attribute.replace(":","");
                                    String searchText=commandlet[4];
                                    if(productAttributes.contains(attribute))
                                    {
                                           listAttributesMap.put("Attribute",attribute);
                                           listAttributesMap.put("Searchtext",searchText);
                                           listService.listProductService(listAttributesMap);
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
                            else if(commandlet.length==7)
                            {
                                if(commandlet[2].equals("-s"))
                              {
                                List<String> productAttributes= Arrays.asList("id","code","name","unitcode","type","price","stock","costprice");
                                String attribute=commandlet[3];
                                attribute=attribute.replace(":","");
                                String searchText=commandlet[4];
                                listAttributesMap.put("Attribute",attribute);
                                listAttributesMap.put("Searchtext",searchText);
                                if(productAttributes.contains(attribute))
                                {
                                    int pageLength=0;
                                    if(commandlet[5].equals("-p"))
                                    {
                                        try{
                                            pageLength=Integer.parseInt(commandlet[6]);
                                        }
                                        catch(Exception e)
                                        {
                                            System.out.println(">> Invalid page Size input");
                                            System.out.println(">> Try \"product list help\" for proper syntax");
                                            break;
                                        }
                                        listAttributesMap.put("Pagelength", String.valueOf(pageLength));
                                        listService.listProductService(attributeMap);
                                    }
                                    else
                                    {
                                        System.out.println(">> Invalid Command Extension format !!!");
                                        System.out.println("Try \"product list help\" for proper syntax");
                                        break;
                                    }
                                }
                                else
                                {
                                    System.out.println("Given attribute is not a searchable attribute!!");
                                    System.out.println("Try \"product list help\" for proper syntax");
                                }

                            }
                                else {
                                    System.out.println(">> Invalid Extension given");
                                    System.out.println(">> Try \"product list help\" for proper syntax");
                                }

                            }
                            else if(commandlet.length==8)
                            {
                                if(commandlet[2].equals("-s"))
                                {
                                    List<String> productAttributes= Arrays.asList("id","code","name","unitcode","type","price","stock","costprice");
                                    String attribute=commandlet[3];
                                    attribute=attribute.replace(":","");
                                    String searchText=commandlet[4];
                                    int pageLength=0;
                                    int pageNumber=0;
                                    listAttributesMap.put("Attribute",attribute);
                                    listAttributesMap.put("Searchtext",searchText);
                                    if(productAttributes.contains(attribute))
                                    {
                                        if(commandlet[5].equals("-p"))
                                        {
                                            try{
                                                pageLength=Integer.parseInt(commandlet[6]);
                                                pageNumber=Integer.parseInt(commandlet[7]);
                                            }
                                            catch(Exception e)
                                            {
                                                System.out.println(">> Invalid page Size (or) page Number input");
                                                System.out.println(">> Try \"product list help\" for proper syntax");
                                            }
                                            listAttributesMap.put("Pagelength", String.valueOf(pageLength));
                                            listAttributesMap.put("Pagenumber", String.valueOf(pageNumber));
                                            listService.listProductService(listAttributesMap);
                                        }
                                        else
                                        {
                                            System.out.println("Given attribute is not a searchable attribute!!");
                                            System.out.println("Try \"product list help\" for proper syntax");
                                        }
                                    }
                                    else
                                    {
                                        System.out.println("Given attribute is not a searchable attribute!!");
                                        System.out.println("Try \"product list help\" for proper syntax");
                                    }

                                }
                                else
                                {
                                    System.out.println(">> Invalid Extension given");
                                    System.out.println(">> Try \"product list help\" for proper syntax");
                                }

                            }
                            for(Map.Entry<String,String> entry:listAttributesMap.entrySet())
                            {
                                entry.setValue(null);
                            }
                            break;
                        case "edit":
                            ProductService productEdit=new ProductServiceImplementation();
                            if(arguments.length==1&&arguments[0].equals("product edit help"))
                            {
                                System.out.println(">> Edit product using following template. Copy the product data from the list, edit the attribute values. \n" +
                                        ">> id: <id - 6>, name: <name-edited>, unitcode: <unitcode>,  type: <type>, price: <price>\n" +
                                        "\n" +
                                        ">> You can also restrict the product data by editable attributes. Id attribute is mandatory for all the edit operation.\n" +
                                        ">> id: <id - 6>, name: <name-edited>, unitcode: <unitcode-edited>\n" +
                                        "\n" +
                                        ">> You can not give empty or null values to the mandatory attributes.\n" +
                                        ">> id: <id - 6>, name: , unitcode: null\n" +
                                        ">>\n" +
                                        " \n" +
                                        " \tid\t - number, mandatory\t\n" +
                                        "\tname - text, min 3 - 30 char, mandatory\n" +
                                        "\tunitcode - text, kg/l/piece/combo, mandatory\n" +
                                        "\ttype - text, between enumerated values, mandatory \n" +
                                        "\tcostprice - numeric, mandatory\n" +
                                        "\t\n" +
                                        ">\tproduct edit id:<id - 6>, name: <name-edited>, unitcode: <unitcode>,  type: <type>, price: <price>\n" +
                                        "                         or\n" +
                                        "> product edit :enter\n" +
                                        "> id: <id - 6>, name: <name-edited>, unitcode: <unitcode>,  type: <type>, price: <price>");
                            }
                            else if(arguments.length==1&&arguments[0].equals("product edit"))
                            {
                                System.out.print("> ");
                                String paramaters=scanner.nextLine();
                                String[] productAttributes=paramaters.split("\\,");
                                if(productAttributes[0].contains("id")) {
                                    for (String attribute : productAttributes) {
                                        if (attribute.contains("id")) {
                                            String[] keyValues = attribute.split("\\:");
                                            attributeMap.put("id", keyValues[1]);
                                        }
                                        else if (attribute.contains("name")) {
                                            String[] keyValues = attribute.split("\\:");
                                            attributeMap.put("name", keyValues[1]);
                                        }
                                        else if (attribute.contains("unitcode")) {
                                            String[] keyValues = attribute.split("\\:");
                                            attributeMap.put("unitcode", keyValues[1]);
                                        }
                                        else if (attribute.contains("code")&&!attribute.contains("unitcode")) {
                                                String[] keyValues = attribute.split("\\:");
                                                attributeMap.put("code", keyValues[1]);
                                        }
                                        else if (attribute.contains("type")) {
                                            String[] keyValues = attribute.split("\\:");
                                            attributeMap.put("type", keyValues[1]);
                                        } else if (attribute.contains(("price"))) {
                                            String[] keyValues = attribute.split("\\:");
                                            attributeMap.put("price", keyValues[1]);
                                        } else {
                                            System.out.println(">> Invalid attribute given!!! : "+attribute);
                                            System.out.println(">> Try \" product edit help\" for proper syntax");
                                            break;
                                        }
                                    }
                                    if(attributeMap.get("id").equals(""))
                                    {
                                        System.out.println(">> Id should not be null");
                                        System.out.println(">> Try \"product edit help\" for proper Syntax");
                                        break;
                                    }
                                    int id=0;
                                    try{
                                        id=Integer.parseInt(attributeMap.get("id").trim());
                                    }
                                    catch(Exception e)
                                    {
                                        System.out.println(">> Id must be a number");
                                    }
                                    int statusCode=productEdit.editProductService(attributeMap);
                                    if(statusCode==1)
                                    {
                                        System.out.println(">> Product Edited Succesfully");
                                    }
                                    else if(statusCode==-1)
                                    {
                                        System.out.println(">> Product edit failed!!!");
                                        System.out.println(">>Please check the Id you have entered!!!");
                                    }
                                    else if(statusCode==0)
                                    {
                                        System.out.println(">>Invalid format of attributes given for edit Command!!!");
                                        System.out.println(">>Try \"product edit help\" for proper syntax");
                                    }
                                }

                                else {
                                    System.out.println(">> Id is a Mandatory argument for every Edit operation");
                                    System.out.println(">> For every Edit operation the first argument must be product's ID");
                                    System.out.println(">> Try \"product edit help\" for proper syntax");
                                    break;
                                }
                            }
                            else if(arguments.length>6)
                            {
                                System.out.println(">>Too many Arguments for command "+commandString+" "+operationString);
                                break;
                            }
                            else if(!commandlet[2].contains("id"))
                            {
                                System.out.println(">> Id is a Mandatory argument for every Edit operation");
                                System.out.println(">> For every Edit operation the first argument must be product's ID");
                                System.out.println(">> Try \"product edit help\" for proper syntax");
                                break;
                            }
                            else{
                                attributeMap.put("id",commandlet[3]);
                                if(attributeMap.get("id").equals("")) {
                                    System.out.println(">> Id should not be null");
                                    System.out.println(">> Try \"product edit help\" for proper Syntax");
                                    break;
                                }
                                    int id=0;
                                try{
                                     id=Integer.parseInt(attributeMap.get("id"));
                                }
                                catch(Exception e){
                                    System.out.println(">> Id must be a Number!");
                                    System.out.println(">> Please Try \"product edit help\" for proper Syntax");
                                }
                                for(int index=1;index<arguments.length;index++)
                                {
                                    if(arguments[index].contains("name"))
                                    {
                                        String[] keyValues =arguments[index].split("\\:");
                                        attributeMap.put("name",keyValues[1]);
                                    }
                                    else if(arguments[index].contains("code")&&!arguments[index].contains("unitcode"))
                                    {
                                        String[] keyValues =arguments[index].split("\\:");
                                        attributeMap.put("code",keyValues[1]);
                                    }
                                    else if(arguments[index].contains("unitcode"))
                                    {
                                        String[] keyValues =arguments[index].split("\\:");
                                        attributeMap.put("unitcode",keyValues[1]);
                                    }
                                    else if(arguments[index].contains("type"))
                                    {
                                        String[] keyValues =arguments[index].split("\\:");
                                        attributeMap.put("type",keyValues[1]);
                                    }
                                    else if(arguments[index].contains("price"))
                                    {
                                        String[] keyValues =arguments[index].split("\\:");
                                        attributeMap.put("price",keyValues[1]);
                                    }
                                    else {
                                        System.out.println(">> Invalid attribute given!!! : "+arguments[index]);
                                        System.out.println(">> Try \"product edit help\" for proper syntax");
                                    }
                                }

                                int statusCode=productEdit.editProductService(attributeMap);
                                if(statusCode==1)
                                {
                                    System.out.println(">> Product Edited Succesfully");
                                }
                                else if(statusCode==-1)
                                {
                                    System.out.println(">> Product edit failed!!!");
                                    System.out.println(">>Please check the Id you have entered!!!");
                                }
                                else if(statusCode==0)
                                {
                                    System.out.println(">>Invalid format of attributes given for edit Command!!!");
                                    System.out.println(">>Try \"product edit help\" for proper syntax");
                                }
                            }
                            for(Map.Entry<String,String> entry:listAttributesMap.entrySet())
                            {
                                entry.setValue(null);
                            }
                            break;
                        case "delete":
                            ProductService deleteProduct=new ProductServiceImplementation();
                            String numberRegex="^[0-9]*$";
                            String procodeRegex="^[a-zA-Z0-9]{2,6}$";
                            if(commandlet.length==3)
                            {
                               if(commandlet[2].matches(numberRegex))
                               {
                                   System.out.println(">> Are you sure want to delete the product y/n ? : ");
                                   String prompt=scanner.nextLine();
                                   if(prompt.equals("y")) {
                                       if(deleteProduct.deleteProductService(commandlet[2])==1)
                                       {
                                           System.out.println("Product Deletion Successfull!!!");
                                       }
                                       else if(deleteProduct.deleteProductService(commandlet[2])==-1)
                                       {
                                           System.out.println(">> Product Deletion Failed!!!");
                                           System.out.println(">> Please check the Id (or) Code that you have entered!!!");
                                           System.out.println("Try \"product delete help\" for proper syntax");
                                       }
                                   }
                                   else if(prompt.equals("n")){
                                       System.out.println(">> Delete operation cancelled");
                                   }
                                   else {
                                       System.out.println("Invalid delete prompt!!! Please select between y/n");
                                   }
                               }
                               else if(commandlet[2].equals("help"))
                               {
                                   System.out.println("> product delete help \n" +
                                           ">> delete product using the following template\n" +
                                           "\t\n" +
                                           "\t\tproductid - numeric, existing\n" +
                                           ">> product delete -c <code>\n" +
                                           "\t \n" +
                                           "\n" +
                                           "> product delete <id>");
                               }
                               else
                               {
                                   System.out.println(">> Invalid format for id!!!");
                                   System.out.println("Try \"product delete help\" for proper syntax");
                               }
                            }
                            else if(commandlet.length==4 && commandlet[2].equals("-c"))
                            {
                                if(commandlet[3].matches(procodeRegex))
                                {
                                    System.out.println(">> Are you sure want to delete the product y/n ? : ");
                                    String prompt=scanner.nextLine();
                                    if(prompt.equals("y")) {
                                        if(deleteProduct.deleteProductService(commandlet[3])==1)
                                        {
                                            System.out.println(">> Product Deletion Successfull!!!");
                                        }
                                        else if(deleteProduct.deleteProductService(commandlet[3])==-1)
                                        {
                                            System.out.println(">> Product Deletion Failed!!!");
                                            System.out.println(">> Please check the Id (or) Code that you have entered!!!");
                                            System.out.println("Try \"product delete help\" for proper syntax");
                                        }
                                        else if(deleteProduct.deleteProductService(commandlet[3])==0)
                                        {
                                            System.out.println(">> Product cannot be deleted!!!");
                                            System.out.println(">>Selected Product has stock left and should not be deleted!!!");
                                            System.out.println(">>Please check the selected product to be deleted!!!");
                                        }

                                    }
                                    else if(prompt.equals("n")){
                                        System.out.println(">> Delete operation cancelled");
                                    }
                                    else {
                                        System.out.println("Invalid delete prompt!!! Please select between y/n");
                                    }
                                }
                                else
                                {
                                    System.out.println(">> Invalid format for product Code!!!");
                                    System.out.println("Try \"product delete help\" for proper syntax");
                                }
                            }
                            else {
                                System.out.println("Invalid command format");
                                System.out.println("Try \"product delete help\" for proper syntax");
                            }
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