package Service;

import Entity.Sales;
import java.util.HashMap;
import java.util.List;

public interface SalesService {
    Sales createSalesService (Sales sales);

    int countSalesService (String parameter);

    List<Sales> listSalesService (HashMap<String, String> listAttributes);

    int deleteSalesService (String id);
}
