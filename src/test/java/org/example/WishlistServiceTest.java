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
        resetWishlistState();
    }

    @SuppressWarnings("unchecked")
    private void resetWishlistState() throws Exception {
        Field usernamesField = WishlistService.class.getDeclaredField("usernames");
        Field gameIdsField = WishlistService.class.getDeclaredField("gameIds");
        Field gameTitlesField = WishlistService.class.getDeclaredField("gameTitles");

        usernamesField.setAccessible(true);
        gameIdsField.setAccessible(true);
        gameTitlesField.setAccessible(true);

        ((ArrayList<String>) usernamesField.get(null)).clear();
        ((ArrayList<Integer>) gameIdsField.get(null)).clear();
        ((ArrayList<String>) gameTitlesField.get(null)).clear();
    }

    @Test
    void initDB_shouldNotThrowException() {
        assertDoesNotThrow(WishlistService::initDB);
    }

    @Test
    void addToWishlist_shouldAddGameWhenNotAlreadyPresent() {
        boolean result = wishlistService.addToWishlist("john", 730, "Counter-Strike 2");

        assertTrue(result);
        assertTrue(wishlistService.isInWishlist("john", 730));
    }

    @Test
    void addToWishlist_shouldReturnFalseForDuplicateGameSameUser() {
        wishlistService.addToWishlist("john", 730, "Counter-Strike 2");

        boolean result = wishlistService.addToWishlist("john", 730, "Counter-Strike 2");

        assertFalse(result);
    }

    @Test
    void removeFromWishlist_shouldRemoveExistingGame() {
        wishlistService.addToWishlist("john", 730, "Counter-Strike 2");

        boolean result = wishlistService.removeFromWishlist("john", 730);

        assertTrue(result);
        assertFalse(wishlistService.isInWishlist("john", 730));
    }

    @Test
    void removeFromWishlist_shouldReturnFalseWhenGameNotFound() {
        boolean result = wishlistService.removeFromWishlist("john", 999);

        assertFalse(result);
    }

    @Test
    void getWishlist_shouldReturnWishlistContentsForSpecificUser() {
        wishlistService.addToWishlist("john", 730, "Counter-Strike 2");
        wishlistService.addToWishlist("john", 570, "Dota 2");
        wishlistService.addToWishlist("jane", 440, "Portal 2");

        String wishlist = wishlistService.getWishlist("john");

        assertTrue(wishlist.contains("Game ID: 730 | Title: Counter-Strike 2"));
        assertTrue(wishlist.contains("Game ID: 570 | Title: Dota 2"));
        assertFalse(wishlist.contains("Portal 2"));
    }

    @Test
    void getWishlist_shouldReturnEmptyMessageWhenNoGamesExistForUser() {
        String wishlist = wishlistService.getWishlist("john");

        assertEquals("Wishlist is empty.", wishlist);
    }

    @Test
    void isInWishlist_shouldReturnTrueWhenGameExistsForUser() {
        wishlistService.addToWishlist("john", 730, "Counter-Strike 2");

        assertTrue(wishlistService.isInWishlist("john", 730));
    }

    @Test
    void isInWishlist_shouldReturnFalseWhenGameDoesNotExistForUser() {
        assertFalse(wishlistService.isInWishlist("john", 730));
    }

    @Test
    void clearWishlist_shouldRemoveOnlySpecifiedUsersGames() {
        wishlistService.addToWishlist("john", 730, "Counter-Strike 2");
        wishlistService.addToWishlist("john", 570, "Dota 2");
        wishlistService.addToWishlist("jane", 440, "Portal 2");

        boolean result = wishlistService.clearWishlist("john");

        assertTrue(result);
        assertEquals("Wishlist is empty.", wishlistService.getWishlist("john"));
        assertTrue(wishlistService.isInWishlist("jane", 440));
    }
}