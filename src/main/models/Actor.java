
package main.models;


import base.Model;

public class Actor extends Model {
    private String actorName;

    public Actor(int actorId, String actorName) {
        super(actorId);
        this.actorName = actorName;
    }

    public int getActorId() {
        return super.getId();
    }

    public void setActorId(int actorId) {
        setId(actorId);
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        getActorId(),
                        getActorName()
                };
    }
}
