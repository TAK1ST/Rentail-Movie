package main.models;

import base.Model;

public class Genre extends Model {
    private String genreName;

    public Genre(String id, String genreName) {
        super(id);
        this.genreName = genreName;
    }
    
    public Genre(Genre other) {
        super(other.getId());
        this.genreName = other.genreName;
    }
    
    @Override
    public String toString() {
        return String.format("Genre: %s, %s.", super.getId(), genreName);
    }
    
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        super.getId(),
                        genreName,
                };

    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

}
