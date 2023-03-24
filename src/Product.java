import java.sql.*;

public class Product {
    private int id;
    private String code;
    private String name;
    private String unitCode;
    private String type;
    private float availableQuantity;
    private double price;
    private double costPrice;
    public Product(String code, String name, String unitCode, String type, float availableQuantity, double price, double costPrice) {
        this.code = code;
        this.name = name;
        this.unitCode = unitCode;
        this.type = type;
        this.availableQuantity = availableQuantity;
        this.price = price;
        this.costPrice = costPrice;
    }

    public Product(String code, String name, String unitCode, String type, float availableQuantity, double price) {
        this.code = code;
        this.name = name;
        this.unitCode = unitCode;
        this.type = type;
        this.availableQuantity = availableQuantity;
        this.price = price;
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

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
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

    public static void create(Product product) throws SQLException {
        Connection productCreateConnection = DBHelper.getConnection();
        try {
            productCreateConnection.setAutoCommit(false);
            String codeQuery = "SELECT * FROM PRODUCT WHERE CODE='" +product.getCode()+"'";
            Statement codeRepeatCheckStatement = productCreateConnection.createStatement();
            ResultSet codeResultSet=codeRepeatCheckStatement.executeQuery(codeQuery);
            if(codeResultSet.next())
            {
                System.out.println(">>Product code Already exists!!! Please try with a unique Product code");
            }
            else {
                PreparedStatement productCreateStatement=productCreateConnection.prepareStatement("INSERT INTO PRODUCT(CODE,NAME,UNITCODE,TYPE,PRICE,STOCK) VALUES (?,?,?,?,?,?)");
                productCreateStatement.setString(1,product.getCode());
                productCreateStatement.setString(2,product.getName());
                productCreateStatement.setString(3, product.getUnitCode());
                productCreateStatement.setString(4,product.getType());
                productCreateStatement.setDouble(5,product.getPrice());
                productCreateStatement.setFloat(6,product.getAvailableQuantity());
                productCreateStatement.executeUpdate();
                productCreateConnection.commit();
                System.out.println(">>Product added Successfully !!!");
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
        Connection getCountConnection=DBHelper.getConnection();
        try {
            Statement countStatement = getCountConnection.createStatement();
            ResultSet countResultSet=countStatement.executeQuery("SELECT COUNT(ID) FROM PRODUCT");
            int count=0;
            while(countResultSet.next()) {
                count = countResultSet.getInt(1);
            }
            System.out.println(">> Product count : "+count);
        }
        catch(Exception e) {
            System.out.println(">> Something went Wrong! Please Try Again");
    }
    }

    public static void list()
    {
        Connection listConnection=DBHelper.getConnection();
        try{
            Statement listStatement=listConnection.createStatement();
            ResultSet listresultSet=listStatement.executeQuery("SELECT * FROM PRODUCT ORDER BY ID LIMIT 20 ");
            while(listresultSet.next()) {
                System.out.println(">> id: "+listresultSet.getInt(1)+", productCode: "+listresultSet.getString(2)+", productname: "+listresultSet.getString(3)+", type: "+listresultSet.getString(4)+", unitCode: "+listresultSet.getString(5)+", quantity: "+listresultSet.getFloat(6)+", price: "+listresultSet.getDouble(7)+", costprice: "+listresultSet.getDouble(8));
            }
        }
        catch(Exception e)
        {
            System.out.println(">>"+e);
        }

    }

    public static void list(int pageLength)
    {
        Connection listConnection=DBHelper.getConnection();
        try{
            Statement listStatement=listConnection.createStatement();
            ResultSet listresultSet=listStatement.executeQuery("SELECT * FROM PRODUCT ORDER BY ID");
            int pageCount=1;
            int pageContentLength=1;
            System.out.println(">> page "+pageCount);
            while(listresultSet.next()) {
                System.out.println(">> id: "+listresultSet.getInt(1)+", productCode: "+listresultSet.getString(2)+", productname: "+listresultSet.getString(3)+", type: "+listresultSet.getString(4)+", unitCode: "+listresultSet.getString(5)+", quantity: "+listresultSet.getFloat(6)+", price: "+listresultSet.getDouble(7)+", costprice: "+listresultSet.getDouble(8));
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
        Connection listConnection=DBHelper.getConnection();
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
                    System.out.println(">> id: "+listResultSet.getInt(1)+", productCode: "+listResultSet.getString(2)+", productname: "+listResultSet.getString(3)+", type: "+listResultSet.getString(4)+", unitCode: "+listResultSet.getString(5)+", quantity: "+listResultSet.getFloat(6)+", price: "+listResultSet.getDouble(7)+", costprice: "+listResultSet.getDouble(8));
                }
            }
            catch(Exception e) {
                System.out.println(">> Something Went Wrong !!!");
        }
        }
        }

        public static void list(String attribute,String searchText)
        {
            Connection listConnection=DBHelper.getConnection();
            try{
                Statement listStatement=listConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                String listQuery="SELECT * FROM PRODUCT WHERE "+attribute+" = '"+searchText+"'"+" ORDER BY ID LIMIT 20";
                ResultSet listResultSet=listStatement.executeQuery(listQuery);
                if(listResultSet.next()) {
                    listResultSet.beforeFirst();
                    while (listResultSet.next()) {
                        System.out.println(">> id: " + listResultSet.getInt(1) + ", productCode: " + listResultSet.getString(2) + ", productname: " + listResultSet.getString(3) + ", type: " + listResultSet.getString(4) + ", unitCode: " + listResultSet.getString(5) + ", quantity: " + listResultSet.getFloat(6) + ", price: " + listResultSet.getDouble(7) + ", costprice: " + listResultSet.getDouble(8));
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


    }



