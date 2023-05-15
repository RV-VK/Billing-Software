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
  private static LoginService loginService = new LoginServiceImplementation();
  private static Scanner scanner = new Scanner(System.in);
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
                "*********************************************************\n"
                        + "\t*********************   WELCOME   ***********************\n"
                        + "\t*********************************************************");
        System.out.println(
                "> Welcome to the Billing software setup. You have to create admin user to continue with the setup.");
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
          System.out.print(">> Enter the password:");
          passWord = scanner.nextLine();
          System.out.print(">> Renter the Passowrd: ");
          if (scanner.nextLine().equals(passWord)) break;
          else System.out.println("Passwords do not Match!-- Re-enter");
        }
        System.out.print(">> Enter Your Firstname: ");
        firstName=scanner.nextLine();
        System.out.println(">> Enter Your LastName: ");
        lastName=scanner.nextLine();
        System.out.println(">> Enter your phone-number: ");
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
          System.out.println(">> Invalid format of attributes given for user!!");
          System.out.println(">> Please Try Again!!");
        }
      }while(true);
    }
    else {
          Login();
    }
  }
  private static void Login() throws SQLException, PageCountOutOfBoundsException, ApplicationErrorException {
    System.out.print(">> Enter UserName: ");
    userName=scanner.nextLine();
    System.out.println(">> Enter the Password!!");
    passWord=scanner.nextLine();
    if(loginService.login(userName,passWord)!=null)
    {
      System.out.println("____________WELCOME "+userName+"_______________");
      //Split Control Here**
    }
    else{
      System.out.println(">> Login credentials invalid ! You should try with a valid user name and password. If you have any issues contact software administrator.");
    }
  }
}

