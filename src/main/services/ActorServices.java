package main.services;

import base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.CRUD.ActorCRUD;
import main.models.Actor;
import main.utils.IDGenerator;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import static main.utils.Utility.Console.getString;

/**
 *
 * @author trann
 */
public class ActorServices extends ListManager<Actor> {
    private static final String DISPLAY_TITLE = "List of Actor:";
    
    public ActorServices() throws IOException {
        super(Actor.className());
        ActorCRUD.getAllActor();
    }
    
    public void managerMenu() throws IOException {  
        Menu.showManagerMenu(
            "Actor Management",
            null,
            new Menu.MenuOption[]{
                new Menu.MenuOption("Add actor", () -> showSuccess(addActor())),
                new Menu.MenuOption("Delete actor", () -> showSuccess(deleteActor())),
                new Menu.MenuOption("Update actor", () -> showSuccess(updateActor())),
                new Menu.MenuOption("Search actor", () -> searchActor()),
                new Menu.MenuOption("Show all actor", () -> display(list, DISPLAY_TITLE)),
                new Menu.MenuOption("Back", () -> { /* Exit action */ })
            },
            new Menu.MenuAction[] { () -> Menu.getSaveMessage(isNotSaved) },
            true
        );
    }
    
    public boolean addActor() {
        String id = list.isEmpty() ? "A00001" : IDGenerator.generateID(list.getLast().getId(), "A");
        String name = getString("Enter actor's name: ", false);
    
        list.add(new Actor(id, name));
        ActorCRUD.addActorToDB(list.getLast());
        return true;
    }

    public boolean updateActor() {
        if (checkEmpty(list)) return false;

        Actor foundActor = (Actor)getById("Enter user's id to update: ");
        if (checkNull(foundActor)) return false;
        
        String name = getString("Enter actor's name: ", true);     
        if (!name.isEmpty()) foundActor.setActorName(name);
        
        ActorCRUD.updateActorFromDB(foundActor);
        return true;
    }

    public boolean deleteActor() { 
        if (checkEmpty(list)) return false;       

        Actor foundActor = (Actor)getById("Enter user's id to update: ");
        if (checkNull(foundActor)) return false;

        list.remove(foundActor);
        ActorCRUD.deleteActorFromDB(foundActor.getId());
        return true;
    }

    public void display(List<Actor> list, String title) {
        if (checkEmpty(list)) return;
        
        if (!title.isBlank()) Menu.showTitle(title);

        list.forEach(item -> System.out.println(item));
    }

    public void searchActor() {
        if (checkEmpty(list)) return;

        display(getActorBy("Enter actor's propety to search: "), DISPLAY_TITLE);
    }

    public List<Actor> getActorBy(String message) {
        return searchBy(getString(message, false));
    }
    
    @Override
    public List<Actor> searchBy(String propety) {
        List<Actor> result = new ArrayList<>();
        for (Actor item : list) 
            if (item.getActorName().equals(propety)
            ) result.add(item);
        return result;
    }
    
}
