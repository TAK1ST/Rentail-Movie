
package main.models;


import main.base.Model;

public class Actor extends Model {
    private String actorName;

    public Actor(String actorId, String actorName) {
        super(actorId);
        this.actorName = actorName;
    }
    
    public Actor(Actor other) {
        super(other.getId());
        this.actorName = other.actorName;
    }
    
    @Override
    public String toString() {
        return String.format("Actor: %s, %s.", super.getId(), actorName);
    }
    
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        super.getId(),
                        actorName,
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
    
}
