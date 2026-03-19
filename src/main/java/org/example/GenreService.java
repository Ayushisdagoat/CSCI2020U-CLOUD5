package org.example;

import java.util.ArrayList;

/*
 GenreService.java
 This class manages the genre/category system for the game catalogue.
 The system is limited to a maximum of 5 genres at any time.
 Only admins can add or remove genres.
 Users can view the list of available genres.

 */
public class GenreService {

    // Stores all genre names. Maximum of 5 allowed at any time.
    private static ArrayList<String> genres = new ArrayList<>();

    private static final int MAX_GENRES = 5;

    public static void initDB() {
        genres.add("Action");
        genres.add("RPG");
        genres.add("Sports");
        genres.add("Shooter");
        genres.add("Sandbox");

        System.out.println("GenreService ready. Default genres loaded.");
    }

    public boolean addGenre(String name) {
         if (genres.size() >= MAX_GENRES) {
            System.out.println("Maximum of " + MAX_GENRES + " genres allowed.");
            return false;
        }

         for (String genre : genres) {
            if (genre.equalsIgnoreCase(name)) {
                System.out.println("Genre '" + name + "' already exists.");
                return false;
            }
        }

        genres.add(name);
        return true;
    }


    public boolean removeGenre(String name) {
         for (int i = 0; i < genres.size(); i++) {
            if (genres.get(i).equalsIgnoreCase(name)) {
                genres.remove(i);
                return true;
            }
        }
        System.out.println("Genre '" + name + "' not found.");
        return false;
    }


    public String getAllGenres() {
        if (genres.isEmpty()) return "No genres available.";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            sb.append((i + 1)).append(". ").append(genres.get(i)).append("\n");
        }
        return sb.toString();
    }


    public boolean genreExists(String name) {
        for (String genre : genres) {
            if (genre.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }


    public ArrayList<String> getGenreList() {
        return genres;
    }

     public int getRemainingSlots() {
        return MAX_GENRES - genres.size();
    }
}