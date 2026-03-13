package org.example;

import java.util.ArrayList;

/*
 WishlistService.java

 This class handles the cart/wishlist feature.
 Each user has their own personal cart where they can save games.
 Games are identified by their Steam App ID and title.

 How it works:
   - User opens a game page and clicks "Add to Cart"
   - The game is saved to their personal cart
   - User clicks the "Cart" button to see all saved games
   - Games stay in the cart until removed or the app is closed

 Each index across all three lists represents ONE cart entry.
 Example:
   index 0 = ("john", 730,  "Counter-Strike 2")
   index 1 = ("john", 570,  "Dota 2")
   index 2 = ("jane", 440,  "Portal 2")
 */
public class WishlistService {

    // --- DATA STORAGE ---
    // Three ArrayLists that together store all cart entries for all users.
    // The same index across all lists = the same cart entry.
    private static ArrayList<String>  usernames  = new ArrayList<>(); // who added the game
    private static ArrayList<Integer> gameIds    = new ArrayList<>(); // Steam App ID of the game
    private static ArrayList<String>  gameTitles = new ArrayList<>(); // display name of the game

    /*
     initDB()

     Called once at startup to confirm WishlistService is ready.
     No setup needed since ArrayLists start empty.
     */
    public static void initDB() {
        System.out.println("WishlistService ready.");
    }

    /*
     addToWishlist(username, gameId, gameTitle)

     USER — Adds a game to a user's cart.
     First checks if the game is already in their cart to avoid duplicates.

     Parameters:
       username  - the logged-in user's username
       gameId    - the Steam App ID of the game
       gameTitle - the display name of the game (shown in cart)

     Returns true if the game was added successfully.
     Returns false if the game is already in the cart.
     */
    public boolean addToWishlist(String username, int gameId, String gameTitle) {
        // Don't add the same game twice to the same user's cart
        if (isInWishlist(username, gameId)) {
            System.out.println("Game already in wishlist.");
            return false;
        }

        // Add the new cart entry across all three lists
        usernames.add(username);
        gameIds.add(gameId);
        gameTitles.add(gameTitle);
        return true;
    }

    /*
     removeFromWishlist(username, gameId)

     USER — Removes a specific game from a user's cart.
     Finds the matching entry by both username AND gameId,
     then removes it from all three lists at the same index.

     Returns true if the game was found and removed.
     Returns false if the game was not in the cart.
     */
    public boolean removeFromWishlist(String username, int gameId) {
        // Search for the specific cart entry
        for (int i = 0; i < usernames.size(); i++) {
            // Must match both the username AND the game ID
            if (usernames.get(i).equals(username) && gameIds.get(i) == gameId) {
                // Remove this entry from all three lists
                usernames.remove(i);
                gameIds.remove(i);
                gameTitles.remove(i);
                return true;
            }
        }
        System.out.println("Game not found in wishlist.");
        return false;
    }

    /*
     getWishlist(username)

     USER — Returns a formatted string of all games in a user's cart.
     Only shows entries that belong to the given username.

     Example output for user "john":
        Game ID: 730 | Title: Counter-Strike 2

     * Returns "Wishlist is empty." if the user has no games saved.
     */
    public String getWishlist(String username) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < usernames.size(); i++) {
            // Only include entries that belong to this user
            if (usernames.get(i).equals(username)) {
                sb.append("Game ID: ").append(gameIds.get(i))
                        .append(" | Title: ").append(gameTitles.get(i))
                        .append("\n");
            }
        }
        return sb.isEmpty() ? "Wishlist is empty." : sb.toString();
    }

    /*
     isInWishlist(username, gameId)

     Checks if a specific game is already in a user's cart.
     Used by addToWishlist() to prevent duplicate entries.
     Also used by the frontend to show "In Cart" instead of "Add to Cart".

     Returns true if the game is already in the cart.
     Returns false if it is not.
     */
    public boolean isInWishlist(String username, int gameId) {
        for (int i = 0; i < usernames.size(); i++) {
            // Must match both the username AND the game ID
            if (usernames.get(i).equals(username) && gameIds.get(i) == gameId) {
                return true;
            }
        }
        return false; // not found
    }

    /*
     clearWishlist(username)

     Removes ALL games from a user's cart at once.
     Loops backwards through the list so removing items
     does not affect the index of items not yet checked.

     Returns true when the cart has been cleared.
     */
    public boolean clearWishlist(String username) {
        // Loop backwards so removing items doesn't mess up the index
        for (int i = usernames.size() - 1; i >= 0; i--) {
            if (usernames.get(i).equals(username)) {
                usernames.remove(i);
                gameIds.remove(i);
                gameTitles.remove(i);
            }
        }
        return true;
    }
}