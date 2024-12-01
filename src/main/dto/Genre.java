package main.dto;

import main.exceptions.MethodNotFound;
import main.base.Model;
import static main.utils.LogMessage.errorLog;

public class Genre extends Model {
    
    private String description;
    
    public Genre() {
    }
    
    public Genre(String genreName, String description) {
        super(genreName);
        this.description = description;
    }
    
    public Genre(Genre other) {
        super(other.getGenreName());
        this.description = other.description;
    }
    
    @Override
    public String toString() {
        return String.format("Genre: %s, %s.", this.getGenreName(), description);
    }
    
    public static String className() {
        return "Genre";
    }
    
    @Override    
    public String[] getSearchOptions() {
        return new String[] {"genre_name", "description"};
    }
    
    @Override
    public String getId() {
        try {
            throw new MethodNotFound("Genre only has NAME instead of id");
        } catch (MethodNotFound e) {
            errorLog("Exception caught: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public void setId(String id) {
        try {
            throw new MethodNotFound("Genre only has NAME instead of id");
        } catch (MethodNotFound e) {
            errorLog("Exception caught: " + e.getMessage());
        }
    }

    public String getGenreName() {
        return super.getId();
    }

    public void setGenreName(String genreName) {
        super.setId(genreName);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
