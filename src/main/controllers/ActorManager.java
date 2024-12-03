package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.constants.ActorRank;
import main.constants.IDPrefix;
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
    
    private static final String[] searchOptions = {"actor_id", "actor_name", "actor_rank", "actor_description"};
    
    public ActorManager() throws IOException {
        super(Actor.className());
        list = ActorDAO.getAllActors();
    }
    
    public boolean addActor() {
        
        String name = getName("Enter actor's name", false);
        if (name.isEmpty()) return false;
        
        ActorRank rank = (ActorRank) getEnumValue("Enter actor's rank", ActorRank.class, false);
        if (rank == ActorRank.NONE) return false;
        
        String description = getString("Enter actor's description", false);
        if (description.isEmpty()) return false;
        
        list.add(new Actor(
                IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), IDPrefix.ACTOR_PREFIX), 
                name, 
                rank,
                description
        ));
        return ActorDAO.addActorToDB(list.getLast());
    }

    public boolean updateActor() {
        if (checkEmpty(list)) return false;

        Actor foundActor = (Actor)getById("Enter actor's id");
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

        Actor foundActor = (Actor)getById("Enter actor's id");
        if (checkNull(foundActor)) return false;

        list.remove(foundActor);
        return ActorDAO.deleteActorFromDB(foundActor.getId());
    }
    
    @Override
    public List<Actor> sortList(List<Actor> tempList, String property) {
        if (checkEmpty(list)) {
            return null;
        }

        List<Actor> result = new ArrayList<>(list);
            switch (property) {
                case "actorName":
                    result.sort(Comparator.comparing(Actor::getActorName));
                    break;
                case "rank":
                    result.sort(Comparator.comparing(Actor::getRank));
                    break;
                case "description":
                    result.sort(Comparator.comparing(Actor::getDescription));
                    break;
                default:
                    result.sort(Comparator.comparing(Actor::getId));
                    break;
            }
        return result;
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
    public void display(List<Actor> tempList) {
        if (checkEmpty(tempList)) return; 
        int actorNameLength = 0;
        int descriptionLength = 0;
        for (Actor item : list) {
            actorNameLength = Math.max(actorNameLength, item.getActorName().length());
            descriptionLength = Math.max(descriptionLength, item.getDescription().length());
        }
        
        int widthLength = 8 + actorNameLength + 5 + descriptionLength + 13;
         for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-8s | %-" + actorNameLength + "s | %-5s | %-" + descriptionLength + "s | \n",
                "ID", "Name", "Rank" , "Description");
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        for (Actor item : tempList) {
            System.out.printf("\n| %-8s | %-" + actorNameLength + "s | %-5s | %-" + descriptionLength + "s |",
                    item.getId(),
                    item.getActorName(),
                    item.getRank(),
                    item.getDescription());
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }
   
}

