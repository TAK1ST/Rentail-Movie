
package main.controllers;


import main.base.ListManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.constants.ActorRank;
import main.constants.IDPrefix;
import main.dao.ActorDAO;
import main.dto.Actor;
import main.utils.IDGenerator;
import main.utils.InfosTable;
import static main.utils.Input.getString;
import static main.utils.Utility.getEnumValue;
import static main.utils.Validator.getName;


public class ActorManager extends ListManager<Actor> {
    
    public ActorManager() {
        super(Actor.className(), Actor.getAttributes());
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
        if (checkNull(list)) return false;

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
        if (checkNull(list)) return false;       

        Actor foundActor = (Actor)getById("Enter actor's id");
        if (checkNull(foundActor)) return false;

        list.remove(foundActor);
        return ActorDAO.deleteActorFromDB(foundActor.getId());
    }
    
    @Override
    public List<Actor> sortList(List<Actor> tempList, String property) {
        if (checkNull(list)) {
            return null;
        }
        String[] options = Actor.getAttributes();
        List<Actor> result = new ArrayList<>(tempList);

        if (property.equals(options[0])) {
            result.sort(Comparator.comparing(Actor::getActorName));
        } else if (property.equals(options[1])) {
            result.sort(Comparator.comparing(Actor::getRank));
        } else if (property.equals(options[2])) {
            result.sort(Comparator.comparing(Actor::getDescription));
        } else {
            result.sort(Comparator.comparing(Actor::getId));
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
    public void show(List<Actor> tempList) {
        if (checkNull(tempList)) { 
            return;
        } 
        
        InfosTable.getTitle(Actor.getAttributes());
        tempList.forEach(item -> 
                InfosTable.calcLayout(
                        item.getId(), 
                        item.getActorName(), 
                        item.getRank(), 
                        item.getDescription()
                )
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
                InfosTable.displayByLine(
                        item.getId(), 
                        item.getActorName(), 
                        item.getRank(), 
                        item.getDescription()
                )
        );
        InfosTable.showFooter();
    }
   
}

