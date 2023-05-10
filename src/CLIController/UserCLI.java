package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.User;
import Service.UserService;
import Service.UserServiceImplementation;
import java.util.*;

public class UserCLI {
    HashMap<String, String> attributeMap = new HashMap<> ();
    HashMap<String, String> listAttributesMap = new HashMap<> ();

    public void userCreateCLI (String[] arguments) {
        Scanner scanner = new Scanner (System.in);
        String nameRegex = "^[a-zA-Z\\s]{3,30}$";
        String codeRegex = "^[a-zA-Z0-9]{2,6}$";
        List<String> userTypeList = new ArrayList<> (Arrays.asList ("Admin", "Purchase", "Sales"));
        if (arguments.length == 3 && arguments[2].equals ("help")) {
            System.out.println (">> create user using following template\n" +
                    ">>  usertype, username,  password, first name, last name, phone number\n" +
                    "\tusertype - text, purchase/sales, mandatory\n" +
                    "\tusername - text, min 3 - 30 char, mandatory\n" +
                    "\tpassword - text, alphanumeric, special char, min 8 char, mandatory\n" +
                    "\tfirstname - text, mandatory with 3 to 30 chars\n" +
                    "\tlastname  - text, optional\n" +
                    "\tphone - number, mandatory, ten digits, digit start with 9/8/7/6\t\t\t\t\n");
            return;
        } else if (arguments.length == 2) {
            System.out.print ("> ");
            String parameters = scanner.nextLine ();
            String[] userAttributes = parameters.split ("\\,");
            if (userAttributes.length < 6) {
                System.out.println ("Insufficient arguments for command \"user create\"");
                System.out.println ("Try \"user create help\" for proper syntax");
                return;
            }
            if (userAttributes.length > 6) {
                System.out.println ("Too many arguments for command \"user create\"");
                System.out.println ("Try \"user create help\" for proper syntax");
                return;
            }
            String userType = userAttributes[0].trim ();
            String userName = userAttributes[1].trim ();
            String passWord = userAttributes[2].trim ();
            String firstName = userAttributes[3].trim ();
            String lastName = userAttributes[4].trim ();
            long phoneNumber = 0;
            //Necessary Validations
            if (! userTypeList.contains (userType)) {
                System.out.println (">> Invalid Usertype given!!!");
                System.out.println (">> Try \"user create help\" for proper syntax");
                return;
            }
            try {
                phoneNumber = Long.parseLong (userAttributes[5].trim ());
            } catch (Exception e) {
                System.out.println (">> Invalid format for 6th argument \"phonenumber\"");
                System.out.println (">> Try \"user create help\" for proper syntax");
            }
            User user = new User (userType, userName, passWord, firstName, lastName, phoneNumber);
            UserService userCreateService = new UserServiceImplementation ();
            int resultcode = 1;
            try {
                resultcode = userCreateService.createUserService (user);

            } catch (Exception e) {
                System.out.println (e.getMessage ());
                return;
            }
            if (resultcode == 1) {
                System.out.println (">> User Created Succesfully!!!");
            } else if (resultcode == - 1) {
                System.out.println (">> User Creation Failed!!!");
                System.out.println (">> The username you have entered already Exists!!!");
            }
            return;
        } else if (arguments.length < 8) {
            System.out.println (">>Insufficient Arguments for command \"user create\"");
            System.out.println (">> Try \"user create help\" for proper syntax");
            return;
        } else if (arguments.length > 8) {
            System.out.println ("Too many arguments for aommand \"user create\"");
            System.out.println (">> Try \"user create help\" for proper syntax");
            return;
        }
        String userType = arguments[2].trim ();
        String userName = arguments[3].trim ();
        String passWord = arguments[4].trim ();
        String firstName = arguments[5].trim ();
        String lastName = arguments[6].trim ();
        if (! userTypeList.contains (userType)) {
            System.out.println (">> Invalid Usertype given!!!");
            System.out.println (">> Try \"user create help\" for proper syntax");
            return;
        }
        long phoneNumber = 0L;
        try {
            phoneNumber = Long.parseLong (arguments[7].trim ());
        } catch (Exception e) {
            System.out.println (">> Invalid format for 6th argument \"phonenumber\"");
            return;
        }
        User user = new User (userType, userName, passWord, firstName, lastName, phoneNumber);
        UserService userCreateService = new UserServiceImplementation ();
        int resultCode = 1;
        try {
            resultCode = userCreateService.createUserService (user);
        } catch (Exception e) {
            System.out.println (e.getMessage ());
            return;
        }
        if (resultCode == 1) {
            System.out.println (">> User Created Successfully!!!");
        } else if (resultCode == - 1) {
            System.out.println (">> User Creation failed");
            System.out.println ("The username you have entered already exists!!!");
        }
    }

    public void userCountCLI (String[] arguments) throws ApplicationErrorException {
        UserService countUserService = new UserServiceImplementation ();
        int userCount = countUserService.countUserService ();
        System.out.println (">> User Count " + userCount);
    }

    public void userListCLI (String[] arguments) throws PageCountOutOfBoundsException, ApplicationErrorException {
        listAttributesMap.put ("Pagelength", null);
        listAttributesMap.put ("Pagenumber", null);
        listAttributesMap.put ("Attribute", null);
        listAttributesMap.put ("Searchtext", null);
        UserService listUserService = new UserServiceImplementation ();
        List<User> userList;
        if (arguments.length == 3 && arguments[2].equals ("help")) {
            System.out.println (">> List user with the following options\n" +
                    ">> user list - will list all the users default to maximum upto 20 users\n" +
                    ">> user list -p 10 - pageable list shows 10 users as default\n" +
                    ">> user list -p 10 3 - pagable list shows 10 users in 3rd page, ie., user from 21 to 30\n" +
                    ">> user list -s searchtext - search the user with the given search text in all the searchable attributes\n" +
                    ">> user list -s <attr>: searchtext - search the user with the given search text in all the given attribute\n" +
                    ">> user list -s <attr>: searchtext -p 10 6 - pagable list shows 10 users in 6th page with the given search text in the given attribute\n");
            return;
        } else if (arguments.length == 2) {
            listAttributesMap.put ("Pagelength", "20");
            listAttributesMap.put ("Pagenumber", "1");
            userList = listUserService.listUserService (listAttributesMap);
            //DO PRINTINGS
            for (User user : userList) {
                System.out.println (">> id: " + user.getId () + ", usertype: " + user.getUserType () + ", username: " + user.getUserName () + ", password: " + user.getPassWord () + ", firstname: " + user.getFirstName () + ", lastname: " + user.getLastName () + ", phonenumber: " + user.getPhoneNumber ());
            }
            return;
        } else if (arguments.length == 4) {
            int pageLength = 0;
            if (arguments[2].equals ("-p")) {
                try {
                    pageLength = Integer.parseInt (arguments[3]);
                } catch (Exception e) {
                    System.out.println (">> Invalid Page Size input!!!");
                    System.out.println (">> Try \"user list help\" for proper syntax");
                }
                listAttributesMap.put ("Pagelength", String.valueOf (pageLength));
                listAttributesMap.put ("Pagenumber", "1");
                userList = listUserService.listUserService (listAttributesMap);
                for (User user : userList) {
                    System.out.println (">> id: " + user.getId () + ", usertype: " + user.getUserType () + ", username: " + user.getUserName () + ", password: " + user.getPassWord () + ", firstname: " + user.getFirstName () + ", lastname: " + user.getLastName () + ", phonenumber: " + user.getPhoneNumber ());
                }
            } else if (arguments[2].equals ("-s")) {
                String searchText = arguments[3].trim ();
                listAttributesMap.put ("Searchtext", searchText);
                userList = listUserService.listUserService (listAttributesMap);
                if (userList.size () == 0) {
                    System.out.println (">> Given SearchText does not Exists!!!");
                }
                for (User user : userList) {
                    System.out.println (">> id: " + user.getId () + ", usertype: " + user.getUserType () + ", username: " + user.getUserName () + ", password: " + user.getPassWord () + ", firstname: " + user.getFirstName () + ", lastname: " + user.getLastName () + ", phonenumber: " + user.getPhoneNumber ());
                }
            } else {
                System.out.println (">> Invalid Extension given");
                System.out.println (">> Try \"user list help\" for proper syntax");
            }

        } else if (arguments.length == 5) {
            int pageLength = 0;
            int pageNumber = 0;
            if (arguments[2].equals ("-p")) {
                try {
                    pageLength = Integer.parseInt (arguments[3]);
                    pageNumber = Integer.parseInt (arguments[4]);
                } catch (Exception e) {
                    System.out.println (">> Invalid page Size (or) page Number input");
                    System.out.println (">> Try \"product list help\" for proper syntax");
                    return;
                }
                listAttributesMap.put ("Pagelength", String.valueOf (pageLength));
                listAttributesMap.put ("Pagenumber", String.valueOf (pageNumber));
                try {
                    userList = listUserService.listUserService (listAttributesMap);
                } catch (Exception e) {
                    System.out.println (e.getMessage ());
                    return;
                }
                for (User user : userList) {
                    System.out.println (">> id: " + user.getId () + ", usertype: " + user.getUserType () + ", username: " + user.getUserName () + ", password: " + user.getPassWord () + ", firstname: " + user.getFirstName () + ", lastname: " + user.getLastName () + ", phonenumber: " + user.getPhoneNumber ());
                }
            } else if (arguments[2].equals ("-s")) {
                List<String> userAttributes = Arrays.asList ("id", "usertype", "username", "password", "firstname", "lastname", "phonenumber");
                String attribute = arguments[3];
                attribute = attribute.replace (":", "");
                String searchText = arguments[4];
                if (userAttributes.contains (attribute)) {
                    listAttributesMap.put
                            ("Attribute", attribute);
                    listAttributesMap.put ("Searchtext", searchText);
                    listAttributesMap.put ("Pagelength", "20");
                    listAttributesMap.put ("Pagenumber", "1");
                    userList = listUserService.listUserService (listAttributesMap);
                    if (userList == null) {
                        System.out.println (">> Given SearchText doesn not exists!!!");
                        return;
                    }
                    for (User user : userList) {
                        System.out.println (">> id: " + user.getId () + ", usertype: " + user.getUserType () + ", username: " + user.getUserName () + ", password: " + user.getPassWord () + ", firstname: " + user.getFirstName () + ", lastname: " + user.getLastName () + ", phonenumber: " + user.getPhoneNumber ());
                    }
                } else {
                    System.out.println (">> Given attribute is not a Searchable attribute!!!");
                    System.out.println (">> Try \"user list help\" for proper syntax!!!");
                }
                return;
            } else {
                System.out.println (">> Invalid Extension given");
                System.out.println (">> Try \"user list help\" for proper syntax");
            }
        } else if (arguments.length == 7) {
            if (arguments[2].equals ("-s")) {
                List<String> userAttributes = Arrays.asList ("id", "usertype", "username", "password", "firstname", "lastname", "phonenumber");
                String attribute = arguments[3];
                attribute = attribute.replace (":", "");
                String searchText = arguments[4];
                listAttributesMap.put ("Attribute", attribute);
                listAttributesMap.put ("Searchtext", searchText);
                if (userAttributes.contains (attribute)) {
                    int pageLength = 0;
                    if (arguments[5].equals ("-p")) {
                        try {
                            pageLength = Integer.parseInt (arguments[6]);
                        } catch (Exception e) {
                            System.out.println (">> Invalid page Size input");
                            System.out.println (">> Try \"product list help\" for proper syntax");
                            return;
                        }
                        listAttributesMap.put ("Pagelength", String.valueOf (pageLength));
                        listAttributesMap.put ("Pagenumber", "1");
                        userList = listUserService.listUserService (listAttributesMap);
                        if (userList == null) {
                            System.out.println (">>Given SearchText does not exists");
                            return;
                        }
                        for (User user : userList) {
                            System.out.println (">> id: " + user.getId () + ", usertype: " + user.getUserType () + ", username: " + user.getUserName () + ", password: " + user.getPassWord () + ", firstname: " + user.getFirstName () + ", lastname: " + user.getLastName () + ", phonenumber: " + user.getPhoneNumber ());
                        }
                    } else {
                        System.out.println (">> Invalid Command Extension format !!!");
                        System.out.println ("Try \"user list help\" for proper syntax");
                    }
                } else {
                    System.out.println ("Given attribute is not a searchable attribute!!");
                    System.out.println ("Try \"user list help\" for proper syntax");
                }
            } else {
                System.out.println (">> Invalid Extension given");
                System.out.println (">> Try \"user list help\" for proper syntax");
            }

        } else if (arguments.length == 8) {
            if (arguments[2].equals ("-s")) {
                List<String> userAttributes = Arrays.asList ("id", "usertype", "username", "password", "firstname", "lastname", "phonenumber");
                String attribute = arguments[3];
                attribute = attribute.replace (":", "");
                String searchText = arguments[4];
                int pageLength = 0;
                int pageNumber = 0;
                listAttributesMap.put ("Attribute", attribute);
                listAttributesMap.put ("Searchtext", searchText);
                if (userAttributes.contains (attribute)) {
                    if (arguments[5].equals ("-p")) {
                        try {
                            pageLength = Integer.parseInt (arguments[6]);
                            pageNumber = Integer.parseInt (arguments[7]);
                        } catch (Exception e) {
                            System.out.println (">> Invalid page Size (or) page Number input");
                            System.out.println (">> Try \"user list help\" for proper syntax");
                            return;
                        }
                        listAttributesMap.put ("Pagelength", String.valueOf (pageLength));
                        listAttributesMap.put ("Pagenumber", String.valueOf (pageNumber));
                        userList = listUserService.listUserService (listAttributesMap);
                        if (userList == null) {
                            System.out.println (">>Given SearchText does not exist!!!");
                            return;
                        }
                        for (User user : userList) {
                            System.out.println (">> id: " + user.getId () + ", usertype: " + user.getUserType () + ", username: " + user.getUserName () + ", password: " + user.getPassWord () + ", firstname: " + user.getFirstName () + ", lastname: " + user.getLastName () + ", phonenumber: " + user.getPhoneNumber ());
                        }
                    } else {
                        System.out.println ("Invalid Extension Given!!!");
                        System.out.println ("Try \"user list help\" for proper syntax");
                    }
                } else {
                    System.out.println ("Given attribute is not a searchable attribute!!");
                    System.out.println ("Try \"user list help\" for proper syntax");
                }
            } else {
                System.out.println (">> Invalid Extension given");
                System.out.println (">> Try \"product list help\" for proper syntax");
            }
        } else if (arguments.length == 3) {
            System.out.println ("Invalid command format!!!");
            System.out.println (">> Try \"user list help\" for proper syntax");
        } else {
            System.out.println ("Invalid command formart!!!");
            System.out.println (">> Try \"user list help\" for proper syntax!!!");
        }
    }

    public void userEditCLI (String[] arguments) {
        attributeMap.put ("id", null);
        attributeMap.put ("username", null);
        attributeMap.put ("usertype", null);
        attributeMap.put ("password", null);
        attributeMap.put ("firstname", null);
        attributeMap.put ("lastname", null);
        attributeMap.put ("phonenmber", null);
        Scanner scanner = new Scanner (System.in);
        UserService userEditService = new UserServiceImplementation ();
        if (arguments.length == 3 && arguments[2].equals ("help")) {
            System.out.println (">> Edit user using following template. Copy the user data from the list, edit the attribute values. \n" +
                    ">> id: <id - 6>, usertype: <usertype-edited>, username: <username>,  password: <password>, first name: <first name>, last name: <last name>, phone number: <phone number>\n" +
                    "\n" +
                    ">> You can also restrict the user data by editable attributes. Id attribute is mandatory for all the edit operation.\n" +
                    ">> id: <id - 6>, usertype: <usertype-edited>, username: <username-edited>\n" +
                    "\n" +
                    ">> You can not give empty or null values to the mandatory attributes.\n" +
                    ">> id: <id - 6>, usertype: , username: null\n" +
                    "\t\n" +
                    "\tid\t\t\t - number, mandatory\t\n" +
                    "\tusertype - text, purchase/sales, mandatory\n" +
                    "\tuse\trname - text, min 3 - 30 char, mandatory\n" +
                    "\tpassword - text, alphanumeric, special char, min 8 char, mandatory\n" +
                    "\tfirstname - text, mandatory with 3 to 30 chars\n" +
                    "\tlastname  - text, optional\n" +
                    "\tphone - number, mandatory, ten digits, digit start with 9/8/7/6");
            return;
        } else if (arguments.length == 2) {
            System.out.print ("> ");
            String parameters = scanner.nextLine ();
            String[] userAttributes = parameters.split ("\\,");
            if (userAttributes[0].contains ("id")) {
                for (String attribute : userAttributes) {
                    if (attribute.contains ("id")) {
                        String[] keyValues = attribute.split ("\\:");
                        attributeMap.put ("id", keyValues[1].trim ());
                    } else if (attribute.contains ("username")) {
                        String[] keyValues = attribute.split ("\\:");
                        attributeMap.put ("username", keyValues[1].trim ());
                    } else if (attribute.contains ("usertype")) {
                        String[] keyValues = attribute.split ("\\:");
                        attributeMap.put ("usertype", keyValues[1].trim ());
                    } else if (attribute.contains ("password")) {
                        String[] keyValues = attribute.split ("\\:");
                        attributeMap.put ("password", keyValues[1].trim ());
                    } else if (attribute.contains ("firstname")) {
                        String[] keyValues = attribute.split ("\\:");
                        attributeMap.put ("firstname", keyValues[1].trim ());
                    } else if (attribute.contains ("lastname")) {
                        String[] keyValues = attribute.split ("\\:");
                        attributeMap.put ("lastname", keyValues[1].trim ());
                    } else if (attribute.contains ("phonenumber")) {
                        String[] keyValues = attribute.split ("\\:");
                        attributeMap.put ("phonenumber", keyValues[1].trim ());
                    } else {
                        System.out.println (">> Invalid attribute given!!! : " + attribute);
                        System.out.println (">> Try \"user edit help\" for proper syntax");
                        break;
                    }
                }
                if (attributeMap.get ("id").equals ("")) {
                    System.out.println (">> Id should not be null");
                    System.out.println (">> Try \"user edit help\" for proper syntax");
                    return;
                }
                int id = 0;
                try {
                    id = Integer.parseInt (attributeMap.get ("id").trim ());
                } catch (Exception e) {
                    System.out.println (">> Id must be a number");
                    return;
                }
                int statusCode;
                try {
                    statusCode = userEditService.editUserService (attributeMap);
                } catch (Exception e) {
                    System.out.println (e.getMessage ());
                    return;
                }
                if (statusCode == 1) {
                    System.out.println (">> User Edited Successfully!!!");
                } else if (statusCode == - 1) {
                    System.out.println (">> Product Edit failed!!!");
                    System.out.println (">> Please Check the Id you have entered!");
                } else if (statusCode == 0) {
                    System.out.println (">> Invalid format of attributes given for edit Command!!!");
                    System.out.println (">> Try \"user edit help\" for proper syntax");
                }
            } else {
                System.out.println (">> Id is a Mandatory argument for every Edit operation");
                System.out.println (">> For every Edit operation the first argument must be User's ID");
                System.out.println (">> Try \"user edit help\" for proper syntax");
            }
        } else if (arguments.length > 16) {
            System.out.println (">> Too many Arguments for command \"product edit\"");
        } else if (arguments.length < 6) {
            System.out.println (">> Insufficient arguments for command \"product edit\"");
        } else if (! arguments[2].contains ("id")) {
            System.out.println (">> Id is a Mandatory argument for every Edit operation");
            System.out.println (">> For every Edit operation the first argument must be user's ID");
            System.out.println (">> Try \"user edit help\" for proper syntax");
        } else {
            attributeMap.put ("id", arguments[3]);
            if (attributeMap.get ("id").equals ("")) {
                System.out.println (">> Id should not be null");
                System.out.println (">> Try \"user edit help\" for proper Syntax");
                return;
            }
            int id = 0;
            try {
                id = Integer.parseInt (attributeMap.get ("id"));
            } catch (Exception e) {
                System.out.println (">> Id must be a Number!");
                System.out.println (">> Please Try \"user edit help\" for proper Syntax");
            }
            for (int index = 4; index < arguments.length; index = index + 2) {
                if (arguments[index].contains ("username")) {
                    attributeMap.put ("username", arguments[index + 1]);
                } else if (arguments[index].contains ("usertype")) {

                    attributeMap.put ("usertype", arguments[index + 1]);
                } else if (arguments[index].contains ("password")) {

                    attributeMap.put ("password", arguments[index + 1]);
                } else if (arguments[index].contains ("firstname")) {
                    attributeMap.put ("firstname", arguments[index + 1]);
                } else if (arguments[index].contains ("lastname")) {
                    attributeMap.put ("lastname", arguments[index + 1]);
                } else if (arguments[index].contains ("phonenumber")) {
                    attributeMap.put ("phonenumber", arguments[index + 1]);
                } else {
                    System.out.println (">> Invalid attribute given!!! : " + arguments[index]);
                    System.out.println (">> Try \"user edit help\" for proper syntax");
                    break;
                }
            }

            int statusCode;
            try {
                statusCode = userEditService.editUserService (attributeMap);
            } catch (Exception e) {
                System.out.println (e.getMessage ());
                return;
            }
            if (statusCode == 1) {
                System.out.println (">> User Edited Succesfully");
            } else if (statusCode == - 1) {
                System.out.println (">> User edit failed!!!");
                System.out.println (">>Please check the Id you have entered!!!");
            } else if (statusCode == 0) {
                System.out.println (">>Invalid format of attributes given for edit Command!!!");
                System.out.println (">>Try \"user edit help\" for proper syntax");
            }
        }
    }

    public void userDeleteCLI (String[] arguments) throws ApplicationErrorException {
        Scanner scanner = new Scanner (System.in);
        UserService userDeleteSerivice = new UserServiceImplementation ();
        String nameregex = "^[a-zA-Z0-9]{3,30}$";
        if (arguments.length == 3) {
            if (arguments[2].equals ("help")) {
                System.out.println (">> delete user using the following template\n" +
                        "\t username\n" +
                        "\t \n" +
                        "\t  username - text, min 3 - 30 char, mandatory,existing\n" +
                        "\n");
            } else if (arguments[2].matches (nameregex)) {
                System.out.println (">> Are you sure want to delete the User y/n ? : ");
                String prompt = scanner.nextLine ();
                if (prompt.equals ("y")) {
                    if (userDeleteSerivice.deleteUserService (arguments[2]) == 1) {
                        System.out.println ("User Deleted Successfull!!!");
                    } else if (userDeleteSerivice.deleteUserService (arguments[2]) == - 1) {
                        System.out.println (">> User Deletion Failed!!!");
                        System.out.println (">> Please check the username you have entered!!!");
                        System.out.println ("Try \"user delete help\" for proper syntax");
                    }

                } else if (prompt.equals ("n")) {
                    System.out.println (">> Delete operation cancelled");
                } else {
                    System.out.println (">> Invalid delete prompt!!! Please select between y/n");
                }
            } else {
                System.out.println (">> Invalid format for username!!!");
                System.out.println ("Try \"user delete help\" for proper syntax");
            }
        }
    }
}

