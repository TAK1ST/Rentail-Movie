package main.dto;

import main.exceptions.MethodNotFound;
import main.base.Model;
import static main.utils.LogMessage.errorLog;

public class Language extends Model {
    
    private String name;  

    public Language(String code, String name) {
        super(code);
        this.name = name;
    }

    public Language(Language other) {
        super(other.getCode());
        this.name = other.name;
    }

    @Override
    public String toString() {
        return String.format("Language: %s, %s.", this.getCode(), name);
    }

    public static String className() {
        return "Language";
    }
    
    @Override
    public String getId() {
        try {
            throw new MethodNotFound("Genre only has CODE instead of id");
        } catch (MethodNotFound e) {
            errorLog("Exception caught: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public void setId(String id) {
        try {
            throw new MethodNotFound("Language only has CODE instead of id");
        } catch (MethodNotFound e) {
            errorLog("Exception caught: " + e.getMessage());
        }
    }

    public String getCode() {
        return super.getId();
    }

    public void setCode(String code) {
        super.setId(code);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
