package CLIController;
import DAO.ApplicationErrorException;
import DAO.PageCountOutOfBoundsException;
import DAO.PurchaseDAO;
import DAO.PurchaseDAOImplementation;
import Entity.Purchase;
import Entity.PurchaseItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Debug {
    public static void main(String[] args) throws SQLException, ApplicationErrorException, PageCountOutOfBoundsException {

        String createcommand="product create hp11, hamam soap, pcs, soap, 25, 0";
        String editcommand = "product edit id: 1, usertype: admin, username: mani, password: mara1205, firstname: RV, lastname: Mani, phonenumber: 6383874789";
        String listcommand="purchase 08/05/23, 15982, [Oil01, 5, 65], [Oil02, 3, 45]";
        String[] parts ;
        String[] commandlet;
        if(listcommand.contains(",")) {
             parts = listcommand.split("[,:]");
             commandlet = parts[0].split(" ");
        }
        else{
            parts=listcommand.split(",");
             commandlet=listcommand.split(" ");
        }
        ArrayList<String> cmdlist=new ArrayList<>();
        if(parts.length==1)
        {
            for(int i=0;i<commandlet.length;i++)
            {
                cmdlist.add(i,commandlet[i]);
            }
        }
        else {
            Collections.addAll(cmdlist, commandlet);
            int j;
            cmdlist.addAll(Arrays.asList(parts).subList(1, parts.length));
        }
        //        for(int i=commandlet.length;i< parts.length-1;i++)
//        {
//            int j=1;
//            arguments[i]=parts[j];
//            j++;
//        }
//        for(String part:parts)
//        {
//            System.out.println(part);
//        }
//        for(int i=0;i<parts.length;i++)
//        {
//            if(parts[i].contains(","))
//            {
//                continue;
//            }
//            else if(!(parts[i].contains(","))&&i>2&&i!= parts.length-1){
//                StringBuilder stringBuilder=new StringBuilder();
//                stringBuilder.append(parts[i]+" ");
//                stringBuilder.append(parts[i+1]);
//                parts[i]=stringBuilder.toString();
//                stringBuilder.setLength(0);
//                i=i+2;
//            }
//        }
//        for(int index=0;index< parts.length;index++)
//        {
//            parts[index]=parts[index].replace(",","");
//        }

// Print the individual words
        for (String product : cmdlist) {
            System.out.println(product);
        }
        PurchaseDAO purchaseDAO=new PurchaseDAOImplementation();
        List<Purchase> Purchaselist=purchaseDAO.list("invoice","124",1,0);
        for(Purchase purchase:Purchaselist)
        {
            System.out.print(purchase.getId()+" "+purchase.getDate()+" "+purchase.getInvoice()+" ");
            for(PurchaseItem purchaseItem: purchase.getPurchaseItemList())
            {
                System.out.print(purchaseItem.getProduct().getCode()+" "+purchaseItem.getQuantity()+" "+purchaseItem.getUnitPurchasePrice()+" ");
            }
            System.out.println();

        }
    }
}
