package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.constants.ActorRank;
import static main.constants.Constants.ACTOR_PREFIX;
import main.dao.ActorDAO;
import main.dto.Actor;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import static main.utils.Utility.getEnumValue;
import static main.utils.Validator.getName;

/**
 *
 * @author trann
 */
public class ActorManager extends ListManager<Actor> {
    
    public ActorManager() throws IOException {
        super(Actor.className());
        list = ActorDAO.getAllActors();
    }
    
    public boolean addActor() {
        list.add(new Actor(
                IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), ACTOR_PREFIX), 
                getName("Enter actor's name", false), 
                (ActorRank) getEnumValue("Enter actor's status", ActorRank.class, false),
                getString("Enter actor's description", false)
        ));
        return ActorDAO.addActorToDB(list.getLast());
    }

    public boolean updateActor() {
        if (checkEmpty(list)) return false;

        Actor foundActor = (Actor)getById("Enter user's id");
        if (checkNull(foundActor)) return false;
        
        String name = getName("Enter actor's name", true);     
        ActorRank rank = (ActorRank) getEnumValue("Enter actor's status", ActorRank.class, true);
        String description = getString("Enter actor's description", false);
        
        if (!name.isEmpty()) 
            foundActor.setActorName(name);
        if (rank != ActorRank.NONE)
            foundActor.setRank(rank);
        if (description.isEmpty()) 
            foundActor.setDescription(description);
        
        return ActorDAO.updateActorInDB(foundActor);
    }

    public boolean deleteActor() { 
        if (checkEmpty(list)) return false;       

        Actor foundActor = (Actor)getById("Enter user's id");
        if (checkNull(foundActor)) return false;

        list.remove(foundActor);
        return ActorDAO.deleteActorFromDB(foundActor.getId());
    }

    public void searchActor() {
        display(getActorBy("Enter actor's propety"), "List of Actor");
    }

    public List<Actor> getActorBy(String message) {
        return searchBy(getString(message, false));
    }
    
    @Override
    public List<Actor> searchBy(String propety) {
        List<Actor> result = new ArrayList<>();
        for (Actor item : list) 
            if (item.getId().equals(propety)
                || item.getActorName().contains(propety.trim().toLowerCase())
            ) 
            result.add(item);
        return result;
    }
    
    @Override
    public void display(List<Actor> actors, String title) {
        if (checkEmpty(list)) return; 
        
        System.out.println(title);
        System.out.println("|------------------------------------------------------------------------");
        System.out.printf("|%-15s | %-30s | %-4s | %-50s\n |", "Actor ID", "Actor Name", "Rank", "Description");
        System.out.println("|------------------------------------------------------------------------");
        for (Actor item : actors) {
            System.out.printf("|%-15s | %-30s | %-4s | %-50s\n |",
                    item.getId(),
                    item.getActorName(),
                    item.getRank(),
                    item.getDescription());
        }
        System.out.println("|------------------------------------------------------------------------");
    }

}
