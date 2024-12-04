package main.controllers;

import main.base.ListManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.constants.actor.ActorRank;
import main.constants.IDPrefix;
import main.dao.ActorDAO;
import main.dto.Actor;
import main.utils.IDGenerator;
import main.utils.InfosTable;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.getEnumValue;
import static main.utils.Validator.getName;


public class ActorManager extends ListManager<Actor> {
    
    public ActorManager() {
        super(Actor.className(), Actor.getAttributes());
        list = ActorDAO.getAllActors();
    }
    
    public boolean add(Actor actor) {
        if (checkNull(actor) || checkNull(list)) return false;
        
        list.add(actor);
        return ActorDAO.addActorToDB(list.getLast());
    }

    public boolean update(Actor actor) {
        if (checkNull(actor) || checkNull(list)) return false;

        Actor newActor = getInputs(new boolean[] {true, true, true}, actor);
        if (newActor != null)
            actor = newActor;
        else 
            return false;
        return ActorDAO.updateActorInDB(newActor);
    }

    public boolean delete(Actor actor) { 
        if (checkNull(actor) || checkNull(list)) return false;     

        if (!list.remove(actor)) {
            errorLog("Actor not found");
            return false;
        }
        return ActorDAO.deleteActorFromDB(actor.getId());
    }
    
    @Override
    public Actor getInputs(boolean[] options, Actor oldData) {
        if (options.length < 3) {
            errorLog("Not enough option length");
            return null;
        }
        
        String actorNames = null, description = null;
        ActorRank rank = ActorRank.NONE;
        
        if (oldData != null) {
            actorNames = oldData.getActorName();
            description = oldData.getDescription();
            rank = oldData.getRank();
        }
        
        if (options[0]) {
            actorNames = getName("Enter genre name", actorNames);
            if (actorNames == null) return null;
        }
        if (options[1]) {
            description = getString("Enter description", description);
            if (description == null) return null;
        }
        if (options[2]) {
            rank = (ActorRank)getEnumValue("Choose actor rank", ActorRank.class, rank);
            if (rank == ActorRank.NONE) return null;
        }
        
        String id = (oldData == null) ? IDGenerator.generateID(list.isEmpty() ? null : list.getLast().getId(), IDPrefix.ACTOR_PREFIX)
                :
            oldData.getId();
        
        return new Actor(
                id, 
                actorNames, 
                rank,
                description
        );
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

