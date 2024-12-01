package main.dto;

import main.base.Model;
import main.constants.ActorRank;

public class Actor extends Model {

    private String actorName;
    private ActorRank rank;
    private String description;
    
    public Actor(String id, String actorName, ActorRank rank, String description) {
        super(id);
        this.actorName = actorName;
        this.rank = rank;
        this.description = description;
    }

    public Actor(Actor other) {
        super(other.getId());
        this.actorName = other.actorName;
        this.rank = other.rank;
        this.description = other.description;
    }

    @Override
    public String toString() {
        return String.format("Actor: %s, %s, %s, %s.", super.getId(), actorName, rank, description);
    }

    public static String className() {
        return "Actor";
    }
    
    @Override
    public String[] getSearchOptions() {
        return new String[] {"actor_id", "actor_name", "actor_rank", "actor_description"};
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
