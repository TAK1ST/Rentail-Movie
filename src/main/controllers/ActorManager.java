package main.controllers;

import main.base.ListManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import main.constants.actor.ActorRank;
import main.constants.IDPrefix;
import main.dao.ActorDAO;
import main.dto.Actor;
import main.utils.InfosTable;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.getEnumValue;
import static main.utils.Validator.getName;


public class ActorManager extends ListManager<Actor> {
    
    public ActorManager() {
        super(Actor.className(), Actor.getAttributes());
        copy(ActorDAO.getAllActors()); 
    }
    
    public boolean addActor() {
        String name = getName("Enter actor's name");
        if (name == null) return false;
        
        for (Actor item : list) 
            if (item.getActorName().equals(name)) {
                errorLog("This name already exist");
                return false;
            }
        
        ActorRank rank = (ActorRank) getEnumValue("Enter actor's rank", ActorRank.class);
        if (rank == null) return false;

        String description = getString("Enter actor's description");
        if (description == null) return false;
        
        Actor actor = new Actor(
                createID(IDPrefix.ACTOR_PREFIX), 
                name, 
                rank,
                description
        );
        return add(actor);
    }
    
    public boolean updateActor(Actor actor) {
        if (checkNull(list)) return false;
        
        if (actor == null)
            actor = (Actor) getById("Enter actor's");
        if (checkNull(actor)) return false;
        
        Actor temp = new Actor(actor);
        temp.setActorName(getName("Enter actor's name", temp.getActorName()));
        temp.setRank((ActorRank) getEnumValue("Enter actor's rank", ActorRank.class, temp.getRank()));
        temp.setDescription(getString("Enter actor's description", temp.getDescription()));
        
        return update(actor, temp);
    }
    
    public boolean deleteActor(Actor actor) {
        if (checkNull(list)) return false;
        if (actor == null) 
            actor = (Actor) getById("Enter actor's id");
        if (checkNull(actor)) return false;
        return delete(actor);
    }
    
    public boolean add(Actor actor) {
        if (actor == null) return false;
        return ActorDAO.addActorToDB(actor) && list.add(actor);
    }

    public boolean update(Actor oldActor, Actor newActor) {
        if (newActor == null || checkNull(list)) return false;
        if (!ActorDAO.updateActorInDB(newActor)) return false;
        
        oldActor.setActorName(newActor.getActorName());
        oldActor.setRank(newActor.getRank());
        oldActor.setDescription(newActor.getDescription());
        
        return true;
    }
    
    public boolean delete(Actor actor) {
        if (actor == null) return false;     
        return ActorDAO.deleteActorFromDB(actor.getId()) && list.remove(actor);
    }
    
    @Override
    public List<Actor> sortList(List<Actor> tempList, String propety, boolean descending) {
        if (tempList == null) return null;
        
        if (propety == null) return tempList;
        
        String[] options = Actor.getAttributes();
        List<Actor> result = new ArrayList<>(tempList);

        if (propety.equalsIgnoreCase(options[0])) {
            result.sort(Comparator.comparing(Actor::getActorName));
        } else if (propety.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Actor::getRank));
        } else if (propety.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Actor::getDescription));
        } else {
            result.sort(Comparator.comparing(Actor::getId));
        }
        
        if (descending) Collections.sort(tempList, Collections.reverseOrder());
        
        return result;
    }
    
    @Override
    public List<Actor> searchBy(List<Actor> tempList, String propety) {
        if (tempList == null) return null;
        
        List<Actor> result = new ArrayList<>();
        for (Actor item : tempList) {
            if (item == null) 
                continue;
            if ((item.getId() != null && item.getId().equals(propety))
                || (item.getActorName() != null && item.getActorName().contains(propety.trim().toLowerCase()))
            ) 
            result.add(item);
        }
        return result;
    }
    
    @Override
    public void show(List<Actor> tempList) {
        if (checkNull(tempList)) return;
        
        InfosTable.getTitle(new String [] {"Actor Name", "Rank", "Descrription"});
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.calcLayout(
                        item.getActorName(), 
                        item.getRank(), 
                        item.getDescription());
            }
        );

        InfosTable.setShowNumber();
        InfosTable.showTitle();
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.displayByLine(
                        item.getActorName(), 
                        item.getRank(), 
                        item.getDescription());
            }
        );
        InfosTable.showFooter();
    }
   
}

