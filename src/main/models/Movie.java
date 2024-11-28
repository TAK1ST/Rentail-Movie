package main.models;

import main.base.Model;
import java.time.LocalDate;
import java.util.List;


public class Movie extends Model {
    private String title;
    private String description;
    private double avgRating;
    private List<String> genreIds;
    private List<String> actorIds;
    private String language;
    private LocalDate releaseYear;  
    private double rentalPrice;
    private int available_copies;

    public Movie(String id, String title, String description, double avgRating, List<String> genreIds, List<String> actorIds, String language, LocalDate releaseYear, double rentalPrice, int available_copies ) {
        super(id);
        this.title = title;
        this.description = description;
        this.avgRating = avgRating;
        this.genreIds = genreIds;
        this.actorIds = actorIds;
        this.language = language;
        this.releaseYear = releaseYear;
        this.rentalPrice = rentalPrice;
        this.available_copies = available_copies;
    }

   
    public Movie(Movie other) {
        super(other.getId());
        this.title = other.title;
        this.description = other.description;
        this.avgRating = other.avgRating;
        this.genreIds = other.genreIds;
        this.actorIds = other.actorIds;
        this.language = other.language;
        this.releaseYear = other.releaseYear;
        this.rentalPrice = other.rentalPrice;
        this.available_copies = other.available_copies;

    }

    //Methods
    @Override
    public String toString() {
         String genreNames = genreIds != null ? String.join(", ", genreIds) : "No genres";
         String actorNames = actorIds != null ? String.join(", ", actorIds) : "No actors";
        return String.format("Movie: %s, %s, %s, %.5f, %s, %s, %s, %s, %.5f, %d.",
                super.getId(),
                title,
                description,
                avgRating,
                genreNames,
                actorNames,
                language,
                releaseYear,
                rentalPrice,
                available_copies

        );
    }


    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            super.getId(),
            title,
            description,
            avgRating,
            language,
            releaseYear,
            rentalPrice,
            available_copies
        };
    }

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

    public double getAVGRating() {
        return avgRating;
    }

    public void setAVGRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public List<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<String> genreIds) {
        this.genreIds = genreIds;
    }

    public List<String> getActorIds() {
        return actorIds;
    }

    public void setActorIds(List<String> actorIds) {
        this.actorIds = actorIds;
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

      public int getAvailable_copies() {
        return available_copies;
    }

    public void setAvailable_copies(int available_copies) {
        this.available_copies = available_copies;
    }
}
