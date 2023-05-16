package CLIController;

import Service.ProductService;
import Service.ProductServiceImplementation;

import java.util.List;

public class StockPriceCLI {
    private ProductService productService=new ProductServiceImplementation();
  public void updateStock(List<String> arguments) {
    if (arguments.size() == 3) {
      if (arguments.get(2).equals("help")) {
        System.out.println(
            ">> update stock using following template\n"
                + "\t\tcode, quantity\n"
                + "\t\t\n"
                + "\t\tcode - text, mandatory\n"
                + "\t\tquantity - number, mandatory");
      } else {
        System.out.println("Invalid Command!");
        System.out.println("Try \"stock update help\" for proper syntax");
      }
    }
    else if(arguments.size()==4)
    {
        int resultCode;
        try{
        resultCode=productService.updateStock(arguments.get(2).trim(),arguments.get(3).trim());
        }
        catch(Exception e)
        {
        System.out.println(e.getMessage());
        return;
        }
        if(resultCode==-1)
        {
        System.out.println(">> Invalid Format of attributes Given!!");
        System.out.println(">> Try \"stock update help\" for proper syntax!!");
        }
        else if(resultCode==0)
        {
        System.out.println(">> The code you have entered does not exists!");
      }
        else System.out.println(">> Stock Updated Successfully!");
    }
    else{
      System.out.println("Invalid command format!");
    }
  }
  public void updatePrice(List<String> arguments) {
    if (arguments.size() == 3) {
      if (arguments.get(2).equals("help")) {
        System.out.println(
            ">> Update sales price per unit using the following template\n"
                + "\t\tcode, price\n"
                + "\t\t\n"
                + "\t\tcode - text, mandatory\n"
                + "\t\tprice - number, mandatory");
      } else {
        System.out.println("Invalid command! Not found!");
      }
    }
    else if(arguments.size()==4)
    {
        int resultCode;
        try{
            resultCode=productService.updatePrice(arguments.get(2).trim(),arguments.get(3).trim());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return;
        }
        if(resultCode==-1)
        {
            System.out.println(">> Invalid Format of attributes Given!!");
            System.out.println(">> Try \"price update help\" for proper syntax!!");
        }
        else if(resultCode==0)
        {
            System.out.println(">> The code you have entered does not exists!");
        }
        else System.out.println(">> Price Updated Successfully!");
    }
    else System.out.println("Invalid Command Format!");
  }
}
