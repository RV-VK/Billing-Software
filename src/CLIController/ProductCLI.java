package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.Product;
import Service.ProductService;
import Service.ProductServiceImplementation;

import java.util.*;

public class ProductCLI {
    HashMap<String,String> attributeMap=new HashMap<>();
    HashMap<String,String> listAttributesMap=new HashMap<>();
    public void productCreateCLI(String[] arguments){
        Scanner scanner=new Scanner(System.in);
        String nameRegex="^[a-zA-Z\\s]{3,30}$";
        String codeRegex="^[a-zA-Z0-9]{2,6}$";
        if(arguments.length==3&&arguments[2].equals("help"))
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
            return;
        }
        else if(arguments.length==2)
        {
            System.out.print("> ");
            String paramaters=scanner.nextLine();
            String[] productAttributes=paramaters.split("\\,");
            if(productAttributes.length<5)
            {
                System.out.println(">>Insufficient Arguments for command \""+arguments[0]+"\"");
                System.out.println(">>Try \"product create help\" for proper syntax");
                return;
            }
            else if(productAttributes.length>6)
            {
                System.out.println(">>Too many arguments for command \""+arguments[0]+"\"");
                System.out.println(">>Try \"product create help\" for proper syntax");
                return;
            }
            String code=productAttributes[0].trim();
            String name=productAttributes[1].trim();
            String unitcode=productAttributes[2].trim();
            String type=productAttributes[3].trim();
            double price=0;
            if(!code.matches(codeRegex))
            {
                System.out.println(">> Invalid format for 1st argument \"code\"");
                System.out.println(">> Try \"product create help\" for proper syntax");
                return;
            }
            if(!name.matches(nameRegex))
            {
                System.out.println(">> Invalid format for 2nd argument \"name\"");
                System.out.println(">> Try \"product create help\" for proper syntax");
                return;
            }
            try {
                price = Double.parseDouble(productAttributes[4].trim());
            }
            catch(Exception e)
            {
                System.out.println(">>Invalid format for 4th Argument \"price\"");
                System.out.println(">>Try \"product create help\" for proper syntax");
                return;
            }
            float stock=0;
            if(productAttributes.length==6)
            {
                try {
                    stock = Float.parseFloat(productAttributes[5].trim());
                }
                catch(Exception e){
                    System.out.println(stock);
                    System.out.println(">>Invalid format for 5th argument \"stock\"");
                    System.out.println(">>Try \"product create help\" for proper syntax");
                    return;
                }
            }
            Product product=new Product(code,name,unitcode,type,stock,price);
            ProductService productService=new ProductServiceImplementation();
            int resultCode=1;
            try{
                resultCode=productService.createProductService(product);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                return;
            }
            if(resultCode==1)
            {
                System.out.println(">> Product Created Successfully!");
            }
            else if(resultCode==-1)
            {
                System.out.println(">> Product Creation failed!!!");
                System.out.println(">> The Product code you have entered already exists!!!");
            }
            else if(resultCode==0)
            {
                System.out.println(">> Product Creation failed!!!");
                System.out.println(">> The unit code you have entered does not exists!!!");
            }
            return;

        }

        else if(arguments.length<7)
        {
            System.out.println(">>Insufficient or Invalid Arguments for command \"product create\"");
            System.out.println(">>Try \"product create help\" for Proper Syntax");
            return;
        }
        else if(arguments.length>8)
        {
            System.out.println(">>Too many Arguments for command \"product create\"");
            return;
        }
        String code=arguments[2].trim();
        String name=arguments[3].trim();
        String unitcode=arguments[4].trim();
        String type=arguments[5].trim();
        double price=0;
        try {
            price = Double.parseDouble(arguments[6].trim());
        }
        catch(Exception e)
        {
            System.out.println(">>Invalid format for 4th Argument \"price\"");
            System.out.println(">>Try \"product create help\" for proper syntax");
            return;
        }
        float stock=0;
        if(arguments.length==8)
        {
            try {
                stock = Float.parseFloat(arguments[7].trim());
            }
            catch(Exception e){
                System.out.println(">>Invalid format for 5th argument \"stock\"");
                System.out.println(">>Try \"product create help\" for proper syntax");
                return;
            }
        }
        Product product=new Product(code,name,unitcode,type,stock,price);
        ProductService productService=new ProductServiceImplementation();
        int resultCode=1;
        try {
            resultCode = productService.createProductService(product);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return;
        }
        if(resultCode==1)
        {
            System.out.println(">> Product Created Successfully!");
        }
        else if(resultCode==-1)
        {
            System.out.println(">> Product Creation failed");
            System.out.println(">> The code you have entered Already exists!!!");
        }
        else if(resultCode==0)
        {
            System.out.println(">> Product Creation Failed!!!");
            System.out.println(">> The Unit you have entered does not exist!!!");
        }


    }
    public void productListCLI(String[] arguments) throws PageCountOutOfBoundsException, ApplicationErrorException {
        listAttributesMap.put("Pagelength",null);
        listAttributesMap.put("Pagenumber",null);
        listAttributesMap.put("Attribute",null);
        listAttributesMap.put("Searchtext",null);
        ProductService listService=new ProductServiceImplementation();
        List<Product> resultList;
        if(arguments.length==3&&arguments[2].equals("help"))
        {
            System.out.println(">> List product with the following options\n" +
                    ">> product list - will list all the products default to maximum upto 20 products\n" +
                    ">> product list -p 10 - pageable list shows 10 products as default\n" +
                    ">> product list -p 10 3 - pagable list shows 10 products in 3rd page, ie., product from 21 to 30\n" +
                    ">> product list -s searchtext - search the product with the given search text in all the searchable attributes\n" +
                    ">> product list -s <attr>: searchtext - search the product with the given search text in all the given attribute\n" +
                    ">> product list -s <attr>: searchtext -p 10 6 - pagable list shows 10 products in 6th page with the given search text in the given attribute\n");
            return;
        }
        else if(arguments.length==2)
        {
            listAttributesMap.put("Pagelength","20");
            resultList=listService.listProductService(listAttributesMap);
            for(Product resultProduct:resultList)
            {
                System.out.println(">> id: " + resultProduct.getId() + ", code: " + resultProduct.getCode() + ", name: " + resultProduct.getName() + ", type: " + resultProduct.getType() + ", unitcode: " + resultProduct.getunitcode() + ", quantity: " + resultProduct.getAvailableQuantity() + ", price: " + resultProduct.getPrice() + ", costprice: " + resultProduct.getCostPrice());
            }
            return;
        }
        else if(arguments.length==4)
        {
            int pageLength=0;
            if(arguments[2].equals("-p"))
            {   try {
                pageLength = Integer.parseInt(arguments[3]);
            }
            catch(Exception e)
            {
                System.out.println(">> Invalid page Size input");
                System.out.println(">> Try \"product list help\" for proper syntax");
            }
                listAttributesMap.put("Pagelength", String.valueOf(pageLength));
                listAttributesMap.put("Pagenumber",String.valueOf(1));
                resultList=listService.listProductService(listAttributesMap);
                for(Product resultProduct:resultList)
                {
                    System.out.println(">> id: " + resultProduct.getId() + ", code: " + resultProduct.getCode() + ", name: " + resultProduct.getName() + ", type: " + resultProduct.getType() + ", unitcode: " + resultProduct.getunitcode() + ", quantity: " + resultProduct.getAvailableQuantity() + ", price: " + resultProduct.getPrice() + ", costprice: " + resultProduct.getCostPrice());
                }
            }
            else if(arguments[2].equals("-s"))
            {
                String searchText=arguments[3].trim();
                listAttributesMap.put("Searchtext",searchText);
                resultList=listService.listProductService(listAttributesMap);
                if(resultList.size()==0)
                {
                    System.out.println(">> Given SearchText does not exist!!!");
                }
                for(Product resultProduct:resultList)
                {
                    System.out.println(">> id: " + resultProduct.getId() + ", code: " + resultProduct.getCode() + ", name: " + resultProduct.getName() + ", type: " + resultProduct.getType() + ", unitcode: " + resultProduct.getunitcode() + ", quantity: " + resultProduct.getAvailableQuantity() + ", price: " + resultProduct.getPrice() + ", costprice: " + resultProduct.getCostPrice());
                }
            }
            else
            {
                System.out.println(">> Invalid Extension given");
                System.out.println(">> Try \"product list help\" for proper syntax");
            }
        }
        else if(arguments.length==5)
        {
            int pageLength=0;
            int pageNumber=0;
            if(arguments[2].equals("-p"))
            {
                try{
                    pageLength=Integer.parseInt(arguments[3]);
                    pageNumber=Integer.parseInt(arguments[4]);
                }
                catch(Exception e)
                {
                    System.out.println(">> Invalid page Size (or) page Number input");
                    System.out.println(">> Try \"product list help\" for proper syntax");
                }
                listAttributesMap.put("Pagelength", String.valueOf(pageLength));
                listAttributesMap.put("Pagenumber", String.valueOf(pageNumber));
                try{
                    resultList=listService.listProductService(listAttributesMap);
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                    return;
                }
                for(Product resultProduct:resultList)
                {
                    System.out.println(">> id: " + resultProduct.getId() + ", code: " + resultProduct.getCode() + ", name: " + resultProduct.getName() + ", type: " + resultProduct.getType() + ", unitcode: " + resultProduct.getunitcode() + ", quantity: " + resultProduct.getAvailableQuantity() + ", price: " + resultProduct.getPrice() + ", costprice: " + resultProduct.getCostPrice());
                }
            }
            else if(arguments[2].equals("-s"))
            {
                List<String> productAttributes= Arrays.asList("id","code","name","unitcode","type","price","stock","costprice");
                String attribute=arguments[3];
                attribute=attribute.replace(":","");
                String searchText=arguments[4];
                if(productAttributes.contains(attribute))
                {
                    listAttributesMap.put("Attribute",attribute);
                    listAttributesMap.put("Searchtext",searchText);
                    listAttributesMap.put("Pagelength","20");
                    listAttributesMap.put("Pagenumber",String.valueOf(1));
                    resultList=listService.listProductService(listAttributesMap);
                    if(resultList==null)
                    {
                        System.out.println(">>Given SearchText does not exist!!!");
                        return;
                    }
                    for(Product resultProduct:resultList)
                    {
                        System.out.println(">> id: " + resultProduct.getId() + ", code: " + resultProduct.getCode() + ", name: " + resultProduct.getName() + ", type: " + resultProduct.getType() + ", unitcode: " + resultProduct.getunitcode() + ", quantity: " + resultProduct.getAvailableQuantity() + ", price: " + resultProduct.getPrice() + ", costprice: " + resultProduct.getCostPrice());
                    }
                }
                else
                {
                    System.out.println("Given attribute is not a searchable attribute!!");
                    System.out.println("Try \"product list help\" for proper syntax");
                }
                return;

            }
            else {
                System.out.println(">> Invalid Extension given");
                System.out.println(">> Try \"product list help\" for proper syntax");
            }
        }
        else if(arguments.length==7)
        {
            if(arguments[2].equals("-s"))
            {
                List<String> productAttributes= Arrays.asList("id","code","name","unitcode","type","price","stock","costprice");
                String attribute=arguments[3];
                attribute=attribute.replace(":","");
                String searchText=arguments[4];
                listAttributesMap.put("Attribute",attribute);
                listAttributesMap.put("Searchtext",searchText);
                if(productAttributes.contains(attribute))
                {
                    int pageLength=0;
                    if(arguments[5].equals("-p"))
                    {
                        try{
                            pageLength=Integer.parseInt(arguments[6]);
                        }
                        catch(Exception e)
                        {
                            System.out.println(">> Invalid page Size input");
                            System.out.println(">> Try \"product list help\" for proper syntax");
                            return;
                        }
                        listAttributesMap.put("Pagelength",String.valueOf(pageLength));
                        System.out.println(listAttributesMap.get("Pagelength"));
                        listAttributesMap.put("Pagenumber","1");
                        resultList=listService.listProductService(listAttributesMap);
                        if(resultList==null)
                        {
                            System.out.println(">>Given SearchText does not exists");
                            return;
                        }
                        for(Product resultProduct:resultList)
                        {
                            System.out.println(">> id: " + resultProduct.getId() + ", code: " + resultProduct.getCode() + ", name: " + resultProduct.getName() + ", type: " + resultProduct.getType() + ", unitcode: " + resultProduct.getunitcode() + ", quantity: " + resultProduct.getAvailableQuantity() + ", price: " + resultProduct.getPrice() + ", costprice: " + resultProduct.getCostPrice());
                        }
                    }
                    else
                    {
                        System.out.println(">> Invalid Command Extension format !!!");
                        System.out.println("Try \"product list help\" for proper syntax");
                        return;
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
        else if(arguments.length==8)
        {
            if(arguments[2].equals("-s"))
            {
                List<String> productAttributes= Arrays.asList("id","code","name","unitcode","type","price","stock","costprice");
                String attribute=arguments[3];
                attribute=attribute.replace(":","");
                String searchText=arguments[4];
                int pageLength=0;
                int pageNumber=0;
                listAttributesMap.put("Attribute",attribute);
                listAttributesMap.put("Searchtext",searchText);
                if(productAttributes.contains(attribute))
                {
                    if(arguments[5].equals("-p"))
                    {
                        try{
                            pageLength=Integer.parseInt(arguments[6]);
                            pageNumber=Integer.parseInt(arguments[7]);
                        }
                        catch(Exception e)
                        {
                            System.out.println(">> Invalid page Size (or) page Number input");
                            System.out.println(">> Try \"product list help\" for proper syntax");
                        }
                        listAttributesMap.put("Pagelength", String.valueOf(pageLength));
                        listAttributesMap.put("Pagenumber", String.valueOf(pageNumber));
                        resultList= listService.listProductService(listAttributesMap);
                        if(resultList==null)
                        {
                            System.out.println(">>Given SearchText does not exist!!!");
                            return;
                        }
                        for(Product resultProduct:resultList)
                        {
                            System.out.println(">> id: " + resultProduct.getId() + ", code: " + resultProduct.getCode() + ", name: " + resultProduct.getName() + ", type: " + resultProduct.getType() + ", unitcode: " + resultProduct.getunitcode() + ", quantity: " + resultProduct.getAvailableQuantity() + ", price: " + resultProduct.getPrice() + ", costprice: " + resultProduct.getCostPrice());
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

    }
    public void productCountCLI(String[] arguments) throws ApplicationErrorException {
        ProductService countProduct=new ProductServiceImplementation();
        int productCount=countProduct.countProductService();
        System.out.println(">> ProductCount "+productCount);

    }
    public void productEditCLI(String[] arguments){
        attributeMap.put("id",null);
        attributeMap.put("code",null);
        attributeMap.put("name",null);
        attributeMap.put("unitcode",null);
        attributeMap.put("type",null);
        attributeMap.put("price",null);
        Scanner scanner=new Scanner(System.in);
        ProductService productEdit=new ProductServiceImplementation();
        if(arguments.length==3&&arguments[2].equals("help"))
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
            return;
        }
        else if(arguments.length==2)
        {
            System.out.print("> ");
            String paramaters=scanner.nextLine();
            String[] productAttributes=paramaters.split("\\,");
            if(productAttributes[0].contains("id")) {
                for (String attribute : productAttributes) {
                    if (attribute.contains("id")) {
                        String[] keyValues = attribute.split("\\:");
                        attributeMap.put("id", keyValues[1].trim());
                    }
                    else if (attribute.contains("name")) {
                        String[] keyValues = attribute.split("\\:");
                        attributeMap.put("name", keyValues[1].trim());
                    }
                    else if (attribute.contains("unitcode")) {
                        String[] keyValues = attribute.split("\\:");
                        attributeMap.put("unitcode", keyValues[1].trim());
                    }
                    else if (attribute.contains("code")&&!attribute.contains("unitcode")) {
                        String[] keyValues = attribute.split("\\:");
                        attributeMap.put("code", keyValues[1].trim());
                    }
                    else if (attribute.contains("type")) {
                        String[] keyValues = attribute.split("\\:");
                        attributeMap.put("type", keyValues[1].trim());
                    } else if (attribute.contains(("price"))) {
                        String[] keyValues = attribute.split("\\:");
                        attributeMap.put("price", keyValues[1].trim());
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
                    return;
                }
                int id=0;
                try{
                    id=Integer.parseInt(attributeMap.get("id").trim());
                }
                catch(Exception e)
                {
                    System.out.println(">> Id must be a number");
                }
                int statusCode= 0;
                try {
                    statusCode = productEdit.editProductService(attributeMap);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
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
            }
        }
        else if(arguments.length>14)
        {
            System.out.println(">>Too many Arguments for command \"product edit\"");
        }
        else if(arguments.length<6)
        {
            System.out.println(">>Insufficient Arguments for command \"product edit\"");
        }
        else if(!arguments[2].contains("id"))
        {
            System.out.println(">> Id is a Mandatory argument for every Edit operation");
            System.out.println(">> For every Edit operation the first argument must be product's ID");
            System.out.println(">> Try \"product edit help\" for proper syntax");
        }
        else{
            attributeMap.put("id",arguments[3]);
            if(attributeMap.get("id").equals("")) {
                System.out.println(">> Id should not be null");
                System.out.println(">> Try \"product edit help\" for proper Syntax");
                return;
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
                    attributeMap.put("name",arguments[index+1]);
                }
                else if(arguments[index].contains("code")&&!arguments[index].contains("unitcode"))
                {

                    attributeMap.put("code",arguments[index+1]);
                }
                else if(arguments[index].contains("unitcode"))
                {

                    attributeMap.put("unitcode",arguments[index+1]);
                }
                else if(arguments[index].contains("type"))
                {
                    attributeMap.put("type",arguments[index+1]);
                }
                else if(arguments[index].contains("price"))
                {
                    attributeMap.put("price",arguments[index+1]);
                }
                else {
                    System.out.println(">> Invalid attribute given!!! : "+arguments[index]);
                    System.out.println(">> Try \"product edit help\" for proper syntax");
                    break;
                }
            }

            int statusCode= 0;
            try {
                statusCode = productEdit.editProductService(attributeMap);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
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
    }
    public void productDeleteCLI(String[] arguments) throws ApplicationErrorException {
        Scanner scanner=new Scanner(System.in);
        ProductService deleteProduct=new ProductServiceImplementation();
        String numberRegex="^[0-9]*$";
        String procodeRegex="^[a-zA-Z0-9]{2,6}$";
        if(arguments.length==3)
        {
            if(arguments[2].equals("help"))
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
            else if(arguments[2].matches(numberRegex))
            {
                System.out.println(">> Are you sure want to delete the product y/n ? : ");
                String prompt=scanner.nextLine();
                if(prompt.equals("y")) {
                    if(deleteProduct.deleteProductService(arguments[2])==1)
                    {
                        System.out.println("Product Deletion Successfull!!!");
                    }
                    else if(deleteProduct.deleteProductService(arguments[2])==-1)
                    {
                        System.out.println(">> Product Deletion Failed!!!");
                        System.out.println(">> Please check the Id  you have entered!!!");
                        System.out.println("Try \"product delete help\" for proper syntax");
                    }
                    else if(deleteProduct.deleteProductService(arguments[2])==0)
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
                System.out.println(">> Invalid format for id!!!");
                System.out.println("Try \"product delete help\" for proper syntax");
            }
        }
        else if(arguments.length==4 && arguments[2].equals("-c"))
        {
            if(arguments[3].matches(procodeRegex))
            {
                System.out.println(">> Are you sure want to delete the product y/n ? : ");
                String prompt=scanner.nextLine();
                if(prompt.equals("y")) {
                    if(deleteProduct.deleteProductService(arguments[3])==1)
                    {
                        System.out.println(">> Product Deletion Successfull!!!");
                    }
                    else if(deleteProduct.deleteProductService(arguments[3])==-1)
                    {
                        System.out.println(">> Product Deletion Failed!!!");
                        System.out.println(">> Please check the  Code that you have entered!!!");
                        System.out.println("Try \"product delete help\" for proper syntax");
                    }
                    else if(deleteProduct.deleteProductService(arguments[3])==0)
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
    }
}
