package Service;

import Entity.Unit;

import java.util.HashMap;

public interface UnitService {
    int createUnitService(Unit unit);
    void listUnitService();
    int editUnitService(HashMap<String,String> attributeMap);
    int deleteUnitService(String code);
}
