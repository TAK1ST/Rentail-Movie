
package main.models;


import base.Model;

public class Movie extends Model {
    private String title;
    private String description;
    private String rating;
    private String genreId;
    private String language;
    private String releaseYear;
    private double rentalPrice;
     private int availableCopies; 

    public Movie(String id, String title, String description, String rating, String genreId, String language, String releaseYear, double rentalPrice, int availableCopies) {
        super(id);
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.genreId = genreId;
        this.language = language;
        this.releaseYear = releaseYear;
        this.rentalPrice = rentalPrice;
        this.availableCopies = availableCopies;
    }
    
    public Movie(Movie other) {
        super(other.getId());
        this.title = other.title;
        this.description = other.description;
        this.rating = other.rating;
        this.genreId = other.genreId;
        this.language = other.language;
        this.releaseYear = other.releaseYear;
        this.rentalPrice = other.rentalPrice;
        this.availableCopies = other.availableCopies;
    }
    
    @Override
    public String toString() {
        return String.format("Movie: %s, %s, %s, %s, %s, %s, %s, %.5f., %d", 
                super.getId(), 
                title, 
                description, 
                rating, 
                genreId, 
                language, 
                releaseYear, 
                rentalPrice,
                availableCopies);
    }
    
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        super.getId(),
                        title,
                        description,
                        rating,
                        genreId,
                        language,
                        releaseYear,
                        rentalPrice,
                        availableCopies,
                };

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
    
}