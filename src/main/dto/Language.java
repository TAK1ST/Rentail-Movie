package main.dto;

import main.base.Model;

public class Language extends Model {
    private String code;  
    private String name;  

    // Constructor
    public Language(String languageId, String code, String name) {
        super(languageId);
        this.code = code;
        this.name = name;
    }

    public Language(Language other) {
        super(other.getId());
        this.code = other.code;
        this.name = other.name;
    }

    @Override
    public String toString() {
        return String.format("Language: %s, %s, %s.", super.getId(), code, name);
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
                super.getId(),
                code,
                name
        };
    }

    public static String className() {
        return "Language";
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
}
