package main.models;

import base.Model;
import java.time.LocalDate; 

public class Movie extends Model {
    private String title;
    private String description;
    private String language;
    private LocalDate releaseYear;  
    private double rentalPrice;
    private int availableCopies;

    // Constructor
    public Movie(String id, String title, String description, String language, LocalDate releaseYear, double rentalPrice, int availableCopies) {
        super(id);
        this.title = title;
        this.description = description;
        this.language = language;
        this.releaseYear = releaseYear;
        this.rentalPrice = rentalPrice;
        this.availableCopies = availableCopies;
    }

    // Copy constructor
    public Movie(Movie other) {
        super(other.getId());
        this.title = other.title;
        this.description = other.description;
        this.language = other.language;
        this.releaseYear = other.releaseYear;
        this.rentalPrice = other.rentalPrice;
        this.availableCopies = other.availableCopies;
    }

    // Method to return a string representation of the movie
    @Override
    public String toString() {
        return String.format("Movie: %s, %s, %s, %s, %.2f, %d.", 
                super.getId(), 
                title, 
                description, 
                language, 
                rentalPrice,
                availableCopies
        );
    }

    // Convert Movie object into an array of database values
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
                super.getId(),
                title,
                description,
                language,
                releaseYear,  
                rentalPrice,
                availableCopies
        };
    }

    
    // Static method to get the class name
    public static String className() {
        return "Movie";
    }

    // Getters and setters
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDate getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(LocalDate releaseYear) {
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
}
