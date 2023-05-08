package CLIController;

import DAO.ApplicationErrorException;
import Entity.Unit;
import Entity.User;
import Service.UnitService;
import Service.UnitServiceImplementation;

import java.security.spec.ECField;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class UnitCLI {
    HashMap<String,String> attributeMap=new HashMap<>();
    public void unitCreateCLI(String[] arguments){
        Scanner scanner=new Scanner(System.in);
        String nameRegex="^[a-zA-Z\\s]{3,30}$";
        String codeRegex="^[a-zA-Z]{2,4}$";
        if(arguments.length==3&&arguments[2].equals("help"))
        {
            System.out.println(">> Create unit using the following template,\n" +
                    "     name, code, description, isdividable\n" +
                    "     \n" +
                    "     name - text, mandatory with 3 to 30 chars\t\n" +
                    "     code - text, maximum 4 char, mandatory\n" +
                    "     description - text\n" +
                    "     isdividable - boolean, mandatory\n" +
                    "    ");
            return;
        }
        else if(arguments.length==2)
        {
            System.out.print("> ");
            String parameters=scanner.nextLine();
            String[] unitAttributes=parameters.split("\\,");
            if(unitAttributes.length<4)
            {
                System.out.println(">> Insufficient arguments for command \"unit create\"");
                System.out.println(">> Try \"unit create help\" for proper syntax");
                return;
            }
            if(unitAttributes.length>4)
            {
                System.out.println(">> Too many arguments for command \"unit create\"");
                System.out.println(">> Try \"unit create help\" for proper syntax");
                return;
            }
            String name=unitAttributes[0].trim();
            String unitCode=unitAttributes[1].trim();
            String description=unitAttributes[2].trim();
            boolean isDividable=true;
            try{
                isDividable=Boolean.parseBoolean(unitAttributes[3].trim());
            }
            catch(Exception e)
            {
                System.out.println(">> Invalid format for 4th argument \"Isdividable\"");
                System.out.println(">> Try \"unit create help\" for proper syntax");
                return;
            }
            Unit unit=new Unit(name,unitCode,description,isDividable);
            UnitService unitCreateService=new UnitServiceImplementation();
            int resultCode=1;
            try{
                resultCode=unitCreateService.createUnitService(unit);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                return;
            }
            if(resultCode==1)
            {
                System.out.println(">> Unit Created Successfully!!!");
            }
            else if (resultCode==-1)
            {
                System.out.println(">> Unit Creation Failed!!!");
                System.out.println(">> The unitCode you have entered already exists!!!");
            }
            return;
        }
        else if(arguments.length<6)
        {
            System.out.println(">> Insufficient Arguments for command \"unit create\"");
            System.out.println(">> Try \"unit create help\" for proper syntax");
            return;
        }
        else if(arguments.length>6)
        {
            System.out.println(">> Too many Arguments for command \"unit create\"");
            System.out.println(">> Try \"unit create help\" for proper syntax");
            return;
        }
        String name=arguments[2].trim();
        String unitCode=arguments[3].trim();
        String description=arguments[4].trim();
        boolean isDividable=true;
        try{
            isDividable=Boolean.parseBoolean(arguments[5].trim());
        }
        catch (Exception e)
        {
            System.out.println(">> Invalid format for 4th argument \"Isdividable\"");
            System.out.println(">> Try \"unit create help\" for proper syntax");
            return;
        }
        Unit unit=new Unit(name,unitCode,description,isDividable);
        UnitService unitCreateService=new UnitServiceImplementation();
        int resultcode=1;
        try{
            resultcode=unitCreateService.createUnitService(unit);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return;
        }
        if(resultcode==1)
        {
            System.out.println(">> Unit Created Successfully!!!");
        }
        else if(resultcode==-1)
        {
            System.out.println(">> Unit Creation failed!!!");
            System.out.println("The unitCode you have entered already Exists!!!");
        }
    }
    public void unitListCLI(String[] arguments) throws ApplicationErrorException {
    UnitService unitListService=new UnitServiceImplementation();
    List<Unit> unitList;
    if(arguments.length==3&&arguments[2].equals("help"))
    {
        System.out.println(">> List unit with the following options\n" +
                ">> unit list - will list all the units");
        return;
    }
    else if(arguments.length==2)
    {
        unitList=unitListService.listUnitService();
        for(Unit unit:unitList)
        {
            System.out.println(">> id: "+unit.getId()+", name: "+unit.getName()+", code: "+unit.getCode()+", description: "+unit.getDescription()+", isdividable: "+unit.getIsDividable());
        }
    }
    else{
        System.out.println(">> Invalid command format!!!");
        System.out.println(">> Try \"unit list help\" for proper syntax");
    }
    }
    public void unitEditCLI(String[] arguments){
        attributeMap.put("id",null);
        attributeMap.put("name",null);
        attributeMap.put("unitcode",null);
        attributeMap.put("description",null);
        attributeMap.put("isdividable",null);
        Scanner scanner=new Scanner(System.in);
        UnitService unitEditService=new UnitServiceImplementation();
        if(arguments.length==3&&arguments[2].equals("help"))
        {
            System.out.println(">> Edit unit using the following template\n" +
                    "id: <id - 6>, name: <name-edited>, code: <code>,  description: <description>, isdividable: <isdividable>\n" +
                    "\n" +
                    ">> You can also restrict the user data by editable attributes. Id attribute is mandatory for all the edit operation.\n" +
                    ">> id: <id - 6>, name: <name>, code: <code>\n" +
                    "\n" +
                    ">> You can not give empty or null values to the mandatory attributes.\n" +
                    ">> id: <id - 6>, name: , code: null\n" +
                    "\n" +
                    "\t\t name - text, mandatory with 3 to 30 chars\t\n" +
                    "     code - text, maximum 4 char, mandatory\n" +
                    "     description - text\n" +
                    "     isdividable - boolean, mandatory");
            return;
        }
        else if(arguments.length==2)
        {
            System.out.print("> ");
            String parameters=scanner.nextLine();
            String[] unitAttributes=parameters.split("\\,");
            if(unitAttributes.length<2)
            {
                System.out.println(">> Insufficient arguments for edit Operation");
                System.out.println(">> Try \"unit edit help\" for proper syntax");
                return;
            }
            if(unitAttributes.length>5)
            {
                System.out.println(">> Too manu arguments for edit Operation!!");
                System.out.println(">> Try \"unit edit help\" for proper syntax!!!");
                return;
            }
            if(unitAttributes[0].contains("id"))
            {
                for(String attribute: unitAttributes)
                {
                    if(attribute.contains("id")&&!attribute.equals("isdividable")){
                        String[] keyValues=attribute.split("\\:");
                        attributeMap.put("id",keyValues[1].trim());
                    }
                    else if(attribute.contains("name")){
                        String[] keyValues=attribute.split("\\:");
                        attributeMap.put("name",keyValues[1].trim());
                    }
                    else if(attribute.contains("code")){
                        String[] keyValues=attribute.split("\\:");
                        attributeMap.put("unitcode",keyValues[1].trim());
                    }
                    else if(attribute.contains("description")){
                        String[] keyValues=attribute.split("\\:");
                        attributeMap.put("description",keyValues[1].trim());
                    }
                    else if(attribute.contains("isdividable")){
                        String[] keyValues=attribute.split("\\:");
                        attributeMap.put("isdividable",keyValues[1].trim());
                    }
                    else{
                        System.out.println(">> Invalid attribute given!!!: "+attribute);
                        System.out.println(">> Try \"unit edit help\" for proper syntax");
                        break;
                    }
                }
                if(attributeMap.get("id").equals(""))
                {
                    System.out.println("Id Should not be null!!!");
                    System.out.println(">> Try \"unit edit help\" for proper syntax");
                    return;
                }
                int id=0;
                try{
                    id=Integer.parseInt(attributeMap.get("id").trim());
                }
                catch(Exception e)
                {
                    System.out.println(attributeMap.get("id"));
                    System.out.println(">> Id must be a number");
                    return;
                }
                int statusCode=0;
                try{
                    statusCode =unitEditService.editUnitService(attributeMap);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    return;
                }
                if(statusCode==1)
                {
                    System.out.println(">> Unit Edited Successfully!!!");
                }
                else if(statusCode==-1){
                    System.out.println(">> Unit Edit Failed!!!");
                    System.out.println(">> Please check the Id you have entered");
                }
                else if(statusCode==0)
                {
                    System.out.println(">> Invalid format of attributes given for edit Command!!!");
                    System.out.println(">> Try \"unit edit help\" for proper syntax!!!");
                }
            }
            else {
                System.out.println(">> Id is a Mandatory argument for every Edit operation");
                System.out.println(">> For every Edit operation the first argument must be unit's ID");
                System.out.println(">> Try \"unit edit help\" for proper syntax");
            }
        }
        else if(arguments.length>12)
        {
            System.out.println(">> Too many Arguments for command \"unit edit\"");
            System.out.println(">> Try \"unit edit help\" for proper syntax");
        }
        else if(arguments.length<6)
        {
            System.out.println(">> Insufficient arguments for command \"unit edit\"");
            System.out.println(">> Try \"unit edit help\" for proper syntax");
        }
        else if(!arguments[2].contains("id"))
        {
            System.out.println(">> Id is a Mandatory argument for every Edit operation");
            System.out.println(">> For every Edit operation the first argument must be unit's ID");
            System.out.println(">> Try \"unit edit help\" for proper syntax");
        }
        else{
            attributeMap.put("id",arguments[3].trim());
            if(attributeMap.get("id").equals("")){
                System.out.println(">> Id should not be null");
                System.out.println(">> Try \"unit edit help\" for proper Syntax");
                return;
            }
            int id=0;
            try{
                id=Integer.parseInt(attributeMap.get("id").trim());
            }
            catch (Exception e)
            {
                System.out.println(">> Id must be a number");
                System.out.println(">> Please Try \"unit edit help\" for proper Syntax");
            }
            for(int index=4;index<arguments.length;index=index+2) {
                if (arguments[index].contains("name")) {
                    attributeMap.put("name", arguments[index + 1]);
                } else if (arguments[index].contains("code")) {
                    attributeMap.put("unitcode", arguments[index + 1]);
                } else if (arguments[index].contains("description")) {
                    attributeMap.put("description", arguments[index + 1]);
                } else if (arguments[index].contains("isdividable")) {
                    attributeMap.put("isdividable", arguments[index + 1]);
                }
                else{
                    System.out.println(">> Invalid attribute given!!!: "+arguments[index]);
                    System.out.println(">> Try \"unit edit help\" for proper Syntax");
                    break;
                }
            }
            int statusCode;
            try{
                statusCode=unitEditService.editUnitService(attributeMap);
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                return;
            }
            if(statusCode==1)
            {
                System.out.println(">> Unit Edited Successfully!!!");
            }
            else if(statusCode==-1)
            {
                System.out.println(">> Unit Edit failed!!!");
                System.out.println(">> Please check the Id you have entered!!!");
            }
            else if(statusCode==0)
            {
                System.out.println(">> Invalid format of attributes for edit command!!!");
                System.out.println(">> Try \"unit edit help:\" for proper syntax!!!");
            }
        }
    }
    public void unitDeleteCLI(String[] arguments) throws ApplicationErrorException {
        Scanner scanner=new Scanner(System.in);
        UnitService unitDeleteService=new UnitServiceImplementation();
        String codeRegex="^[a-zA-Z]{1,4}$";
        if(arguments.length==3)
        {
            if(arguments[2].equals("help"))
            {
                System.out.println(">> delete unit using the following template\n" +
                        "\t \tcode\n" +
                        "\t \n" +
                        "\t  code - text, min 3 - 30 char, mandatory,existing\n");
            }
            else if(arguments[2].matches(codeRegex))
            {
                System.out.print(">> Are you Sure you want to delete the Unit y/n :");
                String prompt=scanner.nextLine();
                if(prompt.equals("y"))
                {
                    if(unitDeleteService.deleteUnitService(arguments[2])==1)
                    {
                        System.out.println(">> Unit deleted Successfully!!!");
                    }
                    else if(unitDeleteService.deleteUnitService(arguments[2])==-1)
                    {
                        System.out.println(">> Unit deletion failed!!!");
                        System.out.println(">> Please check the unitcode you have entered!!!");
                        System.out.println("Try \"unit delete help\" for proper syntax");
                    }
                }
                else if(prompt.equals("n"))
                {
                    System.out.println(">> Delete operation cancelled");
                }
                else{
                    System.out.println("Invalid delete prompt!!! Please select between y/n");
                }
            }
            else {
                System.out.println(">> Invalid format for unitCode!!!");
                System.out.println("Try \"unit delete help\" for proper syntax");
            }

        }

    }
}
