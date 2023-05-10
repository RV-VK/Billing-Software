package Entity;

public class SalesItem {
    private Product product;
    private double quantity;
    private double unitSalesPrice;

    public SalesItem (Product product, double quantity, double unitSalesPrice) {
        this.product = product;
        this.quantity = quantity;
        this.unitSalesPrice = unitSalesPrice;
    }

    public Product getProduct () {
        return product;
    }

    public void setProduct (Product product) {
        this.product = product;
    }

    public double getQuantity () {
        return quantity;
    }

    public void setQuantity (double quantity) {
        this.quantity = quantity;
    }

    public double getUnitSalesPrice () {
        return unitSalesPrice;
    }

    public void setUnitSalesPrice (double unitSalesPrice) {
        this.unitSalesPrice = unitSalesPrice;
    }

    @Override
    public String toString () {
        return "SalesItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", unitSalesPrice=" + unitSalesPrice +
                '}';
    }
}
