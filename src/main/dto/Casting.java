/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.dto;

import exceptions.MethodNotFound;
import main.base.Model;
import main.constants.ActorRole;
import static main.utils.LogMessage.errorLog;

/**
 *
 * @author trann
 */
public class Casting extends Model {
    
    private String actorID;
    private ActorRole role;

    public Casting(String movieID, String actorID, ActorRole role) {
        super(movieID);
        this.actorID = actorID;
        this.role = role;
    }
    
    public Casting(Casting other) {
        super(other.getMovieID());
        this.actorID = other.actorID;
        this.role = other.role;
    }
    
    @Override
    public String toString() {
        return String.format("Casting: %s, %s, %s.", this.getMovieID(), actorID, role);
    }

    public static String className() {
        return "Casting";
    }
    
    @Override
    public String getId() {
        try {
            throw new MethodNotFound("Discount only has CODE instead of id");
        } catch (MethodNotFound e) {
            errorLog("Exception caught: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public void setId(String id) {
        try {
            throw new MethodNotFound("Discount only has CODE instead of id");
        } catch (MethodNotFound e) {
            errorLog("Exception caught: " + e.getMessage());
        }
    }

    public String getMovieID() {
        return super.getId();
    }

    public void setMovieID(String movieID) {
        super.setId(movieID);
    }

    public String getActorID() {
        return actorID;
    }

    public void setActorID(String actorID) {
        this.actorID = actorID;
    }

    public ActorRole getRole() {
        return role;
    }

    public void setRole(ActorRole role) {
        this.role = role;
    }
}
