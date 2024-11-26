package main.models;

import base.Model;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;


public class Movie extends Model {
    private String title;
    private String description;
    private double rating;
    private List<Genre> genres;
    private List<Actor> actors;
    private String language;
    private Date releaseYear;  
    private double rentalPrice;
    private int available_copies;

    public Movie(String id, String title, String description, double rating, List<Genre> genres, List<Actor> actors, String language, Date releaseYear, double rentalPrice, int available_copies ) {
        super(id);
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.genres = genres;
        this.actors = actors;
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
        this.genres = other.genres;
        this.actors = other.actors;
        this.language = other.language;
        this.releaseYear = other.releaseYear;
        this.rentalPrice = other.rentalPrice;
        this.available_copies = other.available_copies;

    }

    //Methods
    @Override
    public String toString() {
        String genreNames = genres != null ? genres.stream().map(Genre::getGenreName).collect(Collectors.joining(", ")) : "No genres";
        String actorNames = actors != null ? actors.stream().map(Actor::getActorName).collect(Collectors.joining(", ")) : "No actors";
        return String.format("Movie: %s, %s, %s, %.5f, %s, %s, %s, %s, %.5f, %d.",
                super.getId(),
                title,
                description,
                rating,
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
            rating,
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
    
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Date releaseYear) {
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
