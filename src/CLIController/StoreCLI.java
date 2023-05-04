package CLIController;

import DAO.ApplicationErrorException;
import Entity.Store;
import Service.StoreService;
import Service.StoreServiceImplementation;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

public class StoreCLI {
    HashMap<String,String> attributeMap=new HashMap<>();
    public void storeCreateCLI(String[] arguments) throws SQLException, ApplicationErrorException {
        Scanner scanner=new Scanner(System.in);
        if(arguments.length==3&&arguments[2].equals("help"))
        {
            System.out.println(">> Create store using the following template,\n" +
                    "     name, phone number, address, gst number\n" +
                    " \n" +
                    "\tname  - text, mandatory with 3 to 30 chars\t\n" +
                    "\tphone - number, mandatory, ten digits, digit start with 9/8/7/6\n" +
                    "\taddress - text, mandatory\n" +
                    "\tgst number - text, 15 digit, mandatory");
            return;
        }
        else if(arguments.length==2)
        {
            System.out.print("> ");
            String parameters=scanner.nextLine();
            String[] storeAttributes=parameters.split("\\,");
            if(storeAttributes.length<4)
            {
                System.out.println(">> Insufficient arguments for command \"store create\"");
                System.out.println(">> Try \"store create help\" for proper syntax");
                return;
            }
            if(storeAttributes.length>4)
            {
                System.out.println(">> Too many arguments for command \"store create\"");
                System.out.println(">> Try \"store create help\" for proper syntax");
                return;
            }
            String name=storeAttributes[0].trim();
            long phonenumber=0L;
            String address=storeAttributes[2].trim();
            int gstnumber=0;
            try{
                phonenumber= Long.parseLong(storeAttributes[1].trim());
            }
            catch(Exception e)
            {
                System.out.println(">> Invalid format for 2nd argument \"phonenumber\"");
                System.out.println(">> Try \"store create help\" for proper syntax");
                return;
            }
            try{
                gstnumber=Integer.parseInt(storeAttributes[3].trim());
            }
            catch (Exception e)
            {
                System.out.println(">> Invalid format for 4th argument \"gstnumber\"");
                System.out.println(">> Try \"store create help\" for proper syntax");
                return;
            }
            Store store=new Store(name,phonenumber,address,gstnumber);
            StoreService storeCreateService=new StoreServiceImplementation();
            int resultCode;
            try{
                resultCode=storeCreateService.createStoreService(store);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                return;
            }
            if(resultCode==1)
            {
                System.out.println(">> Store Created Successfully!!!");
            }
            else if(resultCode==-1)
            {
                System.out.println(">> Store Creation failed!!!");
                System.out.println(">> Store Already Exists!!!");
            }
            return;
        }
        else if(arguments.length<6)
        {
            System.out.println(">> Insufficient Arguments for command \"store create\"");
            System.out.println(">> Try \"store create help\" for proper syntax");
            return;
        }
        else if(arguments.length>6)
        {
            System.out.println(">> Too many Arguments for command \"store create\"");
            System.out.println(">> Try \"store create help\" for proper syntax");
            return;
        }
        String name=arguments[2].trim();
        String address=arguments[4].trim();
        long phonenumber;
        int gstnumber;
        try{
            phonenumber=Long.parseLong(arguments[3].trim());
            gstnumber=Integer.parseInt(arguments[5].trim());
        }
        catch(Exception e)
        {
            System.out.println(">> Invalid phone number (or) gstnumber format!!!");
            System.out.println(">> Try \"store create help\" for proper syntax");
            return;
        }
        Store store=new Store(name,phonenumber,address,gstnumber);
        StoreService storeCreateService=new StoreServiceImplementation();
        int resultCode=1;
        try{
            resultCode=storeCreateService.createStoreService(store);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return;
        }
        if(resultCode==1)
        {
            System.out.println(">> Store Created Successfully!!!");
        }
        else if(resultCode==-1)
        {
            System.out.println(">> Store Creation failed!!!");
            System.out.println(">> Store Already Exists!!!");
        }
    }
    public void storeEditCLI(String[] arguments)
    {
        attributeMap.put("name",null);
        attributeMap.put("phonenumber",null);
        attributeMap.put("address",null);
        attributeMap.put("gstnumber",null);
        Scanner scanner=new Scanner(System.in);
        StoreService storeEditService=new StoreServiceImplementation();
        if(arguments.length==3&&arguments[2].equals("help"))
        {
            System.out.println(">> Edit store uing the following template\t\n" +
                    "\n" +
                    "name, phone number, address, gst number\n" +
                    " \n" +
                    "\tname  - text, mandatory with 3 to 30 chars\t\n" +
                    "\tphone - number, mandatory, ten digits, digit start with 9/8/7/6\n" +
                    "\taddress - text, mandatory\n" +
                    "\tgst number - text, 15 digit, mandatory");
            return;
        }
        else if(arguments.length==2)
        {
            System.out.print("> ");
            String parameters=scanner.nextLine();
            String[] storeAttributes=parameters.split("\\,");
             for(String attribute: storeAttributes)
                {

                    if(attribute.contains("name")){
                        String[] keyValues=attribute.split("\\:");
                        attributeMap.put("name",keyValues[1].trim());
                    }
                    else if(attribute.contains("phonenumber")){
                        String[] keyValues=attribute.split("\\:");
                        attributeMap.put("phonenumber",keyValues[1].trim());
                    }
                    else if(attribute.contains("address")){
                        String[] keyValues=attribute.split("\\:");
                        attributeMap.put("address",keyValues[1].trim());
                    }
                    else if(attribute.contains("gstnumber")){
                        String[] keyValues=attribute.split("\\:");
                        attributeMap.put("gstnumber",keyValues[1].trim());
                    }
                    else{
                        System.out.println(">> Invalid attribute given!!!: "+attribute);
                        System.out.println(">> Try \"store edit help\" for proper syntax");
                        break;
                    }
                }
             int statusCode;
             try{
                 statusCode=storeEditService.editStoreService(attributeMap);
             }
             catch (Exception e)
             {
                 System.out.println(e.getMessage());
                 return;
             }
            if(statusCode==1)
            {
                System.out.println(">> Store Edited Successfully!!!");
            }
            else if(statusCode==-1){
                System.out.println(">> Store Edit Failed!!!");
                System.out.println(">> No store Exists!!!");
            }
            else if(statusCode==0)
            {
                System.out.println(">> Invalid format of attributes given for edit Command!!!");
                System.out.println(">> Try \"store edit help\" for proper syntax!!!");
            }

        }
        else if(arguments.length>10)
        {
            System.out.println(">> Too many Arguments for command \"store edit\"");
            System.out.println(">> Try \"store edit help\" for proper syntax");
        }
        else if(arguments.length<4)
        {
            System.out.println(">> Insufficient arguments for command \"store edit\"");
            System.out.println(">> Try \"store edit help\" for proper syntax");
        }
        else{
            for(int index=2;index<arguments.length;index=index+2) {
                if (arguments[index].contains("name")) {
                    attributeMap.put("name", arguments[index + 1]);
                } else if (arguments[index].contains("phonenumber")) {
                    attributeMap.put("phonenumber", arguments[index + 1]);
                } else if (arguments[index].contains("address")) {
                    attributeMap.put("address", arguments[index + 1]);
                } else if (arguments[index].contains("gstnumber")) {
                    attributeMap.put("gstnumber", arguments[index + 1]);
                }
                else{
                    System.out.println(">> Invalid attribute given!!!: "+arguments[index]);
                    System.out.println(">> Try \"store edit help\" for proper Syntax");
                    break;
                }
            }
            int statusCode;
            try{
                statusCode=storeEditService.editStoreService(attributeMap);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                return;
            }
            if(statusCode==1)
            {
                System.out.println(">> Store Edited Successfully!!!");
            }
            else if(statusCode==-1)
            {
                System.out.println(">> Store Edit failed!!!");
                System.out.println(">> Please check the name you have entered!!!");
            }
            else if(statusCode==0)
            {
                System.out.println(">> Invalid format of attributes for edit command!!!");
                System.out.println(">> Try \"store edit help:\" for proper syntax!!!");
            }
        }
    }
    public void storeDeleteCLI(String[] arguments) throws ApplicationErrorException {
        Scanner scanner=new Scanner(System.in);
        StoreService storeDeleteService=new StoreServiceImplementation();
        if(arguments.length==3&&arguments[2].equals("help"))
        {
                System.out.println(">> delete store using the following template\n" +
                        "\tstore delete \n"
                        );

        }
        else if(arguments.length==2){
            System.out.print("Are you sure want to delete the Store? This will delete all you product/purchase/sales data y/n ? : ");
            String prompt=scanner.nextLine();
            if(prompt.equals("y"))
            {
                System.out.print(">> Enter admin password to delete the store: ");
                String password=scanner.nextLine();
                int resultCode=storeDeleteService.deleteStoreService(password);
                if(resultCode==1)
                {
                    System.out.println(">> Store deleted Successfully !!! GOOD BYE !");
                }
                else if(resultCode==-1){
                    System.out.println(">> Unable to delete Store!");
                    System.out.println(">> Invalid Admin Password");
                }
                else if(resultCode==0)
                {
                    System.out.println();
                }
            }
            else if(prompt.equals("n"))
            {
                System.out.println(">> Delete operation cancelled!!!");
            }
            else {
                System.out.println(">> Invalid prompt!! Please select between y/n:");
            }

        }
        else{
            System.out.println(">> Invalid Command!!!");
            System.out.println("Try \"store delete help\" for proper syntax!!!");
        }
    }
}
