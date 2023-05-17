package CLIController;

import DAO.ApplicationErrorException;
import Entity.Unit;
import Service.UnitService;
import Service.UnitServiceImplementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class UnitCLI {
  private int id;
  private String name;
  private String unitcode;
  private String description;
  private boolean isDividable;
  private UnitService unitService = new UnitServiceImplementation();
  private final Scanner scanner = new Scanner(System.in);

  public void unitCreateCLI(List<String> arguments) {
    if (arguments.size() == 3 && arguments.get(2).equals("help")) {
      System.out.println(
          ">> Create unit using the following template,\n"
              + "     name, code, description, isdividable\n"
              + "     \n"
              + "     name - text, mandatory with 3 to 30 chars\t\n"
              + "     code - text, maximum 4 char, mandatory\n"
              + "     description - text\n"
              + "     isdividable - boolean, mandatory\n"
              + "    ");
      return;
    } else if (arguments.size() == 2) {
      System.out.print("> ");
      String parameters = scanner.nextLine();
      List<String> unitAttributes = List.of(parameters.split("\\,"));
      createHelper(unitAttributes);
      return;
    }
    createHelper(arguments.subList(2, arguments.size()));
  }

  private void createHelper(List<String> unitAttributes) {
    if (unitAttributes.size() < 4) {
      System.out.println(">> Insufficient arguments for command \"unit create\"");
      System.out.println(">> Try \"unit create help\" for proper syntax");
      return;
    }
    if (unitAttributes.size() > 4) {
      System.out.println(">> Too many arguments for command \"unit create\"");
      System.out.println(">> Try \"unit create help\" for proper syntax");
      return;
    }
    name = unitAttributes.get(0).trim();
    unitcode = unitAttributes.get(1).trim();
    description = unitAttributes.get(2).trim();
    isDividable = true;
    try {
      isDividable = Boolean.parseBoolean(unitAttributes.get(3).trim());
    } catch (Exception e) {
      System.out.println(">> Invalid format for 4th argument \"Isdividable\"");
      System.out.println(">> Try \"unit create help\" for proper syntax");
      return;
    }
    Unit unit = new Unit(name, unitcode, description, isDividable);
    Unit createdUnit;
    try {
      createdUnit = unitService.createUnitService(unit);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return;
    }
    if (createdUnit == null) {
      System.out.println(">> Template Mismatch!!");
    } else if (createdUnit != null) {
      System.out.println("Unit creation Succesful!!");
      System.out.println(unit);
    }
  }

  public void unitListCLI(List<String> arguments) throws ApplicationErrorException {
    List<Unit> unitList;
    if (arguments.size() == 3 && arguments.get(2).equals("help")) {
      System.out.println(
          ">> List unit with the following options\n" + ">> unit list - will list all the units");

    } else if (arguments.size() == 2) {
      unitList = unitService.listUnitService();
      for (Unit unit : unitList) {
        System.out.println(
            ">> id: "
                + unit.getId()
                + ", name: "
                + unit.getName()
                + ", code: "
                + unit.getCode()
                + ", description: "
                + unit.getDescription()
                + ", isdividable: "
                + unit.getIsDividable());
      }
    } else {
      System.out.println(">> Invalid command format!!!");
      System.out.println(">> Try \"unit list help\" for proper syntax");
    }
  }

  public void unitEditCLI(List<String> arguments,String command) {
    final String editCommandRegex="^id:\\s*(\\d+)(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?$";
    if (arguments.size() == 3 && arguments.get(2).equals("help")) {
      System.out.println(
          ">> Edit unit using the following template\n"
              + "id: <id - 6>, name: <name-edited>, code: <code>,  description: <description>, isdividable: <isdividable>\n"
              + "\n"
              + ">> You can also restrict the user data by editable attributes. Id attribute is mandatory for all the edit operation.\n"
              + ">> id: <id - 6>, name: <name>, code: <code>\n"
              + "\n"
              + ">> You can not give empty or null values to the mandatory attributes.\n"
              + ">> id: <id - 6>, name: , code: null\n"
              + "\n"
              + "\t\t name - text, mandatory with 3 to 30 chars\t\n"
              + "     code - text, maximum 4 char, mandatory\n"
              + "     description - text\n"
              + "     isdividable - boolean, mandatory");
    } else if (arguments.size() == 2) {
      System.out.print("> ");
      String parameters = scanner.nextLine();
      if (!parameters.matches(editCommandRegex)) {
        System.out.println(
                ">> Invalid command Format!\n>> Try \"unit edit help for proper syntax!");
        return;
      }
      List<String> unitAttributes = List.of(parameters.split("[,:]"));
      editHelper(unitAttributes);
    } else if (arguments.size() > 12) {
      System.out.println(">> Too many Arguments for command \"unit edit\"");
      System.out.println(">> Try \"unit edit help\" for proper syntax");
    } else if (arguments.size() < 6) {
      System.out.println(">> Insufficient arguments for command \"unit edit\"");
      System.out.println(">> Try \"unit edit help\" for proper syntax");
    } else if (!arguments.get(2).contains("id")) {
      System.out.println(">> Id is a Mandatory argument for every Edit operation");
      System.out.println(">> For every Edit operation the first argument must be unit's ID");
      System.out.println(">> Try \"unit edit help\" for proper syntax");
    } else {
      if(!command.substring(10).matches(editCommandRegex))
      {
        System.out.println(
                ">> Invalid command Format!\n>> Try \"user edit help for proper syntax!");
        return;
      }
      editHelper(arguments.subList(2, arguments.size()));
    }
  }

  private void editHelper(List<String> editAttributes) {
    Unit unit = new Unit();
    id = 0;
    try {
      id = Integer.parseInt(editAttributes.get(1).trim());
    } catch (Exception e) {
      System.out.println(">> Id must be a number");
      System.out.println(">> Please Try \"unit edit help\" for proper Syntax");
      return;
    }
    unit.setId(id);
    for (int index = 2; index < editAttributes.size(); index = index + 2) {
      if (editAttributes.get(index).trim().equals("name")) {
        unit.setName(editAttributes.get(index + 1).trim());
      } else if (editAttributes.get(index).trim().equals("code")) {
        unit.setCode(editAttributes.get(index + 1).trim());
      } else if (editAttributes.get(index).trim().equals("description")) {
        unit.setDescription(editAttributes.get(index + 1).trim());
      } else if (editAttributes.get(index).trim().equals("isdividable")) {
        boolean isDividable;
        try {
          isDividable = Boolean.parseBoolean(editAttributes.get(index + 1).trim());
        } catch (Exception e) {
          System.out.println(">> Isdividable must be either true or false!!");
          System.out.println(">> Try \"unit edit help\" for proper syntax");
          return;
        }
        unit.setIsDividable(isDividable);
      } else {
        System.out.println(">> Invalid attribute given!!!: " + editAttributes.get(index));
        System.out.println(">> Try \"unit edit help\" for proper Syntax");
        return;
      }
    }
    int statusCode;
    try {
      statusCode = unitService.editUnitService(unit);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return;
    }
    if (statusCode == 1) {
      System.out.println(">> Unit Edited Successfully!!!");
    } else if (statusCode == -1) {
      System.out.println(">> Unit Edit failed!!!");
      System.out.println(">> Please check the Id you have entered!!!");
    } else if (statusCode == 0) {
      System.out.println(">> Invalid format of attributes for edit command!!!");
      System.out.println(">> Try \"unit edit help:\" for proper syntax!!!");
    }
  }

  public void unitDeleteCLI(List<String> arguments) throws ApplicationErrorException {
    String codeRegex = "^[a-zA-Z]{1,4}$";
    if (arguments.size() == 3) {
      if (arguments.get(2).equals("help")) {
        System.out.println(
            ">> delete unit using the following template\n"
                + "\t \tcode\n"
                + "\t \n"
                + "\t  code - text, min 3 - 30 char, mandatory,existing\n");
      } else if (arguments.get(2).matches(codeRegex)) {
        System.out.print(">> Are you Sure you want to delete the Unit y/n :");
        String prompt = scanner.nextLine();
        if (prompt.equals("y")) {
          if (unitService.deleteUnitService(arguments.get(2)) == 1) {
            System.out.println(">> Unit deleted Successfully!!!");
          } else if (unitService.deleteUnitService(arguments.get(2)) == -1) {
            System.out.println(">> Unit deletion failed!!!");
            System.out.println(">> Please check the unitcode you have entered!!!");
            System.out.println("Try \"unit delete help\" for proper syntax");
          }
        } else if (prompt.equals("n")) {
          System.out.println(">> Delete operation cancelled");
        } else {
          System.out.println("Invalid delete prompt!!! Please select between y/n");
        }
      } else {
        System.out.println(">> Invalid format for unitCode!!!");
        System.out.println("Try \"unit delete help\" for proper syntax");
      }
    }
  }
}
