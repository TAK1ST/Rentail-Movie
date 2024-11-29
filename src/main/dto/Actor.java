package main.dto;

import main.base.Model;

public class Actor extends Model {

    private String actorName;
    private String description;
    private char rank;
//Constructor
    public Actor(String actorId, String actorName, char rank, String description) {
        super(actorId);
        this.actorName = actorName;
        this.rank = rank;
        this.description = description;
    }

    public Actor(Actor other) {
        super(other.getId());
        this.actorName = other.actorName;
    }

    @Override
    public String toString() {
        return String.format("Actor: %s, %s, %s, %s.", super.getId(), actorName, rank, description);
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            super.getId(),
            actorName,
            description,
            rank};
    }

    public static String className() {
        return "Actor";
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public char getRank() {
        return rank;
    }

    public void setRank(char rank) {
        this.rank = rank;
    }
}
