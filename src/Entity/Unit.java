package Entity;

public class Unit {
    String name;
    String code;
    String description;
    String isDividable;


    public Unit(String name, String code, String description, String isDividable) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.isDividable = isDividable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsDividable() {
        return isDividable;
    }

    public void setIsDividable(String isDividable) {
        this.isDividable = isDividable;
    }
}
