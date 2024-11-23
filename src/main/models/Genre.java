package main.models;

import base.Model;

public class Genre extends Model {
    private String genreName;

    public Genre(int genreId, String genreName) {
        super(genreId);
        this.genreName = genreName;
    }

    public int getGenreId() {
        return getId();
    }

    public void setGenreId(int genreId) {
        this.setId(genreId);
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        getGenreId(),
                        getGenreName()
                };
    }
}
