package DAO;

import Entity.Unit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.SQLException;
import java.util.List;

public interface UnitDAO {
  @Select("INSERT INTO UNIT(NAME,CODE,DESCRIPTION,ISDIVIDABLE) VALUES (#{name},#{code},#{description},#{isDividable}) RETURNING *")
  Unit create(Unit unit) throws SQLException, ApplicationErrorException, UniqueConstraintException;

  @Select("SELECT * FROM UNIT ORDER BY CODE")
  List<Unit> list() throws ApplicationErrorException;

  @Update("UPDATE UNIT SET NAME= COALESCE(#{name},NAME), CODE=COALESCE(#{code},CODE), DESCRIPTION=COALESCE(#{description},DESCRIPTION), ISDIVIDABLE=COALESCE(#{isDividable},ISDIVIDABLE) WHERE ID=#{id}")
  int edit(Unit unit) throws ApplicationErrorException, SQLException, UniqueConstraintException;

  @Delete("DELETE FROM UNIT WHERE CODE=#{code}")
  int delete(String code) throws ApplicationErrorException;

  @Select("SELECT * FROM UNIT WHERE CODE=#{code}")
  Unit findByCode(String code) throws ApplicationErrorException;
}
