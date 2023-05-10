package Entity;

public class PurchaseItem {
  private Product product;
  private float quantity;
  private double unitPurchasePrice;

  public PurchaseItem(Product product, float quantity, double unitPurchasePrice) {
    this.product = product;
    this.quantity = quantity;
    this.unitPurchasePrice = unitPurchasePrice;
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

  public double getUnitPurchasePrice() {
    return unitPurchasePrice;
  }

  public void setUnitPurchasePrice(double unitPurchasePrice) {
    this.unitPurchasePrice = unitPurchasePrice;
  }

  @Override
  public String toString() {
    return "PurchaseItem{"
        + "product="
        + product
        + ", quantity="
        + quantity
        + ", unitPurchasePrice="
        + unitPurchasePrice
        + '}';
  }
}
