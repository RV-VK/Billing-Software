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

  public Product() {}

  public Product(
      int id,
      String code,
      String name,
      String unitcode,
      String type,
      float availableQuantity,
      double price) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.unitcode = unitcode;
    this.type = type;
    this.availableQuantity = availableQuantity;
    this.price = price;
  }

  public Product(
      String code,
      String name,
      String unitcode,
      String type,
      float availableQuantity,
      double price,
      double costPrice) {
    this.code = code;
    this.name = name;
    this.unitcode = unitcode;
    this.type = type;
    this.availableQuantity = availableQuantity;
    this.price = price;
  }

  public Product(
      String code,
      String name,
      String unitcode,
      String type,
      float availableQuantity,
      double price) {
    this.code = code;
    this.name = name;
    this.unitcode = unitcode;
    this.type = type;
    this.availableQuantity = availableQuantity;
    this.price = price;
  }

  public Product(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public Product(String code) {
    this.code = code;
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




  @Override
  public String toString() {
    return "Product{"
        + "id="
        + id
        + ", code='"
        + code
        + '\''
        + ", name='"
        + name
        + '\''
        + ", unitcode='"
        + unitcode
        + '\''
        + ", type='"
        + type
        + '\''
        + ", availableQuantity="
        + availableQuantity
        + ", price="
        + price+
        '}';
  }

 }