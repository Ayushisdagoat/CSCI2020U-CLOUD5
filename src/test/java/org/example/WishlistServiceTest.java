package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class WishlistServiceTest {

    private WishlistService wishlistService;

    @BeforeEach
    void setUp() throws Exception {
        wishlistService = new WishlistService();
        resetWishlistServiceState();
    }

    @SuppressWarnings("unchecked")
    private void resetWishlistServiceState() throws Exception {
        String[] fields = { "usernames", "gameIds", "gameTitles" };

        for (String fieldName : fields) {
            Field field = WishlistService.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            ((ArrayList<?>) field.get(null)).clear();
        }
    }

    @Test
    void testMultipleGamesInWishlist() {
        wishlistService.addToWishlist("user1", 1, "Game 1");
        wishlistService.addToWishlist("user1", 2, "Game 2");

        String result = wishlistService.getWishlist("user1");

        assertTrue(result.contains("Game ID: 1"));
        assertTrue(result.contains("Game ID: 2"));
    }

    @Test
    void testDifferentUsersHaveSeparateWishlists() {
        wishlistService.addToWishlist("user1", 1, "Game 1");
        wishlistService.addToWishlist("user2", 2, "Game 2");

        String result1 = wishlistService.getWishlist("user1");
        String result2 = wishlistService.getWishlist("user2");

        assertTrue(result1.contains("Game ID: 1"));
        assertFalse(result1.contains("Game ID: 2"));

        assertTrue(result2.contains("Game ID: 2"));
        assertFalse(result2.contains("Game ID: 1"));
    }

    @Test
    void testEmptyWishlist() {
        String result = wishlistService.getWishlist("user1");
        assertEquals("Wishlist is empty.", result);
    }

    @Test
    void testRemoveGameFromWishlist() {
        wishlistService.addToWishlist("user1", 1, "Game 1");
        wishlistService.removeFromWishlist("user1", 1);

        String result = wishlistService.getWishlist("user1");
        assertEquals("Wishlist is empty.", result);
    }

    @Test
    void testRemoveGameThatDoesNotExist() {
        wishlistService.addToWishlist("user1", 1, "Game 1");
        wishlistService.removeFromWishlist("user1", 2);

        String result = wishlistService.getWishlist("user1");
        assertTrue(result.contains("Game ID: 1"));
    }
}