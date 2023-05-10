package Entity;

import java.util.List;

public class Purchase {
  private int id;
  private String date;
  private int invoice;
  private List<PurchaseItem> purchaseItemList;
  private double grandTotal;

  public Purchase() {}
  ;

  public Purchase(
      int id, String date, int invoice, List<PurchaseItem> purchaseItemList, double grandTotal) {
    this.id = id;
    this.date = date;
    this.invoice = invoice;
    this.purchaseItemList = purchaseItemList;
    this.grandTotal = grandTotal;
  }

  public Purchase(
      String date, int invoice, List<PurchaseItem> purchaseItemList, double grandTotal) {
    this.date = date;
    this.invoice = invoice;
    this.purchaseItemList = purchaseItemList;
    this.grandTotal = grandTotal;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getInvoice() {
    return invoice;
  }

  public void setInvoice(int invoice) {
    this.invoice = invoice;
  }

  public List<PurchaseItem> getPurchaseItemList() {
    return purchaseItemList;
  }

  public void setPurchaseItemList(List<PurchaseItem> purchaseItemList) {
    this.purchaseItemList = purchaseItemList;
  }

  public double getGrandTotal() {
    return grandTotal;
  }

  public void setGrandTotal(double grandTotal) {
    this.grandTotal = grandTotal;
  }

  @Override
  public String toString() {
    return "Purchase{"
        + "id="
        + id
        + ", date='"
        + date
        + '\''
        + ", invoice="
        + invoice
        + ", purchaseItemList="
        + purchaseItemList
        + ", grandTotal="
        + grandTotal
        + '}';
  }
}
