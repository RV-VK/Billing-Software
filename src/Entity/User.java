package Entity;

public class User {
  private int id;
  private String userType;
  private String userName;
  private String passWord;
  private String firstName;
  private String lastName;
  private long phoneNumber;

  public User() {}

  public User(
      String userType,
      String userName,
      String passWord,
      String firstName,
      String lastName,
      long phoneNumber) {
    this.userType = userType;
    this.userName = userName;
    this.passWord = passWord;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
  }

  public User(
      int id,
      String userType,
      String userName,
      String passWord,
      String firstName,
      String lastName,
      long phoneNumber) {
    this.id = id;
    this.userType = userType;
    this.userName = userName;
    this.passWord = passWord;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassWord() {
    return passWord;
  }

  public void setPassWord(String passWord) {
    this.passWord = passWord;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public long getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(long phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public String toString() {
    return "User{"
        + "id="
        + id
        + ", userType='"
        + userType
        + '\''
        + ", userName='"
        + userName
        + '\''
        + ", passWord='"
        + passWord
        + '\''
        + ", firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", phoneNumber="
        + phoneNumber
        + '}';
  }
}
