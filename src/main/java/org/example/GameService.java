package org.example;

import java.util.ArrayList;

/*
 GameService.java

 This class handles everything related to games in the catalogue.
 Games are stored across multiple ArrayLists — one list per field.
 Each index across all lists represents ONE game.

 Example:
   index 0 = (1, "Elden Ring", "RPG", "PC", 79.99, "A dark RPG...", "", 4.5)
   index 1 = (2, "Minecraft",  "Sandbox", "PC", 39.99, "Build stuff...", "", 0.0)

 Only admins can add, edit, or remove games.
 All users can view games.
 */
public class GameService {

    // --- DATA STORAGE ---
    // Each ArrayList stores one "column" of game data.
    // The same index across all lists = the same game.
    private static ArrayList<Integer> ids            = new ArrayList<>(); // unique ID for each game
    private static ArrayList<String>  titles         = new ArrayList<>(); // game title
    private static ArrayList<String>  genres         = new ArrayList<>(); // genre (e.g. RPG, Shooter)
    private static ArrayList<String>  platforms      = new ArrayList<>(); // platform (e.g. PC, Xbox)
    private static ArrayList<Double>  prices         = new ArrayList<>(); // price in dollars
    private static ArrayList<String>  descriptions   = new ArrayList<>(); // short description
    private static ArrayList<String>  trailerUrls    = new ArrayList<>(); // link to trailer video
    private static ArrayList<Double>  averageRatings = new ArrayList<>(); // average star rating (0.0 - 5.0)

    // Tracks the next available ID. Starts at 1 and increases each time a game is added.
    private static int nextId = 1;

    /*
     initDB()

     Called once at startup to confirm GameService is ready.
     No setup needed since ArrayLists are empty and ready to use.
     */
    public static void initDB() {
        System.out.println("GameService ready.");
    }

    /*
     addGame(title, genre, platform, price, description, trailerUrl)

     ADMIN ONLY — Adds a new game to the catalogue.
     Assigns the game a unique ID automatically.
     All new games start with an average rating of 0.0.

     Returns true if the game was added successfully.
     */
    public boolean addGame(String title, String genre, String platform,
                           double price, String description, String trailerUrl) {
        ids.add(nextId++);         // assign next available ID, then increment
        titles.add(title);
        genres.add(genre);
        platforms.add(platform);
        prices.add(price);
        descriptions.add(description);
        trailerUrls.add(trailerUrl);
        averageRatings.add(0.0);   // new games start with no rating
        return true;
    }

    /*
     editGame(id, title, genre, platform, price, description, trailerUrl)

     ADMIN ONLY — Updates an existing game's details by its ID.
     First finds the game's index using its ID, then updates all fields.

     Returns true if the game was found and updated.
     Returns false if no game with that ID exists.
     */
    public boolean editGame(int id, String title, String genre, String platform,
                            double price, String description, String trailerUrl) {
        int index = ids.indexOf(id); // find the position of this game by its ID
        if (index == -1) return false; // game not found

        // Update all fields at the found index
        titles.set(index, title);
        genres.set(index, genre);
        platforms.set(index, platform);
        prices.set(index, price);
        descriptions.set(index, description);
        trailerUrls.set(index, trailerUrl);
        return true;
    }

    /*
     removeGame(id)

     ADMIN ONLY — Deletes a game from the catalogue by its ID.
     Removes the game's entry from every ArrayList at the same index.

     Returns true if the game was found and removed.
     Returns false if no game with that ID exists.
     */
    public boolean removeGame(int id) {
        int index = ids.indexOf(id); // find the game's position
        if (index == -1) return false; // game not found

        // Remove this game from every list at the same index
        ids.remove(index);
        titles.remove(index);
        genres.remove(index);
        platforms.remove(index);
        prices.remove(index);
        descriptions.remove(index);
        trailerUrls.remove(index);
        averageRatings.remove(index);
        return true;
    }

    /*
     getAllGames()

     Returns a formatted string listing all games in the catalogue.
     Shows ID, title, genre, platform, price and rating for each game.

     Example output:
       ID: 1 | Title: Elden Ring | Genre: RPG | Platform: PC | Price: $79.99 | Rating: 4.5
       ID: 2 | Title: Minecraft  | Genre: Sandbox | Platform: PC | Price: $39.99 | Rating: 0.0
     */
    public String getAllGames() {
        if (ids.isEmpty()) return "No games in catalogue.";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            sb.append("ID: ").append(ids.get(i))
                    .append(" | Title: ").append(titles.get(i))
                    .append(" | Genre: ").append(genres.get(i))
                    .append(" | Platform: ").append(platforms.get(i))
                    .append(" | Price: $").append(prices.get(i))
                    .append(" | Rating: ").append(averageRatings.get(i))
                    .append("\n");
        }
        return sb.toString();
    }

    /*
     getIndexById(id)

     Helper method — finds the ArrayList index of a game by its ID.
     Returns -1 if no game with that ID exists.
     Used internally to locate a game before reading or updating it.
     */
    public int getIndexById(int id) {
        return ids.indexOf(id);
    }

    // --- GETTERS ---
    // These methods return individual fields for a game at a given index.
    // The frontend uses these to display game details on screen.

    /** Returns the unique ID of the game at the given index */
    public int getId(int index) { return ids.get(index); }

    /** Returns the title of the game at the given index */
    public String getTitle(int index) { return titles.get(index); }

    /** Returns the genre of the game at the given index */
    public String getGenre(int index) { return genres.get(index); }

    /** Returns the platform of the game at the given index */
    public String getPlatform(int index) { return platforms.get(index); }

    /** Returns the price of the game at the given index */
    public double getPrice(int index) { return prices.get(index); }

    /** Returns the description of the game at the given index */
    public String getDescription(int index) { return descriptions.get(index); }

    /** Returns the trailer URL of the game at the given index */
    public String getTrailerUrl(int index) { return trailerUrls.get(index); }

    /** Returns the average star rating of the game at the given index */
    public double getAverageRating(int index) { return averageRatings.get(index); }

    /** Returns the total number of games currently in the catalogue */
    public int getSize() { return ids.size(); }

    /*
     updateAverageRating(id, rating)

     Called automatically by ReviewService whenever a review is approved.
     Updates the game's average star rating based on all approved reviews.
     */
    public void updateAverageRating(int id, double rating) {
        int index = ids.indexOf(id);
        if (index != -1) { // only update if the game exists
            averageRatings.set(index, rating);
        }
    }

    /*
     getAllIds()

     Returns the full list of game IDs.
     Useful when the frontend needs to loop through all games.
     */
    public ArrayList<Integer> getAllIds() { return ids; }
}