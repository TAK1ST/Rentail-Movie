package main.dto;

import main.base.Model;
import main.constants.ActorRank;

public class Actor extends Model {

    private String actorName;
    private String description;
    private ActorRank rank;

    public Actor(String id, String actorName, ActorRank rank, String description) {
        super(id);
        this.actorName = actorName;
        this.rank = rank;
        this.description = description;
    }

    public Actor(Actor other) {
        super(other.getId());
        this.actorName = other.actorName;
        this.rank = rank;
        this.description = description;
    }

    //Methods
    @Override
    public String toString() {
        return String.format("Actor: %s, %s, %c, %s.", super.getId(), actorName, rank, description);
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            super.getId(),
            actorName,
            rank,
            description
        };
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

    public ActorRank getRank() {
        return rank;
    }

    public void setRank(ActorRank rank) {
        this.rank = rank;
    }
}
