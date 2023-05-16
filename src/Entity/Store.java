package Entity;

public class Store {
  private String name;
  private long phoneNumber;
  private String address;
  private String gstCode;

  public Store() {}

  public Store(String name, long phoneNumber, String address, String gstCode) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.gstCode = gstCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(long phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getGstCode() {
    return gstCode;
  }

  public void setGstCode(String gstCode) {
    this.gstCode = gstCode;
  }
}
