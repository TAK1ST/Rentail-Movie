/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.util.ArrayList;
import java.util.List;
import main.constants.AccRole;
import main.dao.ActorDAO;
import main.dao.GenreDAO;
import main.dao.AccountDAO;
import static main.controllers.Managers.getAM;
import static main.controllers.Managers.getGM;
import static main.controllers.Managers.getMM;
import static main.controllers.Managers.getRM;
import static main.controllers.Managers.getRTM;
import static main.controllers.Managers.getUM;
import main.dao.RentalDAO;
import main.dao.ReviewDAO;
import main.dto.Actor;
import main.dto.Genre;
import main.dto.Movie;
import main.dto.Rental;
import main.dto.Review;
import main.dto.User;
import static main.utils.Utility.toDate;

/**
 *
 * @author trann
 */
public class FakeData {
    
    public static void init() {
        
    }
    
    public static boolean makeAllFakeData() {
        return makeFakeUser() 
                && makeFakeActor() 
                && makeFakeGenre() 
                && makeFakeMovie();
    }
    
    public static boolean makeFakeUser() {
        
        List<User> temp = new ArrayList<>();
        temp.add(new User("U0000002", "thien", "newPass123", AccRole.STAFF, "Thien Tran", "123 Main St", "9876543210", "thien@example.com"));
        temp.add(new User("U0000003", "kiet", "newPass456", AccRole.CUSTOMER, "Kiet Tran", "456 Elm St", "8765432109", "kiet@example.com"));
        temp.add(new User("U0000004", "kietse", "newPass789", AccRole.CUSTOMER, "Ngoc Thien", "789 Oak St", "7654321098", "kietse@example.com"));
        temp.add(new User("U0000005", "duongngo", "newPass012", AccRole.STAFF, "Duong Ngo", "321 Pine St", "6543210987", "duongngo@example.com"));
        temp.add(new User("U0000006", "duongbingo", "newPass345", AccRole.ADMIN, "Bingo Duong", "654 Cedar St", "5432109876", "duongbingo@example.com"));
        temp.add(new User("U0000007", "thiendepzai", "newPass678", AccRole.PREMIUM, "Thien Depzai", "987 Maple St", "4321098765", "thiendepzai@example.com"));
        temp.add(new User("U0000008", "3changlinh", "newPass901", AccRole.CUSTOMER, "Chang Linh", "147 Birch St", "3210987654", "3changlinh@example.com"));
        temp.add(new User("U0000009", "success", "newPass234", AccRole.CUSTOMER, "Successful Tran", "258 Walnut St", "2109876543", "success@example.com"));
        temp.add(new User("U0000010", "tttt", "newPass567", AccRole.STAFF, "T Tran", "369 Spruce St", "1098765432", "tttt@example.com"));
        temp.add(new User("U0000011", "kkkk", "newPass890", AccRole.PREMIUM, "KK Tran", "741 Cherry St", "0987654321", "kkkk@example.com"));
        
        for (User item : temp) {
            AccountDAO.addUserToDB(item);
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
    
    public static boolean makeFakeRental() {
        List<Rental> temp = new ArrayList<>();
        
        temp.add(new Rental("RT000001", "U0000002", "M0000001", toDate("12/04/2023"), toDate("16/04/2023"), 100.15, 0));
        temp.add(new Rental("RT000002", "U0000003", "M0000002", toDate("05/05/2023"), toDate("10/05/2023"), 120.50, 10.0));
        temp.add(new Rental("RT000003", "U0000004", "M0000003", toDate("01/06/2023"), toDate("03/06/2023"), 80.00, 5.0));
        temp.add(new Rental("RT000004", "U0000005", "M0000004", toDate("15/06/2023"), toDate("18/06/2023"), 90.25, 0));
        temp.add(new Rental("RT000005", "U0000006", "M0000005", toDate("20/07/2023"), toDate("25/07/2023"), 150.75, 20.0));
        temp.add(new Rental("RT000006", "U0000007", "M0000006", toDate("01/08/2023"), toDate("05/08/2023"), 50.00, 0));
        temp.add(new Rental("RT000007", "U0000008", "M0000007", toDate("10/09/2023"), toDate("15/09/2023"), 70.30, 15.0));
        temp.add(new Rental("RT000008", "U0000009", "M0000008", toDate("20/10/2023"), toDate("25/10/2023"), 110.00, 0));
        temp.add(new Rental("RT000009", "U0000010", "M0000001", toDate("01/11/2023"), toDate("06/11/2023"), 65.50, 5.0));
        temp.add(new Rental("RT000010", "U0000011", "M0000002", toDate("15/11/2023"), toDate("20/11/2023"), 125.00, 10.0));
        
        for (Rental item : temp) {
            RentalDAO.addRentalToDB(item);
            getRTM().getList().add(item);
        }
        
        return true;
    }
    
    public static boolean makeFakeReview() {
        List<Review> temp = new ArrayList<>();
        
        temp.add(new Review("R00000001", "M00000004", "U00000005", 5, "Absolutely fantastic movie!", toDate("12/04/2023")));
        temp.add(new Review("R00000002", "M00000002", "U00000003", 4, "Great watch, enjoyed the storyline.", toDate("15/04/2023")));
        temp.add(new Review("R00000003", "M00000006", "U00000007", 3, "It was okay, not the best.", toDate("18/04/2023")));
        temp.add(new Review("R00000004", "M00000001", "U00000002", 5, "Loved it! Would watch again.", toDate("20/04/2023")));
        temp.add(new Review("R00000005", "M00000005", "U00000004", 2, "Disappointed, expected better.", toDate("25/04/2023")));
        temp.add(new Review("R00000006", "M00000003", "U00000006", 4, "Good performances and direction.", toDate("28/04/2023")));
        temp.add(new Review("R00000007", "M00000007", "U00000008", 1, "Terrible, wouldn't recommend.", toDate("30/04/2023")));
        temp.add(new Review("R00000008", "M00000008", "U00000009", 5, "Amazing visuals and plot!", toDate("02/05/2023")));
        temp.add(new Review("R00000009", "M00000004", "U0000010", 3, "Average movie, could be better.", toDate("05/05/2023")));
        temp.add(new Review("R00000010", "M00000002", "U0000011", 4, "Enjoyed it overall, worth watching.", toDate("08/05/2023")));
        
        for (Review item : temp) {
            ReviewDAO.addReviewToDB(item);
            getRM().getList().add(item);
        }
 
        return true;
    }
}
