package main.dto;

import main.base.Model;

public class Genre extends Model {
    
    private String description;
    
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
    
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]
                {
                    this.getGenreName(),
                    description
                };
    }
    
    public static String className() {
        return "Genre";
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
