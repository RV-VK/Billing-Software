package Entity;

import DBConnection.DBHelper;

import java.sql.*;

public class Product {
    private int id;
    private String code;
    private String name;
    private String unitcode;
    private String type;
    private float availableQuantity;
    private double price;
    private double costPrice;

    public Product(){}
    public Product(int id,String code, String name, String unitcode, String type, float availableQuantity, double price, double costPrice) {
        this.id=id;
        this.code = code;
        this.name = name;
        this.unitcode = unitcode;
        this.type = type;
        this.availableQuantity = availableQuantity;
        this.price = price;
        this.costPrice = costPrice;
    }
    public Product(String code, String name, String unitcode, String type, float availableQuantity, double price, double costPrice) {
        this.code = code;
        this.name = name;
        this.unitcode = unitcode;
        this.type = type;
        this.availableQuantity = availableQuantity;
        this.price = price;
        this.costPrice = costPrice;
    }

    public Product(String code, String name, String unitcode, String type, float availableQuantity, double price) {
        this.code = code;
        this.name = name;
        this.unitcode = unitcode;
        this.type = type;
        this.availableQuantity = availableQuantity;
        this.price = price;
    }
    public Product(String code,String name)
    {
        this.code=code;
        this.name=name;
    }
    public Product(String code)
    {
        this.code=code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getunitcode() {
        return unitcode;
    }

    public void setunitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(float availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", unitcode='" + unitcode + '\'' +
                ", type='" + type + '\'' +
                ", availableQuantity=" + availableQuantity +
                ", price=" + price +
                ", costPrice=" + costPrice +
                '}';
    }

    public static void create(Product product) throws SQLException {
        Connection productCreateConnection = DBHelper.getConnection();
        try {
            productCreateConnection.setAutoCommit(false);
            String codeQuery = "SELECT * FROM PRODUCT WHERE CODE='" +product.getCode()+"'";
            Statement codeRepeatCheckStatement = productCreateConnection.createStatement();
            ResultSet codeResultSet=codeRepeatCheckStatement.executeQuery(codeQuery);
            if(codeResultSet.next())
            {
                System.out.println(">>Entity.Product code Already exists!!! Please try with a unique Entity.Product code");
            }
            else {
                PreparedStatement productCreateStatement=productCreateConnection.prepareStatement("INSERT INTO PRODUCT(CODE,NAME,unitcode,TYPE,PRICE,STOCK) VALUES (?,?,?,?,?,?)");
                productCreateStatement.setString(1,product.getCode());
                productCreateStatement.setString(2,product.getName());
                productCreateStatement.setString(3, product.getunitcode());
                productCreateStatement.setString(4,product.getType());
                productCreateStatement.setDouble(5,product.getPrice());
                productCreateStatement.setFloat(6,product.getAvailableQuantity());
                productCreateStatement.executeUpdate();
                productCreateConnection.commit();
                System.out.println(">>Entity.Product added Successfully !!!");
            }
        }
        catch(Exception e){
            System.out.println(">>"+"Arguments didnt match the constraints");
            System.out.println(">>"+"Try \"product create help\" for proper syntax");
            productCreateConnection.rollback();
    }

    }
    public static void count()
    {
        Connection getCountConnection= DBHelper.getConnection();
        try {
            Statement countStatement = getCountConnection.createStatement();
            ResultSet countResultSet=countStatement.executeQuery("SELECT COUNT(ID) FROM PRODUCT");
            int count=0;
            while(countResultSet.next()) {
                count = countResultSet.getInt(1);
            }
            System.out.println(">> Entity.Product count : "+count);
        }
        catch(Exception e) {
            System.out.println(">> Something went Wrong! Please Try Again");
    }
    }

    public static void list()
    {
        Connection listConnection= DBHelper.getConnection();
        try{
            Statement listStatement=listConnection.createStatement();
            ResultSet listresultSet=listStatement.executeQuery("SELECT * FROM PRODUCT ORDER BY ID LIMIT 20 ");
            while(listresultSet.next()) {
                System.out.println(">> id: "+listresultSet.getInt(1)+", code: "+listresultSet.getString(2)+", name: "+listresultSet.getString(3)+", type: "+listresultSet.getString(4)+", unitcode: "+listresultSet.getString(5)+", quantity: "+listresultSet.getFloat(6)+", price: "+listresultSet.getDouble(7)+", costprice: "+listresultSet.getDouble(8));
            }
        }
        catch(Exception e)
        {
            System.out.println(">>"+e);
        }

    }

    public static void list(int pageLength)
    {
        Connection listConnection= DBHelper.getConnection();
        try{
            Statement listStatement=listConnection.createStatement();
            ResultSet listresultSet=listStatement.executeQuery("SELECT * FROM PRODUCT ORDER BY ID");
            int pageCount=1;
            int pageContentLength=1;
            System.out.println(">> page "+pageCount);
            while(listresultSet.next()) {
                System.out.println(">> id: "+listresultSet.getInt(1)+", code: "+listresultSet.getString(2)+", name: "+listresultSet.getString(3)+", type: "+listresultSet.getString(4)+", unitcode: "+listresultSet.getString(5)+", quantity: "+listresultSet.getFloat(6)+", price: "+listresultSet.getDouble(7)+", costprice: "+listresultSet.getDouble(8));
                if(pageContentLength%pageLength==0)
                {
                    pageCount++;
                    System.out.println("\n\n\n");
                    System.out.println(">> page "+pageCount);
                }
                pageContentLength++;
            }
        }
        catch(Exception e)
        {
            System.out.println(">>"+e);
        }

    }
    public static  void list(int pageLength,int pageNumber)
    {
        Connection listConnection= DBHelper.getConnection();
        int count=0;
        try {
            Statement countStatement = listConnection.createStatement();
            ResultSet countResultSet=countStatement.executeQuery("SELECT COUNT(ID) FROM PRODUCT");
            count=0;
            while(countResultSet.next()) {
                count = countResultSet.getInt(1);
            }
        }
        catch(Exception e) {
            System.out.println(">> Something went Wrong! Please Try Again");
        }
        if(count<=((pageLength*pageNumber)-pageLength))
        {
            System.out.println(">> Requested page doesnt exist !!!");
            System.out.println(">> Existing page count with given pagination "+(count/pageLength)+1);
        }
        else
        {
            try {
                int begin=(pageLength*pageNumber)-pageLength;
                int end=(pageLength*pageNumber);
                PreparedStatement listStatement = listConnection.prepareStatement("SELECT * FROM PRODUCT WHERE ID BETWEEN ? AND ?");
                listStatement.setInt(1,begin);
                listStatement.setInt(2,end);
                ResultSet listResultSet=listStatement.executeQuery();
                System.out.println("page "+pageNumber+" ("+begin+"-"+end+")");
                while(listResultSet.next())
                {
                    System.out.println(">> id: "+listResultSet.getInt(1)+", code: "+listResultSet.getString(2)+", name: "+listResultSet.getString(3)+", type: "+listResultSet.getString(4)+", unitcode: "+listResultSet.getString(5)+", quantity: "+listResultSet.getFloat(6)+", price: "+listResultSet.getDouble(7)+", costprice: "+listResultSet.getDouble(8));
                }
            }
            catch(Exception e) {
                System.out.println(">> Something Went Wrong !!!");
        }
        }
        }

        public static void list(String attribute,String searchText)
        {
            Connection listConnection= DBHelper.getConnection();
            try{
                Statement listStatement=listConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                String listQuery="SELECT * FROM PRODUCT WHERE "+attribute+" = '"+searchText+"'"+" ORDER BY ID LIMIT 20";
                ResultSet listResultSet=listStatement.executeQuery(listQuery);
                if(listResultSet.next()) {
                    listResultSet.beforeFirst();
                    while (listResultSet.next()) {
                        System.out.println(">> id: " + listResultSet.getInt(1) + ", code: " + listResultSet.getString(2) + ", name: " + listResultSet.getString(3) + ", type: " + listResultSet.getString(4) + ", unitcode: " + listResultSet.getString(5) + ", quantity: " + listResultSet.getFloat(6) + ", price: " + listResultSet.getDouble(7) + ", costprice: " + listResultSet.getDouble(8));
                    }
                }
                else {
                    System.out.println(">> SearchText not Found ! Please try with an existing attribute value");
                }
            }
            catch(Exception e)
            {
                System.out.println(">> "+e);
            }
        }

         public static void list(String attribute,String searchText,int pageLength)
         {
             Connection listConnection= DBHelper.getConnection();

             try{
                 Statement listStatement=listConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                 String listQuery="SELECT * FROM PRODUCT WHERE "+attribute+" = '"+searchText+"'"+" ORDER BY ID";
                 ResultSet listResultSet=listStatement.executeQuery(listQuery);
                 if(listResultSet.next()) {
                     listResultSet.beforeFirst();
                     int pageCount=1;
                     int pageContentLength=1;
                     System.out.println(">> page "+pageCount);
                     while (listResultSet.next()) {
                         System.out.println(">> id: " + listResultSet.getInt(1) + ", code: " + listResultSet.getString(2) + ", name: " + listResultSet.getString(3) + ", type: " + listResultSet.getString(5) + ", unitcode: " + listResultSet.getString(4) + ", quantity: " + listResultSet.getFloat(6) + ", price: " + listResultSet.getDouble(7) + ", costprice: " + listResultSet.getDouble(8));
                         if(pageContentLength%pageLength==0)
                         {
                             pageCount++;
                             System.out.println("\n\n\n");
                             System.out.println(">> page "+pageCount);
                         }
                         pageContentLength++;
                     }
                 }
                 else {
                     System.out.println(">> SearchText not Found ! Please try with an existing attribute value");
                 }
             }
             catch(Exception e)
             {
                 System.out.println(">> "+e);
             }

         }
         public static void list(String attribute,String searchText,int pageLength,int pageNumber )
         {
             Connection listConnection= DBHelper.getConnection();
             int rowCount;
                 try {
                     Statement listStatement = listConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                     String listQuery = "SELECT * FROM PRODUCT WHERE "+attribute+" = '"+searchText+"'"+" ORDER BY ID";
                     ResultSet listResultSet = listStatement.executeQuery(listQuery);
                     if(listResultSet.next()) {
                         listResultSet.last();
                         rowCount = listResultSet.getRow();
                         listResultSet.beforeFirst();
                         if (rowCount > ((pageLength * pageNumber)-pageLength)) {
                             int rowPosition = (pageLength * pageNumber) - pageLength;
                             listResultSet.absolute(rowPosition-1);
                             int index=0;
                             System.out.println(">> page : "+pageNumber);
                             while (listResultSet.next() && index < pageLength)
                             {
                                 System.out.println(">> id: " + listResultSet.getInt(1) + ", code: " + listResultSet.getString(2) + ", name: " + listResultSet.getString(3) + ", type: " + listResultSet.getString(4) + ", unitcode: " + listResultSet.getString(5) + ", quantity: " + listResultSet.getFloat(6) + ", price: " + listResultSet.getDouble(7) + ", costprice: " + listResultSet.getDouble(8));
                                 index++;
                             }
                         } else {
                             System.out.println(">> Requested page doesnt exist !!!");
                             System.out.println(">> Existing page count with given pagination " + ((rowCount/pageLength)+1));
                         }
                     }
                     else {
                         System.out.println(">> SearchText not Found ! Please try with an existing attribute value");
                     }
                 } catch (Exception e) {
                     System.out.println(">> " + e);
                     e.printStackTrace();
                 }

         }
         public static void edit(int id,String attribute,String value) throws SQLException {
             Connection editConnection= DBHelper.getConnection();
             try{
                 editConnection.setAutoCommit(false);
                 String idCheckQuery="SELECT * FROM PRODUCT WHERE ID="+id;
                 Statement idCheckStatement=editConnection.createStatement();
                 ResultSet idCheckResultSet=idCheckStatement.executeQuery(idCheckQuery);
                 if(idCheckResultSet.next())
                 {
                     String editQuery="UPDATE PRODUCT SET "+attribute.toUpperCase()+"="+"'"+value+"'"+" WHERE ID="+id;
                     Statement editStatement=editConnection.createStatement();
                     editStatement.executeUpdate(editQuery);
                 }
                 else {
                     System.out.println(">> The id you have entered does not exist");
                     System.out.println(">> Please try with an existing id");
                 }
                 editConnection.commit();
             }
             catch(Exception e)
             {
                 e.printStackTrace();
                 System.out.println(">> Template Mismatch! Please Try \"product edit help\" for proper attribute Constraints");
                 editConnection.rollback();
             }
         }


    }



