
package main.models;


public class Actor {
    private int actorId;
    private String actorName;

    public Actor(int actorId, String actorName) {
        this.actorId = actorId;
        this.actorName = actorName;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }
    
}
