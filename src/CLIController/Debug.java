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

        String createcommand="product create hp11, hamam soap, pcs, soap, 25, 0";
        String editcommand = "product edit id: 1, usertype: admin, username: mani, password: mara1205, firstname: RV, lastname: Mani, phonenumber: 6383874789";
        String listcommand="product list -s type: soap -p 10 5";
        String[] parts = createcommand.split("[, ]");
        for(int index=0;index< parts.length;index++)
        {
            parts[index]=parts[index].replace(",","");
        }
//        String[] products = parts[].split(",\\s*");

// Print the individual words
        for (String product : parts) {
            System.out.println(product);
        }
    }
}
