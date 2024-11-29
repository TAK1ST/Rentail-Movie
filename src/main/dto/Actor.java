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

<<<<<<< HEAD
    //Methods
    @Override
    public String toString() {
        return String.format("Actor: %s, %s, %c, %s.", super.getId(), actorName, rank, description);
=======
    @Override
    public String toString() {
        return String.format("Actor: %s, %s, %s, %s.", super.getId(), actorName, rank, description);
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            super.getId(),
            actorName,
<<<<<<< HEAD
            rank,
            description
        };
=======
            description,
            rank};
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
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
