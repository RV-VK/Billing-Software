package DAO;
import Entity.Product;
import java.sql.SQLException;
import java.util.List;
public interface ProductDAO {
  Product create(Product product)
          throws SQLException, ApplicationErrorException, UniqueNameException, UniqueConstraintException;
  int count() throws ApplicationErrorException;
  List list(String attribute, String searchText, int pageLength, int offset)
      throws ApplicationErrorException;
  List list(String searchText) throws ApplicationErrorException;
  boolean edit(Product product)
      throws SQLException,
          ApplicationErrorException,
          UniqueNameException,
          UniqueConstraintException,
          UnitCodeViolationException;
  int delete(String parameter) throws ApplicationErrorException;
  Product findByCode(String code) throws ApplicationErrorException;
}
