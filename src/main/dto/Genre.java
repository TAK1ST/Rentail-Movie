package main.dto;

import main.base.Model;

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
        String[] attr = getAttributes();
        int count = 0;
        return String.format(
                "\n[%s]:\n"
                + "%s: %s,\n"
                + "%s: %s.",
                className(),
                attr[count++], this.getGenreName(),
                attr[count++], description
        );
    }
    
    public static String className() {
        return "Genre";
    }
      
    public static String[] getAttributes() {
        return new String[] {"Name", "Description"};
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
