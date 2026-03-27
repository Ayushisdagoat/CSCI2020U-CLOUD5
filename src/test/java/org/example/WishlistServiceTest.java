package org.example;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class WishlistServiceTest {

    private void clearData() throws Exception {
        Field usernames = WishlistService.class.getDeclaredField("usernames");
        Field gameIds = WishlistService.class.getDeclaredField("gameIds");
        Field gameTitles = WishlistService.class.getDeclaredField("gameTitles");

        usernames.setAccessible(true);
        gameIds.setAccessible(true);
        gameTitles.setAccessible(true);

        ((ArrayList<String>) usernames.get(null)).clear();
        ((ArrayList<Integer>) gameIds.get(null)).clear();
        ((ArrayList<String>) gameTitles.get(null)).clear();
    }

    @Test
    void testInitDB() {
        assertDoesNotThrow(() -> WishlistService.initDB());
    }

    @Test
    void testAddToWishlist() throws Exception {
        clearData();

        WishlistService wishlist = new WishlistService();
        boolean result = wishlist.addToWishlist("john", 730, "Counter-Strike 2");

        assertTrue(result);
        assertTrue(wishlist.isInWishlist("john", 730));
    }

    @Test
    void testAddDuplicateGame() throws Exception {
        clearData();

        WishlistService wishlist = new WishlistService();
        wishlist.addToWishlist("john", 730, "Counter-Strike 2");

        boolean result = wishlist.addToWishlist("john", 730, "Counter-Strike 2");

        assertFalse(result);
    }

    @Test
    void testRemoveFromWishlist() throws Exception {
        clearData();

        WishlistService wishlist = new WishlistService();
        wishlist.addToWishlist("john", 730, "Counter-Strike 2");

        boolean result = wishlist.removeFromWishlist("john", 730);

        assertTrue(result);
        assertFalse(wishlist.isInWishlist("john", 730));
    }

    @Test
    void testRemoveGameThatDoesNotExist() throws Exception {
        clearData();

        WishlistService wishlist = new WishlistService();
        boolean result = wishlist.removeFromWishlist("john", 999);

        assertFalse(result);
    }

    @Test
    void testGetWishlist() throws Exception {
        clearData();

        WishlistService wishlist = new WishlistService();
        wishlist.addToWishlist("john", 730, "Counter-Strike 2");
        wishlist.addToWishlist("john", 570, "Apex 2");
        wishlist.addToWishlist("jane", 440, "Portal 2");

        String result = wishlist.getWishlist("john");

        assertTrue(result.contains("Counter-Strike 2"));
        assertTrue(result.contains("Apex 2"));
        assertFalse(result.contains("Portal 2"));
    }

    @Test
    void testEmptyWishlist() throws Exception {
        clearData();

        WishlistService wishlist = new WishlistService();
        String result = wishlist.getWishlist("john");

        assertEquals("Wishlist is empty.", result);
    }

    @Test
    void testIsInWishlist() throws Exception {
        clearData();

        WishlistService wishlist = new WishlistService();
        wishlist.addToWishlist("john", 730, "Counter-Strike 2");

        assertTrue(wishlist.isInWishlist("john", 730));
    }

    @Test
    void testIsNotInWishlist() throws Exception {
        clearData();

        WishlistService wishlist = new WishlistService();

        assertFalse(wishlist.isInWishlist("john", 730));
    }

    @Test
    void testClearWishlist() throws Exception {
        clearData();

        WishlistService wishlist = new WishlistService();
        wishlist.addToWishlist("john", 730, "Counter-Strike 2");
        wishlist.addToWishlist("john", 570, "Dota 2");
        wishlist.addToWishlist("jane", 440, "Portal 2");

        boolean result = wishlist.clearWishlist("john");

        assertTrue(result);
        assertEquals("Wishlist is empty.", wishlist.getWishlist("john"));
        assertTrue(wishlist.isInWishlist("jane", 440));
    }
}