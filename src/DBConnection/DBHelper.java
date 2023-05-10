package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBHelper {
  public static Connection getConnection() {
    Connection connection = null;
    try {
      Class.forName("org.postgresql.Driver");
      connection =
          DriverManager.getConnection(
              "jdbc:postgresql://localhost:5432/POSDatabase", "postgres", "manikantan1205");
    } catch (Exception e) {
      System.out.println("Application Encountered an Error!");
    }
    return connection;
  }
}
