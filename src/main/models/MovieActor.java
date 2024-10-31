// MovieActor.java
package main.models;

public class MovieActor {
    private Movie movieId;
    private Actor actorId;
    private String role;

    public MovieActor(Movie movieId, Actor actorId, String role) {
        this.movieId = movieId;
        this.actorId = actorId;
        this.role = role;
    }

    public Movie getMovieId() {
        return movieId;
    }

    public void setMovieId(Movie movieId) {
        this.movieId = movieId;
    }

    public Actor getActorId() {
        return actorId;
    }

    public void setActorId(Actor actorId) {
        this.actorId = actorId;
    }

    private String getRoleMovieActor() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

//    public Object[] getDatabaseValues() {
//        return new Object[]
//                {
//                        getActorId(),
//                        getMovieId(),
//                        getRoleMovieActor()
//                };
//    }
}
