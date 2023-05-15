package CLIController;

import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import Entity.Product;
import Service.ProductService;
import Service.ProductServiceImplementation;

import java.util.*;

public class ProductCLI {
  private int id;
  private String code;
  private String name;
  private String unitCode;
  private String type;
  private double price;
  private float stock;
  private int pageLength;
  private int pageNumber;
  private String attribute;
  private String searchText;
  private List<Product> resultList;
  private final List<String> productAttributes =
      Arrays.asList("id", "code", "name", "unitcode", "type", "price", "stock", "costprice");
  private final ProductService productService = new ProductServiceImplementation();
  private final HashMap<String, String> listAttributesMap = new HashMap<>();
  private final Scanner scanner = new Scanner(System.in);
  private final String helpMessage = ">> Try \"product create help for proper syntax";

  /**
   * This method handles the presentation layer for the create function.
   *
   * @param arguments - List of Command arguments.
   */
  public void Create(List<String> arguments) {
    if (arguments.size() == 3 && arguments.get(2).equals("help")) {
      System.out.println(
          ">> create product using the following template\n"
              + ">> code, name, unit, type, price, stock\n"
              + "\t\n"
              + "\tcode - text, min - 2 - 6, mandatory\n"
              + "\tname - text, min 3 - 30 char, mandatory\n"
              + "\tunitcode - text, kg/l/piece/combo, mandatory\n"
              + "\ttype - text, between enumerated values, mandatory \n"
              + "\tprice - number, mandatory\n"
              + "\tstock - number, default 0\n"
              + "\t\n"
              + ">\tproduct create code, productname, unitcode, type, price, stock\n"
              + "                         or\n"
              + "> product create :enter\n"
              + "code, name, unitcode, type, price, stock\n");
      return;
    } else if (arguments.size() == 2) {
      System.out.print("> ");
      String parameters = scanner.nextLine();
      List<String> productAttributes = List.of(parameters.split(","));
      createHelper(productAttributes);
      return;
    }
    createHelper(arguments.subList(2, arguments.size()));
  }

  /**
   * This method serves the create function
   *
   * @param productAttributes
   */
  private void createHelper(List<String> productAttributes) {
    if (productAttributes.size() < 5) {
      System.out.println(">>Insufficient Arguments for command \"product create\"");
      System.out.println(helpMessage);
      return;
    } else if (productAttributes.size() > 6) {
      System.out.println(">>Too many arguments for command \"product create \"");
      System.out.println(helpMessage);
      return;
    }
    code = productAttributes.get(0).trim();
    name = productAttributes.get(1).trim();
    unitCode = productAttributes.get(2).trim();
    type = productAttributes.get(3).trim();
    try {
      price = Double.parseDouble(productAttributes.get(4).trim());
    } catch (Exception e) {
      System.out.println(">>Invalid format for 4th Argument \"price\"");
      System.out.println();
      return;
    }
    if (productAttributes.size() == 6) {
      try {
        stock = Float.parseFloat(productAttributes.get(5).trim());
      } catch (Exception e) {
        System.out.println(stock);
        System.out.println(">>Invalid format for 5th argument \"stock\"");
        System.out.println(helpMessage);
        return;
      }
    }
    Product product = new Product(code, name, unitCode, type, stock, price);
    Product createdProduct;
    try {
      createdProduct = productService.createProductService(product);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return;
    }
    if (createdProduct.getName() == null) {
      System.out.println(">> Template Mismatch!!");
      System.out.println(">> Try \"product create help\" for proper syntax");
    } else if (createdProduct != null) {
      System.out.println(">> Product Creation Successfull!!");
      System.out.println(product);
    }
  }

  /**
   * This method handles the Presentation layer of the List function.
   *
   * @param arguments - List of Command Arguments.
   * @throws PageCountOutOfBoundsException
   * @throws ApplicationErrorException
   */
  public void list(List<String> arguments)
      throws PageCountOutOfBoundsException, ApplicationErrorException {
    listAttributesMap.put("Pagelength", null);
    listAttributesMap.put("Pagenumber", null);
    listAttributesMap.put("Attribute", null);
    listAttributesMap.put("Searchtext", null);
    if (arguments.size() == 3 && arguments.get(2).equals("help")) {
      System.out.println(
          ">> List product with the following options\n"
              + ">> product list - will list all the products default to maximum upto 20 products\n"
              + ">> product list -p 10 - pageable list shows 10 products as default\n"
              + ">> product list -p 10 3 - pagable list shows 10 products in 3rd page, ie., product from 21 to 30\n"
              + ">> product list -s searchtext - search the product with the given search text in all the searchable attributes\n"
              + ">> product list -s <attr>: searchtext - search the product with the given search text in all the given attribute\n"
              + ">> product list -s <attr>: searchtext -p 10 6 - pagable list shows 10 products in 6th page with the given search text in the given attribute\n");
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
          System.out.println(">> Invalid page Size input");
          System.out.println(">> Try \"product list help\" for proper syntax");
        }
        listAttributesMap.put("Pagelength", String.valueOf(pageLength));
        listAttributesMap.put("Pagenumber", String.valueOf(1));
        listAttributesMap.put("Attribute", "id");
        listHelper(listAttributesMap);
      } else if (arguments.get(2).equals("-s")) {
        searchText = arguments.get(3).trim();
        listAttributesMap.put("Searchtext", searchText);
        listHelper(listAttributesMap);
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"product list help\" for proper syntax");
      }
    } else if (arguments.size() == 5) {
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
        if (productAttributes.contains(attribute)) {
          listAttributesMap.put("Attribute", attribute);
          listAttributesMap.put("Searchtext", searchText);
          listAttributesMap.put("Pagelength", "20");
          listAttributesMap.put("Pagenumber", String.valueOf(1));
          resultList = productService.listProductService(listAttributesMap);
          listHelper(listAttributesMap);
        } else {
          System.out.println("Given attribute is not a searchable attribute!!");
          System.out.println("Try \"product list help\" for proper syntax");
        }
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"product list help\" for proper syntax");
      }
    } else if (arguments.size() == 7) {
      if (arguments.get(2).equals("-s")) {
        attribute = arguments.get(3);
        attribute = attribute.replace(":", "");
        searchText = arguments.get(4);
        listAttributesMap.put("Attribute", attribute);
        listAttributesMap.put("Searchtext", searchText);
        if (productAttributes.contains(attribute)) {
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
            resultList = productService.listProductService(listAttributesMap);
            listHelper(listAttributesMap);
          } else {
            System.out.println(">> Invalid Command Extension format !!!");
            System.out.println("Try \"product list help\" for proper syntax");
          }
        } else {
          System.out.println("Given attribute is not a searchable attribute!!");
          System.out.println("Try \"product list help\" for proper syntax");
        }
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"product list help\" for proper syntax");
      }
    } else if (arguments.size() == 8) {
      if (arguments.get(2).equals("-s")) {
        attribute = arguments.get(3);
        attribute = attribute.replace(":", "");
        searchText = arguments.get(4);
        listAttributesMap.put("Attribute", attribute);
        listAttributesMap.put("Searchtext", searchText);
        if (productAttributes.contains(attribute)) {
          if (arguments.get(5).equals("-p")) {
            try {
              pageLength = Integer.parseInt(arguments.get(6));
              pageNumber = Integer.parseInt(arguments.get(7));
            } catch (Exception e) {
              System.out.println(">> Invalid page Size (or) page Number input");
              System.out.println(">> Try \"product list help\" for proper syntax");
              return;
            }
            listAttributesMap.put("Pagelength", String.valueOf(pageLength));
            listAttributesMap.put("Pagenumber", String.valueOf(pageNumber));
            resultList = productService.listProductService(listAttributesMap);
            listHelper(listAttributesMap);
          } else {
            System.out.println("Invalid Extension Given!!!");
            System.out.println("Try \"product list help\" for proper syntax");
          }
        } else {
          System.out.println("Given attribute is not a searchable attribute!!");
          System.out.println("Try \"product list help\" for proper syntax");
        }
      } else {
        System.out.println(">> Invalid Extension given");
        System.out.println(">> Try \"product list help\" for proper syntax");
      }
    } else if (arguments.size() == 3) {
      System.out.println("Invalid command format!!!");
      System.out.println(">> Try \"product list help\" for proper syntax");
    } else {
      System.out.println("Invalid command format!!!");
      System.out.println(">> Try \"product list help\" for proper syntax");
    }
  }

  /**
   * This method serves the List function.
   *
   * @param listAttributesMap
   * @throws PageCountOutOfBoundsException
   * @throws ApplicationErrorException
   */
  private void listHelper(HashMap<String, String> listAttributesMap)
      throws PageCountOutOfBoundsException, ApplicationErrorException {
    resultList = productService.listProductService(listAttributesMap);
    if (resultList.size() == 0) {
      System.out.println(">> Given SearchText does not exist!!!");
    }
    for (Product resultProduct : resultList) {
      System.out.println(
          ">> id: "
              + resultProduct.getId()
              + ", code: "
              + resultProduct.getCode()
              + ", name: "
              + resultProduct.getName()
              + ", type: "
              + resultProduct.getType()
              + ", unitcode: "
              + resultProduct.getunitcode()
              + ", stock: "
              + resultProduct.getAvailableQuantity()
              + ", price: "
              + resultProduct.getPrice()
              + ", costprice: "
              + resultProduct.getCostPrice());
    }
  }

  /**
   * This method handles the Presentation Layer of the Count function.
   *
   * @throws ApplicationErrorException
   */
  public void count() throws ApplicationErrorException {
    ProductService countProduct = new ProductServiceImplementation();
    int productCount = countProduct.countProductService();
    System.out.println(">> ProductCount " + productCount);
  }

  /**
   * This method handles the Presentation Layer of the Edit function.
   *
   * @param arguments - List of Command arguments
   */
  public void edit(List<String> arguments) {
    Scanner scanner = new Scanner(System.in);
    if (arguments.size() == 3 && arguments.get(2).equals("help")) {
      System.out.println(
          ">> Edit product using following template. Copy the product data from the list, edit the attribute values. \n"
              + ">> id: <id - 6>, name: <name-edited>, unitcode: <unitcode>,  type: <type>, price: <price>\n"
              + "\n"
              + ">> You can also restrict the product data by editable attributes. Id attribute is mandatory for all the edit operation.\n"
              + ">> id: <id - 6>, name: <name-edited>, unitcode: <unitcode-edited>\n"
              + "\n"
              + ">> You can not give empty or null values to the mandatory attributes.\n"
              + ">> id: <id - 6>, name: , unitcode: null\n"
              + ">>\n"
              + " \n"
              + " \tid\t - number, mandatory\t\n"
              + "\tname - text, min 3 - 30 char, mandatory\n"
              + "\tunitcode - text, kg/l/piece/combo, mandatory\n"
              + "\ttype - text, between enumerated values, mandatory \n"
              + "\tcostprice - numeric, mandatory\n"
              + "\t\n"
              + ">\tproduct edit id:<id - 6>, name: <name-edited>, unitcode: <unitcode>,  type: <type>, price: <price>\n"
              + "                         or\n"
              + "> product edit :enter\n"
              + "> id: <id - 6>, name: <name-edited>, unitcode: <unitcode>,  type: <type>, price: <price>");
    } else if (arguments.size() == 2) {
      System.out.print("> ");
      String parameters = scanner.nextLine();
      String[] productAttributes = parameters.split(",");
      List<String> splitAttributes = new ArrayList<>();
      for (String string : productAttributes) {
        String[] keyValues = string.split(":");
        splitAttributes.add(keyValues[0]);
        splitAttributes.add(keyValues[1]);
      }
      editHelper(splitAttributes);
    } else if (arguments.size() > 14) {
      System.out.println(">>Too many Arguments for command \"product edit\"");
    } else if (arguments.size() < 6) {
      System.out.println(">>Insufficient Arguments for command \"product edit\"");
    } else if (!arguments.get(2).contains("id")) {
      System.out.println(">> Id is a Mandatory argument for every Edit operation");
      System.out.println(">> For every Edit operation the first argument must be product's ID");
      System.out.println(">> Try \"product edit help\" for proper syntax");
    } else {
      editHelper(arguments.subList(2, arguments.size()));
    }
  }

  /**
   * This method serves the Edit function
   *
   * @param editAttributes
   */
  private void editHelper(List<String> editAttributes) {
    Product product = new Product();
    try {
      id = Integer.parseInt(editAttributes.get(1).trim());
    } catch (Exception e) {
      System.out.println(">> Id must be a Number!");
      System.out.println(">> Please Try \"product edit help\" for proper Syntax");
    }
    product.setId(id);
    if (product.getId() == 0) {
      System.out.println(">> Id should not be null");
      System.out.println(">> Try \"product edit help\" for proper Syntax");
      return;
    }
    for (int index = 2; index < editAttributes.size(); index = index + 2) {
      if (editAttributes.get(index).contains("name")) {
        product.setName(editAttributes.get(index + 1).trim());
      } else if (editAttributes.get(index).contains("code")
          && !editAttributes.get(index).contains("unitcode")) {
        product.setCode(editAttributes.get(index + 1).trim());
      } else if (editAttributes.get(index).contains("unitcode")) {
        product.setunitcode(editAttributes.get(index + 1).trim());
      } else if (editAttributes.get(index).contains("type")) {
        product.setType(editAttributes.get(index + 1).trim());
      } else if (editAttributes.get(index).contains("price")) {
        float price;
        try {
          price = Float.parseFloat(editAttributes.get(index + 1));
        } catch (Exception e) {
          System.out.println(">> Price attribute must be a number");
          return;
        }
        product.setPrice(price);
      } else {
        System.out.println(">> Invalid attribute given!!! : " + editAttributes.get(index));
        System.out.println(">> Try \"product edit help\" for proper syntax");
        break;
      }
    }
    int statusCode;
    try {
      statusCode = productService.editProductService(product);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return;
    }
    if (statusCode == 1) {
      System.out.println(">> Product Edited Succesfully");
    } else if (statusCode == -1) {
      System.out.println(">> Product edit failed!!!");
      System.out.println(">>Please check the Id you have entered!!!");
    } else if (statusCode == 0) {
      System.out.println(">>Invalid format of attributes given for edit Command!!!");
      System.out.println(">>Try \"product edit help\" for proper syntax");
    }
  }

  /**
   * This method handles the Presentation Layer of the Delete function.
   *
   * @param arguments - List of Command arguments.
   * @throws ApplicationErrorException
   */
  public void delete(List<String> arguments) throws ApplicationErrorException {
    String numberRegex = "^[0-9]*$";
    String productcodeRegex = "^[a-zA-Z0-9]{2,6}$";
    if (arguments.size() == 3) {
      if (arguments.get(2).equals("help")) {
        System.out.println(
            "> product delete help \n"
                + ">> delete product using the following template\n"
                + "\t\n"
                + "\t\tproductid - numeric, existing\n"
                + ">> product delete -c <code>\n"
                + "\t \n"
                + "\n"
                + "> product delete <id>");
      } else if (arguments.get(2).matches(numberRegex)) {
        deleteHelper(arguments);
      } else {
        System.out.println(">> Invalid format for id!!!");
        System.out.println("Try \"product delete help\" for proper syntax");
      }
    } else if (arguments.size() == 4 && arguments.get(2).equals("-c")) {
      if (arguments.get(3).matches(productcodeRegex)) {
        deleteHelper(arguments);
      } else {
        System.out.println(">> Invalid format for product Code!!!");
        System.out.println("Try \"product delete help\" for proper syntax");
      }
    } else {
      System.out.println("Invalid command format");
      System.out.println("Try \"product delete help\" for proper syntax");
    }
  }

  /**
   * This method serves the Delete function
   *
   * @param arguments
   * @throws ApplicationErrorException
   */
  private void deleteHelper(List<String> arguments) throws ApplicationErrorException {
    System.out.println(">> Are you sure want to delete the product y/n ? : ");
    String prompt = scanner.nextLine();
    if (prompt.equals("y")) {
      if (productService.deleteProductService(arguments.get(2)) == 1) {
        System.out.println("Product Deletion Successfull!!!");
      } else if (productService.deleteProductService(arguments.get(2)) == -1) {
        System.out.println(">> Product Deletion Failed!!!");
        System.out.println(">> Please check the Id  you have entered!!!");
        System.out.println("Try \"product delete help\" for proper syntax");
      } else if (productService.deleteProductService(arguments.get(2)) == 0) {
        System.out.println(">> Product cannot be deleted!!!");
        System.out.println(">>Selected Product has stock left and should not be deleted!!!");
        System.out.println(">>Please check the selected product to be deleted!!!");
      }
    } else if (prompt.equals("n")) {
      System.out.println(">> Delete operation cancelled");
    } else {
      System.out.println("Invalid delete prompt!!! Please select between y/n");
    }
  }
}
