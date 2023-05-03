package Service;
import DAO.ApplicationErrorException;
import DAO.UniqueConstraintException;
import DAO.UnitDAO;
import DAO.UnitDAOImplementation;
import Entity.Unit;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
public class UnitServiceImplementation implements UnitService{
    @Override
    public int createUnitService(Unit unit) throws SQLException, ApplicationErrorException {
        UnitDAO unitCreateDAO=new UnitDAOImplementation();
        Unit createdUnit=unitCreateDAO.create(unit);
        if(createdUnit!=null)
        {
            return 1;
        }
        else {
            return -1;
        }
    }
    @Override
    public List<Unit> listUnitService() throws ApplicationErrorException {
        UnitDAO unitListDAO=new UnitDAOImplementation();
        return unitListDAO.list();
    }

    @Override
    public int editUnitService(HashMap<String, String> attributeMap) throws SQLException, ApplicationErrorException, UniqueConstraintException {
        UnitDAO unitEditDAO=new UnitDAOImplementation();
        String unitCodeRegex="^[a-zA-Z]{1,4}$";
        String nameRegex="^[a-zA-Z]{3,30}$";
        int id;
        try {
            id = Integer.parseInt(attributeMap.get("id").trim());
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
        if (attributeMap.get("name") != null && !attributeMap.get("name").matches(nameRegex) || attributeMap.get("unitcode") != null && !attributeMap.get("unitcode").matches(unitCodeRegex)) {
            return 0;
        }
        if(attributeMap.get("name") != null)
        {
            int status=unitEditDAO.edit(id, "name",attributeMap.get("name"));
            if(status==-1)
            {
                return -1;
            }
        }
        if(attributeMap.get("unitcode") != null)
        {
            unitEditDAO.edit(id,"unitcode",attributeMap.get("unitcode"));
        }
        if(attributeMap.get("description") != null)
        {
            unitEditDAO.edit(id,"description",attributeMap.get("description"));
        }
        if(attributeMap.get("isdividable") != null)
        {
            unitEditDAO.edit(id,"isdividable",attributeMap.get("isdividable"));
        }
        return 0;
    }
    @Override
    public int deleteUnitService(String code) throws ApplicationErrorException {
        UnitDAO unitDeleteDAO=new UnitDAOImplementation();
        return unitDeleteDAO.delete(code);
    }
}
