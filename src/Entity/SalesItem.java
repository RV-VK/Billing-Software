package Entity;

public class SalesItem {
  private Product product;
  private float quantity;
  private double unitSalesPrice;

  public SalesItem(Product product, float quantity, double unitSalesPrice) {
    this.product = product;
    this.quantity = quantity;
    this.unitSalesPrice = unitSalesPrice;
  }

  public SalesItem(Product product, float quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public float getQuantity() {
    return quantity;
  }

  public void setQuantity(float quantity) {
    this.quantity = quantity;
  }

  public double getUnitSalesPrice() {
    return unitSalesPrice;
  }

  public void setUnitSalesPrice(double unitSalesPrice) {
    this.unitSalesPrice = unitSalesPrice;
  }

  @Override
  public String toString() {
    return "SalesItem{"
        + "product="
        + product
        + ", quantity="
        + quantity
        + ", unitSalesPrice="
        + unitSalesPrice
        + '}';
  }
}
