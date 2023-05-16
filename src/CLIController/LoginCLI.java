package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import DAO.UniqueConstraintException;
import Entity.User;
import Service.LoginService;
import Service.LoginServiceImplementation;
import java.sql.SQLException;
import java.util.Scanner;

public class LoginCLI {
  private static final LoginService loginService = new LoginServiceImplementation();
  private static final Scanner scanner = new Scanner(System.in);
  private static String userName;
  private static String passWord;
  private static String firstName;
  private static String lastName;
  private static Long phoneNumber;

  public static void main(String args[])
      throws SQLException,
          UniqueConstraintException,
          ApplicationErrorException,
          PageCountOutOfBoundsException {
    if (loginService.checkIfInitialSetup()) {

        System.out.println(
                          "\t\t\t\t\t\t*********************************************************\n"
                        + "\t\t\t\t\t\t*********************   WELCOME   ***********************\n"
                        + "\t\t\t\t\t\t*********************************************************\n\n");
        System.out.println(
                "> Welcome to the Billing software setup. You have to create admin user to continue with the setup.\n\n");
      do{
        System.out.println(">> create user using following template\n"
                + ">>  usertype, username,  password, first name, last name, phone number\n"
                + "\tusertype - text, purchase/sales, mandatory\n"
                + "\tusername - text, min 3 - 30 char, mandatory\n"
                + "\tpassword - text, alphanumeric, special char, min 8 char, mandatory\n"
                + "\tfirstname - text, mandatory with 3 to 30 chars\n"
                + "\tlastname  - text, optional\n"
                + "\tphone - number, mandatory, ten digits, digit start with 9/8/7/6\t\t\t\t\n");
        System.out.println("\n");
        System.out.print(">> Enter the Admin Username: ");
        userName = scanner.nextLine();
        while (true) {
          System.out.print(">> Enter the password: ");
          passWord = scanner.nextLine();
          System.out.print(">> Renter the password: ");
          if (scanner.nextLine().equals(passWord)) break;
          else System.out.println("Passwords do not Match!-- Re-enter");
        }
        System.out.print(">> Enter Your Firstname: ");
        firstName=scanner.nextLine();
        System.out.print(">> Enter Your LastName: ");
        lastName=scanner.nextLine();
        System.out.print(">> Enter your phone-number: ");
        while(true){
        try{
          phoneNumber=Long.parseLong(scanner.nextLine());
          break;
        }catch(Exception e)
        {
          System.out.println("Phone number must be numeric! -- Re-enter");
        }
        }
        User user=new User("Admin",userName,passWord,firstName,lastName,phoneNumber);
        User createdUser=loginService.createUser(user);
        if(createdUser!=null)
        {
          System.out.println(">> Admin Created Successfully!!");
          System.out.println(">> You need to Login to Continue to the Billing Software Setup");
          Login();
        }
        else{
          System.out.println("\n\n>> Invalid format of attributes given for user!!");
          System.out.println(">> Please Try Again!!\n\n");
        }
      }while(true);
    }
    else {
          Login();
    }
  }

  private static void Login()
      throws SQLException, PageCountOutOfBoundsException, ApplicationErrorException {
    System.out.println("Please Login To continue to the Billing Software!!\n");
    do{
    System.out.print(">> Enter UserName: ");
    userName=scanner.nextLine();
    System.out.print(">> Enter the Password: ");
    passWord=scanner.nextLine();
    String userType;
    try{
     userType=loginService.login(userName,passWord);
    }
    catch(Exception e)
    {
      System.out.println(e.getMessage());
      return;
    }
    if(userType!=null)
    {
      System.out.print("\n\n\n\t\t\t\t\t____________WELCOME "+userName);
      /**
       * Split control Here
       */
      if(userType.equalsIgnoreCase("Admin"))
        AdminMain.AdminView();
      else if(userType.equalsIgnoreCase("Sales"))
        SalesMain.SalesView();
      else if(userType.equalsIgnoreCase("Purchase"))
        PurchaseMain.PurchaseView();
    }
    else{
      System.out.println(">> Login credentials invalid ! You should try with a valid user name and password. If you have any issues contact software administrator.");
    }
    }while(true);
  }
}

