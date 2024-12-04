/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

/**
 *
 * @author tak
 */
package test.services;

import java.sql.SQLException;
import org.junit.*;
import java.time.LocalDate;
import main.dto.Wishlist;
import main.services.MovieServices;
import main.services.RentalServices;
import static main.services.RentalServices.calculateOverdueFine;
import main.services.WishlistServices;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class RentalServicesTest {

    @BeforeClass
    public static void setUpClass() throws SQLException {
//        test.TestDatabaseSetup.testAddAccount();
    }

//    @Before
//    
//    public void setUp() {
//        test.TestDatabaseSetup.cleanTestDatabase();
//        // Insert test data
//    }

    @Test
 
    public void testCalculateOverdueFine() {
        // Given
        LocalDate returnDate = LocalDate.now().minusDays(5);
        double movieRentalPrice = 10.0;

        // When
        double fine = calculateOverdueFine(returnDate, movieRentalPrice);

        // Then
        assertEquals(100.0, fine, 0.01); // 5 days * 2 * 10.0
    }

//    @Test

//    public void testReturnMovie() {
//        // Given
//        String movieId = "MOV00001";
//        String rentalId = "RNT00001";
//
//        // When
//        boolean result = RentalServices.returnMovie();
//
//        // Then
//        assertTrue(result);
//        // Add additional assertions to verify database state
//    }
}

