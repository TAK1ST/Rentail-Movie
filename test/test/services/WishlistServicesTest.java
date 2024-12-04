/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test.services;

import main.dto.Wishlist;
import main.services.WishlistServices;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tak
 */


public class WishlistServicesTest {

    private WishlistServices wishlistServices;

    @Before
    public void setUp() {
        wishlistServices = new WishlistServices();
    }

    @Test
    public void testAddWishlist() {
        // Given
        Wishlist wishlist = new Wishlist();
        wishlist.setId("WSH00001");
        wishlist.setCustomerId("CUS00001");
        wishlist.setMovieId("MOV00001");

        // When
        boolean result = wishlistServices.addWishlist(wishlist);

        // Then
        assertTrue(result);
        // Verify wishlist in database
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWishlistExceedsLimit() {
        // Given
        // Add MAX_WISHLIST_ITEMS + 1 items

//         When/Then
        // Should throw IllegalArgumentException
    }
}
