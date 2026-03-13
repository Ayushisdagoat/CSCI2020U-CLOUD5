package org.example;

import java.util.ArrayList;

public class WishlistService {

    private static ArrayList<String> usernames = new ArrayList<>();
    private static ArrayList<Integer> gameIds = new ArrayList<>();
    private static ArrayList<String> gameTitles = new ArrayList<>();

    public static void initDB() {
        System.out.println("WishlistService ready.");
    }

    public boolean addToWishlist(String username, int gameId, String gameTitle) {
        if (isInWishlist(username, gameId)) {
            System.out.println("Game already in wishlist.");
            return false;
        }

        usernames.add(username);
        gameIds.add(gameId);
        gameTitles.add(gameTitle);
        return true;
    }

    public boolean removeFromWishlist(String username, int gameId) {
        for (int i = 0; i < usernames.size(); i++) {
            if (usernames.get(i).equals(username) && gameIds.get(i) == gameId) {
                usernames.remove(i);
                gameIds.remove(i);
                gameTitles.remove(i);
                return true;
            }
        }
        System.out.println("Game not found in wishlist.");
        return false;
    }

    public String getWishlist(String username) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < usernames.size(); i++) {
            if (usernames.get(i).equals(username)) {
                sb.append("Game ID: ").append(gameIds.get(i))
                        .append(" | Title: ").append(gameTitles.get(i))
                        .append("\n");
            }
        }
        return sb.isEmpty() ? "Wishlist is empty." : sb.toString();
    }

    public boolean isInWishlist(String username, int gameId) {
        for (int i = 0; i < usernames.size(); i++) {
            if (usernames.get(i).equals(username) && gameIds.get(i) == gameId) {
                return true;
            }
        }
        return false;
    }

    public boolean clearWishlist(String username) {
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