package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;

import java.sql.SQLException;

public class Debug {
  public static void main(String[] args)
      throws SQLException, ApplicationErrorException, PageCountOutOfBoundsException {

    String input = "purchase 2023-05-09, 123, [item1, 2, 10.99], item2, 3, 8.99]";
    String[] entities = input.split(",\\s*(?=\\[)");
    for (String s : entities) {
      System.out.println(s);
    }
    System.out.println(entities.length);
    String item1 = entities[1].replaceAll("[\\[\\]]", "").trim();
    ;
    //        String item2=entities[2].replaceAll("[\\[\\]]", "").trim();;
    //        System.out.println(item1+" "+item2);
    String[] parts1 = item1.split(",");
    //        String[] parts2=item2.split(",");
    System.out.println(parts1.length);
    for (String s : parts1) {
      System.out.println(s);
    }
  }
}
