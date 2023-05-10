package Entity;

import java.util.List;

public class Sales {
  private int id;
  private String date;
  private List<SalesItem> salesItemList;
  private double grandTotal;

  public Sales() {}

  public Sales(int id, String date, List<SalesItem> salesItemList, double grandTotal) {
    this.id = id;
    this.date = date;
    this.salesItemList = salesItemList;
    this.grandTotal = grandTotal;
  }

  public Sales(String date, List<SalesItem> salesItemList, double grandTotal) {
    this.date = date;
    this.salesItemList = salesItemList;
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

  public List<SalesItem> getSalesItemList() {
    return salesItemList;
  }

  public void setSalesItemList(List<SalesItem> salesItemList) {
    this.salesItemList = salesItemList;
  }

  public double getGrandTotal() {
    return grandTotal;
  }

  public void setGrandTotal(double grandTotal) {
    this.grandTotal = grandTotal;
  }

  @Override
  public String toString() {
    return "Sales{"
        + "id="
        + id
        + ", date='"
        + date
        + '\''
        + ", salesItemList="
        + salesItemList
        + ", grandTotal="
        + grandTotal
        + '}';
  }
}
