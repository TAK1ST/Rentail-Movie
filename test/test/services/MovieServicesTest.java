    
package test.services;

import java.sql.SQLException;
import main.services.MovieServices;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tak
 */

public class MovieServicesTest {

    @Test
    public void testCalculateAverageRating() throws SQLException{
        // Given
        String movieId = "MOV00001";
        // Insert test reviews

        // When
        double avgRating = MovieServices.calculateAverageRating(movieId);

        // Then
        assertEquals(4.5, avgRating, 0.01);
    }

    @Test
    public void testAdjustAvailableCopy() {
        // Given
        String movieId = "MOV00001";
        int amount = 1;

        // When
        boolean result = MovieServices.adjustAvailableCopy(movieId, amount);

        // Then
        assertTrue(result);
        // Verify available copies in database
    }
}
