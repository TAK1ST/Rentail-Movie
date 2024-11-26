
package main.models;


import base.Model;

public class Movie extends Model {
    private String title;
    private String description;
    private double rating;
    private String genreID;
    private String language;
    private String releaseYear;
    private double rentalPrice;
    private int available_copies;
    
    //Constructor
    
    public Movie(String id, String title, String description, double rating, String genreID, String language, String releaseYear, double rentalPrice,int available_copies) {
        super(id);
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.genreID = genreID;
        this.language = language;
        this.releaseYear = releaseYear;
        this.rentalPrice = rentalPrice;
        this.available_copies = available_copies;
    }
    
    public Movie(Movie other) {
        super(other.getId());
        this.title = other.title;
        this.description = other.description;
        this.rating = other.rating;
        this.genreID = other.genreID;
        this.language = other.language;
        this.releaseYear = other.releaseYear;
        this.rentalPrice = other.rentalPrice;
        this.available_copies = available_copies;
    }
    //Methods
    @Override
    public String toString() {
        return String.format("Movie: %s, %s, %s, %.5f, %s, %s, %s, %.5f, %d.", 
                super.getId(), 
                title, 
                description, 
                rating, 
                genreID, 
                language, 
                releaseYear, 
                rentalPrice,
                available_copies
        );
    }
    
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        super.getId(),
                        title,
                        description,
                        rating,
                        genreID,
                        language,
                        releaseYear,
                        rentalPrice,
                        available_copies
                };
    }
    
    public static String className() {
        return "Movie";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genreID;
    }

    public void setGenre(String genreID) {
        this.genreID = genreID;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public int getAvailable_copies() {
        return available_copies;
    }

    public void setAvailable_copies(int available_copies) {
        this.available_copies = available_copies;
    }
}