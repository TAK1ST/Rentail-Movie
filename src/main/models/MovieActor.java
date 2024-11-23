// MovieActor.java
package main.models;

public class MovieActor {
    private String movieId;
    private String actorId;


    public MovieActor(String movieId,String actorId) {
        this.movieId = movieId;
        this.actorId = actorId;

    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
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
