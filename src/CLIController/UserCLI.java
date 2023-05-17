package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.Product;
import Entity.User;
import Service.UserService;
import Service.UserServiceImplementation;

import javax.swing.*;
import java.util.*;

public class UserCLI {
  private int id;
  private String userType;
  private String userName;
  private String passWord;
  private String firstName;
  private String lastName;
  private long phoneNumber;
  private int pageLength;
  private int pageNumber;
  private String attribute;
  private String searchText;
  private List<User> userList;
  private final UserService userService = new UserServiceImplementation();
  private Scanner scanner = new Scanner(System.in);
  private HashMap<String, String> listAttributesMap = new HashMap<>();

  private List<String> userAttributes =
      Arrays.asList(
          "id", "usertype", "username", "password", "firstname", "lastname", "phonenumber");
  public void userCreateCLI(List<String> arguments) {
    Scanner scanner = new Scanner(System.in);
    if (arguments.size() == 3 && arguments.get(2).equals("help")) {
      System.out.println(
          ">> create user using following template\n"
              + ">>  usertype, username,  password, first name, last name, phone number\n"
              + "\tusertype - text, purchase/sales, mandatory\n"
              + "\tusername - text, min 3 - 30 char, mandatory\n"
              + "\tpassword - text, alphanumeric, special char, min 8 char, mandatory\n"
              + "\tfirstname - text, mandatory with 3 to 30 chars\n"
              + "\tlastname  - text, optional\n"
              + "\tphone - number, mandatory, ten digits, digit start with 9/8/7/6\t\t\t\t\n");
      return;
    } else if (arguments.size() == 2) {
      System.out.print("> ");
      String parameters = scanner.nextLine();
      List<String> userAttributes = List.of(parameters.split("\\,"));
      createHelper(userAttributes);
      return;
    }
    createHelper(arguments.subList(2, arguments.size()));
  }

  private void createHelper(List<String> userAttributes) {
    if (userAttributes.size() < 6) {
      System.out.println("Insufficient arguments for command \"user create\"");
      System.out.println("Try \"user create help\" for proper syntax");
      return;
    }
    if (userAttributes.size() > 6) {
      System.out.println("Too many arguments for command \"user create\"");
      System.out.println("Try \"user create help\" for proper syntax");
      return;
    }
    userType = userAttributes.get(0).trim();
    userName = userAttributes.get(1).trim();
    passWord = userAttributes.get(2).trim();
    firstName = userAttributes.get(3).trim();
    lastName = userAttributes.get(4).trim();
    phoneNumber = 0;
    try {
      phoneNumber = Long.parseLong(userAttributes.get(5).trim());
    } catch (Exception e) {
      System.out.println(">> Invalid format for 6th argument \"phonenumber\"");
      System.out.println(">> Try \"user create help\" for proper syntax");
    }
    User user = new User(userType, userName, passWord, firstName, lastName, phoneNumber);
    User createdUser;
    try {
      createdUser = userService.createUserService(user);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return;
    }
    if (createdUser== null) {
      System.out.println("Template Mismatch!!");
      System.out.println("Try \"user create help\" for proper syntax");
    } else if (createdUser != null) {
      System.out.println(">> User Creation Successfull!!");
      System.out.println(createdUser);
    }
  }

  public void userCountCLI(List<String> arguments) throws ApplicationErrorException {
    if (arguments.size() > 2) {
      System.out.println(">> Invalid Command!! Try \"help\"");
      return;
    }
    int userCount = userService.countUserService();
    System.out.println(">> User Count " + userCount);
  }

  public void userListCLI(List<String> arguments)
      throws PageCountOutOfBoundsException, ApplicationErrorException {
    listAttributesMap.put("Pagelength", null);
    listAttributesMap.put("Pagenumber", null);
    listAttributesMap.put("Attribute", null);
    listAttributesMap.put("Searchtext", null);
    if (arguments.size() == 3 && arguments.get(2).equals("help")) {
      System.out.println(
          ">> List user with the following options\n"
              + ">> user list - will list all the users default to maximum upto 20 users\n"
              + ">> user list -p 10 - pageable list shows 10 users as default\n"
              + ">> user list -p 10 3 - pagable list shows 10 users in 3rd page, ie., user from 21 to 30\n"
              + ">> user list -s searchtext - search the user with the given search text in all the searchable attributes\n"
              + ">> user list -s <attr>: searchtext - search the user with the given search text in all the given attribute\n"
              + ">> user list -s <attr>: searchtext -p 10 6 - pagable list shows 10 users in 6th page with the given search text in the given attribute\n");
    } else if (arguments.size() == 2) {
      listAttributesMap.put("Pagelength", "20");
      listAttributesMap.put("Pagenumber", "1");
      listAttributesMap.put("Attribute", "id");
      listHelper(listAttributesMap);
    } else if (arguments.size() == 4) {
      if (arguments.get(2).equals("-p")) {
        try {
          pageLength = Integer.parseInt(arguments.get(3));
        } catch (Exception e) {
          System.out.println(">> Invalid Page Size input!!!");
          System.out.println(">> Try \"user list help\" for proper syntax");
        }
        listAttributesMap.put("Pagelength", String.valueOf(pageLength));
        listAttributesMap.put("Pagenumber", "1");
        listAttributesMap.put("Attribute", "id");
        listHelper(listAttributesMap);
      } else if (arguments.get(2).equals("-s")) {
        searchText = arguments.get(3).trim();
        listAttributesMap.put("Searchtext", searchText);
        listHelper(listAttributesMap);
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"user list help\" for proper syntax");
      }
    } else if (arguments.size() == 5) {
      pageLength = 0;
      pageNumber = 0;
      if (arguments.get(2).equals("-p")) {
        try {
          pageLength = Integer.parseInt(arguments.get(3));
          pageNumber = Integer.parseInt(arguments.get(4));
        } catch (Exception e) {
          System.out.println(">> Invalid page Size (or) page Number input");
          System.out.println(">> Try \"product list help\" for proper syntax");
          return;
        }
        listAttributesMap.put("Pagelength", String.valueOf(pageLength));
        listAttributesMap.put("Pagenumber", String.valueOf(pageNumber));
        listAttributesMap.put("Attribute", "id");
        listHelper(listAttributesMap);
      } else if (arguments.get(2).equals("-s")) {
        attribute = arguments.get(3);
        attribute = attribute.replace(":", "");
        searchText = arguments.get(4);
        if (userAttributes.contains(attribute)) {
          listAttributesMap.put("Attribute", attribute);
          listAttributesMap.put("Searchtext", searchText);
          listAttributesMap.put("Pagelength", "20");
          listAttributesMap.put("Pagenumber", "1");
          listHelper(listAttributesMap);
        } else {
          System.out.println(">> Given attribute is not a Searchable attribute!!!");
          System.out.println(">> Try \"user list help\" for proper syntax!!!");
        }
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"user list help\" for proper syntax");
      }
    } else if (arguments.size() == 7) {
      if (arguments.get(2).equals("-s")) {
        attribute = arguments.get(3);
        attribute = attribute.replace(":", "");
        searchText = arguments.get(4);
        listAttributesMap.put("Attribute", attribute);
        listAttributesMap.put("Searchtext", searchText);
        if (userAttributes.contains(attribute)) {
          if (arguments.get(5).equals("-p")) {
            try {
              pageLength = Integer.parseInt(arguments.get(6));
            } catch (Exception e) {
              System.out.println(">> Invalid page Size input");
              System.out.println(">> Try \"product list help\" for proper syntax");
              return;
            }
            listAttributesMap.put("Pagelength", String.valueOf(pageLength));
            listAttributesMap.put("Pagenumber", "1");
            listHelper(listAttributesMap);
          } else {
            System.out.println(">> Invalid Command Extension format !!!");
            System.out.println("Try \"user list help\" for proper syntax");
          }
        } else {
          System.out.println("Given attribute is not a searchable attribute!!");
          System.out.println("Try \"user list help\" for proper syntax");
        }
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"user list help\" for proper syntax");
      }
    } else if (arguments.size() == 8) {
      if (arguments.get(2).equals("-s")) {
        attribute = arguments.get(3);
        attribute = attribute.replace(":", "");
        searchText = arguments.get(4);
        listAttributesMap.put("Attribute", attribute);
        listAttributesMap.put("Searchtext", searchText);
        if (userAttributes.contains(attribute)) {
          if (arguments.get(5).equals("-p")) {
            try {
              pageLength = Integer.parseInt(arguments.get(6));
              pageNumber = Integer.parseInt(arguments.get(7));
            } catch (Exception e) {
              System.out.println(">> Invalid page Size (or) page Number input");
              System.out.println(">> Try \"user list help\" for proper syntax");
              return;
            }
            listAttributesMap.put("Pagelength", String.valueOf(pageLength));
            listAttributesMap.put("Pagenumber", String.valueOf(pageNumber));
            listHelper(listAttributesMap);
          } else {
            System.out.println("Invalid Extension Given!!!");
            System.out.println("Try \"user list help\" for proper syntax");
          }
        } else {
          System.out.println("Given attribute is not a searchable attribute!!");
          System.out.println("Try \"user list help\" for proper syntax");
        }
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"product list help\" for proper syntax");
      }
    } else if (arguments.size() == 3) {
      System.out.println("Invalid command format!!!");
      System.out.println(">> Try \"user list help\" for proper syntax");
    } else {
      System.out.println("Invalid command format!!!");
      System.out.println(">> Try \"user list help\" for proper syntax!!!");
    }
  }

  private void listHelper(HashMap<String, String> listAttributesMap) {
    try{
    userList = userService.listUserService(listAttributesMap);
    if (userList == null) {
      System.out.println(">>Given SearchText does not exist!!!");
      return;
    }
    for (User user : userList) {
      System.out.println(
          ">> id: "
              + user.getId()
              + ", usertype: "
              + user.getUserType()
              + ", username: "
              + user.getUserName()
              + ", password: "
              + user.getPassWord()
              + ", firstname: "
              + user.getFirstName()
              + ", lastname: "
              + user.getLastName()
              + ", phonenumber: "
              + user.getPhoneNumber());
    }
    }
    catch(Exception e)
    {
      System.out.println(e.getMessage());
    }
  }

  public void userEditCLI(List<String> arguments,String command) {
    final String editCommandRegex="^id:\\s*(\\d+)(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?(?:,\\s*([A-Za-z]+):\\s*([^,]+))?$";
    if (arguments.size() == 3 && arguments.get(2).equals("help")) {
      System.out.println(
          ">> Edit user using following template. Copy the user data from the list, edit the attribute values. \n"
              + ">> id: <id - 6>, usertype: <usertype-edited>, username: <username>,  password: <password>, first name: <first name>, last name: <last name>, phone number: <phone number>\n"
              + "\n"
              + ">> You can also restrict the user data by editable attributes. Id attribute is mandatory for all the edit operation.\n"
              + ">> id: <id - 6>, usertype: <usertype-edited>, username: <username-edited>\n"
              + "\n"
              + ">> You can not give empty or null values to the mandatory attributes.\n"
              + ">> id: <id - 6>, usertype: , username: null\n"
              + "\t\n"
              + "\tid\t\t\t - number, mandatory\t\n"
              + "\tusertype - text, purchase/sales, mandatory\n"
              + "\tuse\trname - text, min 3 - 30 char, mandatory\n"
              + "\tpassword - text, alphanumeric, special char, min 8 char, mandatory\n"
              + "\tfirstname - text, mandatory with 3 to 30 chars\n"
              + "\tlastname  - text, optional\n"
              + "\tphone - number, mandatory, ten digits, digit start with 9/8/7/6");
    } else if (arguments.size() == 2) {
      System.out.print("> ");
      String parameters = scanner.nextLine();
      if (!parameters.matches(editCommandRegex)) {
        System.out.println(
                ">> Invalid command Format!\n>> Try \"user edit help for proper syntax!");
        return;
      }
      List<String> userAttributes = List.of(parameters.split("[,:]"));
      editHelper(userAttributes);
    } else if (arguments.size() > 16) {
      System.out.println(">> Too many Arguments for command \"product edit\"");
    } else if (arguments.size() < 6) {
      System.out.println(">> Insufficient arguments for command \"product edit\"");
    } else if (!arguments.get(2).contains("id")) {
      System.out.println(">> Id is a Mandatory argument for every Edit operation");
      System.out.println(">> For every Edit operation the first argument must be user's ID");
      System.out.println(">> Try \"user edit help\" for proper syntax");
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
    User user = new User();
    try {
      id = Integer.parseInt(editAttributes.get(1).trim());
    } catch (Exception e) {
      System.out.println(">> Id must be a Number!");
      System.out.println(">> Please Try \"user edit help\" for proper Syntax");
    }
    user.setId(id);
    for (int index = 2; index < editAttributes.size(); index = index + 2) {
      if (editAttributes.get(index).trim().equals("username")) {
        user.setUserName(editAttributes.get(index + 1).trim());
      } else if (editAttributes.get(index).trim().equals("usertype")) {
        user.setUserType(editAttributes.get(index + 1).trim());
      } else if (editAttributes.get(index).trim().equals("password")) {
        user.setPassWord(editAttributes.get(index + 1).trim());
      } else if (editAttributes.get(index).trim().equals("firstname")) {
        user.setFirstName(editAttributes.get(index + 1).trim());
      } else if (editAttributes.get(index).trim().equals("lastname")) {
        user.setLastName(editAttributes.get(index + 1).trim());
      } else if (editAttributes.get(index).trim().equals("phonenumber")) {
        try {
          phoneNumber = Long.parseLong(editAttributes.get(index + 1).trim());
        } catch (NumberFormatException e) {
          System.out.println(">> Phonenumber must be numeric!!");
          System.out.println(">> Try \"user edit help\" for proper syntax");
          return;
        }
        user.setPhoneNumber(phoneNumber);
      } else {
        System.out.println(">> Invalid attribute given!!! : " + editAttributes.get(index));
        System.out.println(">> Try \"user edit help\" for proper syntax");
        return;
      }
    }
    int statusCode;
    try {
      statusCode = userService.editUserService(user);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return;
    }
    if (statusCode == 1) {
      System.out.println(">> User Edited Succesfully");
    } else if (statusCode == -1) {
      System.out.println(">> User edit failed!!!");
      System.out.println(">>Please check the Id you have entered!!!");
    } else if (statusCode == 0) {
      System.out.println(">>Invalid format of attributes given for edit Command!!!");
      System.out.println(">>Try \"user edit help\" for proper syntax");
    }
  }

  public void userDeleteCLI(List<String> arguments) throws ApplicationErrorException {
    String nameregex = "^[a-zA-Z0-9]{3,30}$";
    if (arguments.size() == 3) {
      if (arguments.get(2).equals("help")) {
        System.out.println(
            ">> delete user using the following template\n"
                + "\t username\n"
                + "\t \n"
                + "\t  username - text, min 3 - 30 char, mandatory,existing\n"
                + "\n");
      } else if (arguments.get(2).matches(nameregex)) {
        System.out.println(">> Are you sure want to delete the User y/n ? : ");
        String prompt = scanner.nextLine();
        if (prompt.equals("y")) {
          if (userService.deleteUserService(arguments.get(2)) == 1) {
            System.out.println("User Deleted Successfull!!!");
          } else if (userService.deleteUserService(arguments.get(2)) == -1) {
            System.out.println(">> User Deletion Failed!!!");
            System.out.println(">> Please check the username you have entered!!!");
            System.out.println("Try \"user delete help\" for proper syntax");
          }
        } else if (prompt.equals("n")) {
          System.out.println(">> Delete operation cancelled");
        } else {
          System.out.println(">> Invalid delete prompt!!! Please select between y/n");
        }
      } else {
        System.out.println(">> Invalid format for username!!!");
        System.out.println("Try \"user delete help\" for proper syntax");
      }
    }
  }
}
