
package main.models;


import base.Model;

public class Movie extends Model {
    private String title;
    private String description;
    private String rating;
    private Genre genre;
    private String language;
    private String releaseYear;
    private double rentalPrice;
    private int availableCopies;

    public Movie(int movieId, String title, String description, String rating, Genre genre, String language, String releaseYear, double rentalPrice, int availableCopies) {
        super(movieId);
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.genre = genre;
        this.language = language;
        this.releaseYear = releaseYear;
        this.rentalPrice = rentalPrice;
        this.availableCopies = availableCopies;
    }

    public int getMovieId() {
        return getId();
    }

    public void setMovieId(int movieId) {
        this.setId(movieId);
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

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
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

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        getId(),
                        title,
                        description,
                        rating,
                        genre,
                        language,
                        releaseYear,
                        rentalPrice,
                        availableCopies
                };
    }
}