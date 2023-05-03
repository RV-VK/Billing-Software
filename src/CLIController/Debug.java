package CLIController;

import DAO.ApplicationErrorException;
import Entity.User;
import Service.ProductService;
import Service.ProductServiceImplementation;
import Service.UserService;
import Service.UserServiceImplementation;

import java.sql.SQLException;

public class Debug {
    public static void main(String[] args) throws SQLException, ApplicationErrorException {

        String command = "product edit id: 1, code: hp11, name: hamam, unitcode: pcs, type: soap, price: 30";

        String[] parts = command.split("\\s+");
//        String[] products = parts[].split(",\\s*");

// Print the individual words
        for (String product : parts) {
            System.out.println(product);
        }
    }
}
