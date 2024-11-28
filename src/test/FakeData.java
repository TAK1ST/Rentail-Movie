/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.util.ArrayList;
import java.util.List;
import main.DAO.ActorDAO;
import main.DAO.GenreDAO;
import main.DAO.UserDAO;
import static main.controllers.Managers.getAM;
import static main.controllers.Managers.getGM;
import static main.controllers.Managers.getMM;
import static main.controllers.Managers.getUM;
import main.dto.Actor;
import main.dto.Genre;
import main.dto.Movie;
import main.dto.User;
import static main.utils.Utility.toDate;

/**
 *
 * @author trann
 */
public class FakeData {
    
    public static boolean makeAllFakeData() {
        return makeFakeUser() 
                && makeFakeActor() 
                && makeFakeGenre() 
                && makeFakeMovie();
    }
    
    public static boolean makeFakeUser() {
        
        List<User> temp = new ArrayList<>();
        temp.add(new User("U0000002", "thien", "newPass123", 2, "Thien Tran", "123 Main St", "9876543210", "thien@example.com"));
        temp.add(new User("U0000003", "kiet", "newPass456", 1, "Kiet Tran", "456 Elm St", "8765432109", "kiet@example.com"));
        temp.add(new User("U0000004", "kietse", "newPass789", 2, "Ngoc Thien", "789 Oak St", "7654321098", "kietse@example.com"));
        temp.add(new User("U0000005", "duongngo", "newPass012", 1, "Duong Ngo", "321 Pine St", "6543210987", "duongngo@example.com"));
        temp.add(new User("U0000006", "duongbingo", "newPass345", 2, "Bingo Duong", "654 Cedar St", "5432109876", "duongbingo@example.com"));
        temp.add(new User("U0000007", "thiendepzai", "newPass678", 2, "Thien Depzai", "987 Maple St", "4321098765", "thiendepzai@example.com"));
        temp.add(new User("U0000008", "3changlinh", "newPass901", 2, "Chang Linh", "147 Birch St", "3210987654", "3changlinh@example.com"));
        temp.add(new User("U0000009", "success", "newPass234", 1, "Successful Tran", "258 Walnut St", "2109876543", "success@example.com"));
        temp.add(new User("U0000010", "tttt", "newPass567", 2, "T Tran", "369 Spruce St", "1098765432", "tttt@example.com"));
        temp.add(new User("U0000011", "kkkk", "newPass890", 2, "KK Tran", "741 Cherry St", "0987654321", "kkkk@example.com"));
        
        for (User item : temp) {
            UserDAO.addUserToDB(item);
            getUM().getList().add(item);
        }
        
        return true;
    }
    
    public static boolean makeFakeActor() {
        List<Actor> temp = new ArrayList<>();
        temp.add(new Actor("A0000001", "Leonardo DiCaprio"));
        temp.add(new Actor("A0000002", "Scarlett Johansson"));
        temp.add(new Actor("A0000003", "Robert Downey Jr."));
        temp.add(new Actor("A0000004", "Jennifer Lawrence"));
        temp.add(new Actor("A0000005", "Denzel Washington"));
        temp.add(new Actor("A0000006", "Emma Stone"));
        temp.add(new Actor("A0000007", "Chris Hemsworth"));
        temp.add(new Actor("A0000008", "Angelina Jolie"));
        temp.add(new Actor("A0000009", "Tom Cruise"));
        temp.add(new Actor("A0000010", "Margot Robbie"));   
        
        for (Actor item : temp) {
            ActorDAO.addActorToDB(item);
            getAM().getList().add(item);
        }
        
        return true;
    }
    
    public static boolean makeFakeGenre() {
        List<Genre> temp = new ArrayList<>();
        temp.add(new Genre("G0000001", "Horror"));
        temp.add(new Genre("G0000002", "Comedy"));
        temp.add(new Genre("G0000003", "Action"));
        temp.add(new Genre("G0000004", "Drama"));
        temp.add(new Genre("G0000005", "Science Fiction"));
        temp.add(new Genre("G0000006", "Fantasy"));
        temp.add(new Genre("G0000007", "Romance"));
        temp.add(new Genre("G0000008", "Thriller"));
        temp.add(new Genre("G0000009", "Mystery"));
        temp.add(new Genre("G0000010", "Adventure"));
        
        for (Genre item : temp) {
            GenreDAO.addGenreToDB(item);
            getGM().getList().add(item);
        }
        
        return true;
    }
    
    public static boolean makeFakeMovie() {
        getMM().addMovie(new Movie("M0000001", "The Substance", "A horror movie about a mysterious entity.", 4.5, 
                   List.of("G0000001", "G0000005", "G0000008"), 
                   List.of("A0000001", "A0000005", "A0000010"), 
                   "English", toDate("10/10/2024"), 100, 20));

        getMM().addMovie(new Movie("M0000002", "Laugh Out Loud", "A hilarious comedy that will leave you in stitches.", 4.8, 
                           List.of("G0000002", "G0000006"), 
                           List.of("A0000002", "A0000007", "A0000008", "A0000009"), 
                           "English", toDate("01/01/2023"), 80, 15));

        getMM().addMovie(new Movie("M0000003", "Cosmic Wars", "An epic space adventure to save the galaxy.", 4.9, 
                           List.of("G0000003", "G0000006", "G0000005"), 
                           List.of("A0000003", "A0000008", "A0000001", "A0000005"), 
                           "English", toDate("05/05/2022"), 120, 25));

        getMM().addMovie(new Movie("M0000004", "Love in Paris", "A romantic drama set in the City of Love.", 4.6, 
                           List.of("G0000007", "G0000004", "G0000009", "G0000006"), 
                           List.of("A0000004", "A0000009", "A0000010"), 
                           "French", toDate("14/02/2023"), 90, 18));

        getMM().addMovie(new Movie("M0000005", "Mysterious Island", "A thriller about survival and secrets.", 4.7, 
                           List.of("G0000008", "G0000009", "G0000003", "G0000005", "G0000006"), 
                           List.of("A0000005", "A0000010", "A0000002", "A0000008"), 
                           "English", toDate("12/12/2023"), 95, 10));

        getMM().addMovie(new Movie("M0000006", "The Enchanted Forest", "A magical tale of courage and friendship.", 4.5, 
                           List.of("G0000006", "G0000010", "G0000001"), 
                           List.of("A0000001", "A0000006", "A0000007", "A0000003"), 
                           "English", toDate("20/03/2023"), 110, 12));

        getMM().addMovie(new Movie("M0000007", "Robot Revolution", "A science fiction movie about AI gone rogue.", 4.3, 
                           List.of("G0000003", "G0000005", "G0000008"), 
                           List.of("A0000003", "A0000008", "A0000001"), 
                           "English", toDate("10/10/2022"), 85, 8));

        getMM().addMovie(new Movie("M0000008", "The Haunted Mansion", "A classic horror tale in a cursed house.", 4.2, 
                           List.of("G0000001", "G0000009", "G0000004", "G0000002"), 
                           List.of("A0000002", "A0000007", "A0000006"), 
                           "English", toDate("31/10/2022"), 70, 5));
        
        return true;
    }
    
}
