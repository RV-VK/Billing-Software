package DAO;

public class ApplicationErrorException extends Exception {
  public ApplicationErrorException(String message) {
    super(message);
  }
}
