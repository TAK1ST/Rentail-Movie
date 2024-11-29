package main.dto;

import main.base.Model;
import java.time.LocalDate;
import java.util.List;


public class Movie extends Model {
    private String title;
    private String description;
    private double avgRating;
    private List<String> genreIDs;
    private List<String> actorIDs;
    private List<String> languageCodes;
    private LocalDate releaseYear;  
    private double rentalPrice;
    private int availableCopies;

    public Movie(String id, String title, String description, double avgRating, List<String> genreIDs, List<String> actorIDs, List<String> languageCodes, LocalDate releaseYear, double rentalPrice, int availableCopies ) {
        super(id);
        this.title = title;
        this.description = description;
        this.avgRating = avgRating;
        this.genreIDs = genreIDs;
        this.actorIDs = actorIDs;
        this.languageCodes = languageCodes;
        this.releaseYear = releaseYear;
        this.rentalPrice = rentalPrice;
        this.availableCopies = availableCopies;
    }

   
    public Movie(Movie other) {
        super(other.getId());
        this.title = other.title;
        this.description = other.description;
        this.avgRating = other.avgRating;
        this.genreIDs = other.genreIDs;
        this.actorIDs = other.actorIDs;
        this.languageCodes = other.languageCodes;
        this.releaseYear = other.releaseYear;
        this.rentalPrice = other.rentalPrice;
        this.availableCopies = other.availableCopies;

    }

    @Override
    public String toString() {
         String genreNames = genreIDs != null ? String.join(", ", genreIDs) : "No genres";
         String actorNames = actorIDs != null ? String.join(", ", actorIDs) : "No actors";
         String languages = languageCodes != null ? String.join(", ", languageCodes) : "No languages";
        return String.format("Movie: %s, %s, %s, %.5f, %s, %s, %s, %s, %.5f, %d.",
                super.getId(),
                title,
                description,
                avgRating,
                genreNames,
                actorNames,
                languages,
                releaseYear,
                rentalPrice,
                availableCopies

        );
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            super.getId(),
            title,
            description,
            avgRating,
            releaseYear,
            rentalPrice,
            availableCopies
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

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public List<String> getGenreIDs() {
        return genreIDs;
    }

    public void setGenreIDs(List<String> genreIDs) {
        this.genreIDs = genreIDs;
    }

    public List<String> getActorIDs() {
        return actorIDs;
    }

    public void setActorIDs(List<String> actorIDs) {
        this.actorIDs = actorIDs;
    }

    public List<String> getLanguageCodes() {
        return languageCodes;
    }

    public void setLanguageCodes(List<String> languageCodes) {
        this.languageCodes = languageCodes;
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

    public int getAvailable_copies() {
        return availableCopies;
    }

    public void setAvailable_copies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

}
