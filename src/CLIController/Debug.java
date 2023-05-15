package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.Product;
import Entity.Unit;

import java.sql.SQLException;

public class Debug {
  public static void main(String[] args)
      throws SQLException, ApplicationErrorException, PageCountOutOfBoundsException {

    String regex="^store create ([a-zA-Z ]{3,30}), ([9|8|7|6][0-9]{9}), (.*), (\\d{15})$";
    String command="store create ABCStores, 6383874789, 130-B Lakshmana perumal koil street, 123456789012345";
    System.out.println(command.matches(regex));
  }
}
