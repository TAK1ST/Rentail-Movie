package main.dto;

import main.base.Model;

public class Genre extends Model {
    private String genreName;
    private String description;
    public Genre(String genreId, String genreName, String description) {
        super(genreId);
        this.genreName = genreName;
        this.description = description;
    }
    
    public Genre(Genre other) {
        super(other.getId());
        this.genreName = other.genreName;
        this.description = description;
    }
    
    @Override
    public String toString() {
        return String.format("Genre: %s, %s, %s.", super.getId(), genreName,description);
    }
    
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        super.getId(),
                        genreName,
                        description
                };
    }
    
    public static String className() {
        return "Genre";
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
