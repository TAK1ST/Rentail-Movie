package main.dto;

import main.base.Model;

public class Language extends Model {
    
    private String name;  

    public Language() {
    }
    
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

    public static String[] getAttributes() {
        return new String[] {"Language", "Code", "Language name"};
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
