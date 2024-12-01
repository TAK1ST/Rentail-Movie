package main.dto;

import main.base.Model;
import java.time.LocalDate;
import main.utils.Validator;


public class Movie extends Model {
    
    private String title;
    private String description;
    private double avgRating;
    private String genreNames;
    private String actorIDs;
    private String languageCodes;
    private LocalDate releaseYear;  
    private double rentalPrice;
    private int availableCopies;
    private LocalDate createDate;
    private LocalDate updateDate;

    public Movie(
            String id, 
            String title, 
            String description, 
            double avgRating, 
            String genreNames, 
            String actorIDs, 
            String languageCodes, 
            LocalDate releaseYear, 
            double rentalPrice, 
            int availableCopies,
            LocalDate createDate, 
            LocalDate updateDate) 
    {
        super(id);
        this.title = title;
        this.description = description;
        this.avgRating = avgRating;
        this.genreNames = genreNames;
        this.actorIDs = actorIDs;
        this.languageCodes = languageCodes;
        this.releaseYear = releaseYear;
        this.rentalPrice = rentalPrice;
        this.availableCopies = availableCopies;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
   
    public Movie(Movie other) {
        super(other.getId());
        this.title = other.title;
        this.description = other.description;
        this.avgRating = other.avgRating;
        this.genreNames = other.genreNames;
        this.actorIDs = other.actorIDs;
        this.languageCodes = other.languageCodes;
        this.releaseYear = other.releaseYear;
        this.rentalPrice = other.rentalPrice;
        this.availableCopies = other.availableCopies;
        this.createDate = other.createDate;
        this.updateDate = other.updateDate;
    }

    @Override
    public String toString() {
        return String.format("Movie: %s, %s, %s, %.5f, %s, %s, %s, %s, %.5f, %d, %s, %s.",
                super.getId(),
                title,
                description,
                avgRating,
                genreNames != null ? genreNames : "No genres",
                actorIDs != null ? actorIDs : "No actors",
                languageCodes != null ? languageCodes : "No languages",
                releaseYear.format(Validator.DATE),
                rentalPrice,
                availableCopies,
                createDate.format(Validator.DATE),
                updateDate.format(Validator.DATE)
        );
    }

    public static String className() {
        return "Movie";
    }
    
    @Override    
    public String[] getSearchOptions() {
        return new String[] {"movie_id", "title", "description", "avg_rating", "release_year", "rental_price", "available_copies", "created_at", "updated_at"};
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

    public String getGenreNames() {
        return genreNames;
    }

    public void setGenreNames(String genreNames) {
        this.genreNames = genreNames;
    }

    public String getActorIDs() {
        return actorIDs;
    }

    public void setActorIDs(String actorIDs) {
        this.actorIDs = actorIDs;
    }

    public String getLanguageCodes() {
        return languageCodes;
    }

    public void setLanguageCodes(String languageCodes) {
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

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

}
