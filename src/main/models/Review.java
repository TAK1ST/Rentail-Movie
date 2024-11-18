package main.models;

import base.Model;

public class Review extends Model {
    private Movie movie_id;
    private Users users_id;

    private int rating;
    private String review_text;
    private String review_date;


    public Review(int id, Movie movie_id, Users users_id, int rating, String review_text, String review_date) {
        super(id);
        this.movie_id = movie_id;
        this.users_id = users_id;
        this.rating = rating;
        this.review_date = review_date;
        this.review_text = review_text;
    }

    public Movie getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Movie movie_id) {
        this.movie_id = movie_id;
    }

    public Users getUsers_id() {
        return users_id;
    }

    public void setUsers_id(Users users_id) {
        this.users_id = users_id;
    }

    public int getRating()  {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview_date() {
        return review_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        getId(),
                        getMovie_id(),
                        getReview_date(),
                        getReview_text(),
                        getUsers_id()
                };
    }
}
