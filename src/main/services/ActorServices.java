package main.services;

import main.base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.DAO.ActorDAO;
import main.models.Actor;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;

/**
 *
 * @author trann
 */
public class ActorServices extends ListManager<Actor> {

    public ActorServices() throws IOException {
        super(Actor.className());
        list = ActorDAO.getAllActor();
    }

    public void adminMenu() throws IOException {
        Menu.showManagerMenu(
                "Actor Management",
                null,
                new Menu.MenuOption[]{
                    new Menu.MenuOption("Add actor", () -> showSuccess(addActor()), true),
                    new Menu.MenuOption("Delete actor", () -> showSuccess(deleteActor()), true),
                    new Menu.MenuOption("Update actor", () -> showSuccess(updateActor()), true),
                    new Menu.MenuOption("Search actor", () -> searchActor(), true),
                    new Menu.MenuOption("Show all actor", () -> displayActors(list, "List of Actor"), false),
                    new Menu.MenuOption("Back", () -> {
                        /* Exit action */ }, false)
                },
                null
        );
    }

    public boolean addActor() {
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "A");
        String name = getString("Enter actor's name", false);

        list.add(new Actor(id, name));
        ActorDAO.addActorToDB(list.getLast());
        return true;
    }

    public boolean updateActor() {
        if (checkEmpty(list)) {
            return false;
        }

        Actor foundActor = (Actor) getById("Enter user's id");
        if (checkNull(foundActor)) {
            return false;
        }

        String name = getString("Enter actor's name", true);
        if (!name.isEmpty()) {
            foundActor.setActorName(name);
        }

        ActorDAO.updateActorFromDB(foundActor);
        return true;
    }

    public boolean deleteActor() {
        if (checkEmpty(list)) {
            return false;
        }

        Actor foundActor = (Actor) getById("Enter user's id");
        if (checkNull(foundActor)) {
            return false;
        }

        list.remove(foundActor);
        ActorDAO.deleteActorFromDB(foundActor.getId());
        return true;
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
        for (Actor item : list) {
            if (item.getId().equals(propety)
                    || item.getActorName().contains(propety.trim().toLowerCase())) {
                result.add(item);
            }
        }
        return result;
    }

    public void displayActors(List<Actor> actors, String title) {
        System.out.println(title);
        System.out.println("|----------------------------------------------------");

        if (actors.isEmpty()) {
            System.out.println("No actors available.");
            return;
        }
        System.out.printf("|%-15s | %-30s\n |", "Actor ID", "Actor Name");
        System.out.println("|----------------------------------------------------");

        for (Actor actor : actors) {
            System.out.printf("%-15s | %-30s\n",
                    actor.getId(),
                    actor.getActorName());
        }

        System.out.println("|----------------------------------------------------");
    }
}
