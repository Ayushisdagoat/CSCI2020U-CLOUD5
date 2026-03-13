package org.example;

import java.util.ArrayList;

public class GameService {

    private static ArrayList<Integer> ids = new ArrayList<>();
    private static ArrayList<String> titles = new ArrayList<>();
    private static ArrayList<String> genres = new ArrayList<>();
    private static ArrayList<String> platforms = new ArrayList<>();
    private static ArrayList<Double> prices = new ArrayList<>();
    private static ArrayList<String> descriptions = new ArrayList<>();
    private static ArrayList<String> trailerUrls = new ArrayList<>();
    private static ArrayList<Double> averageRatings = new ArrayList<>();
    private static int nextId = 1;

    public static void initDB() {
        System.out.println("GameService ready.");
    }

    public boolean addGame(String title, String genre, String platform, double price, String description, String trailerUrl) {
        ids.add(nextId++);
        titles.add(title);
        genres.add(genre);
        platforms.add(platform);
        prices.add(price);
        descriptions.add(description);
        trailerUrls.add(trailerUrl);
        averageRatings.add(0.0);
        return true;
    }

    public boolean editGame(int id, String title, String genre, String platform, double price, String description, String trailerUrl) {
        int index = ids.indexOf(id);
        if (index == -1) return false;

        titles.set(index, title);
        genres.set(index, genre);
        platforms.set(index, platform);
        prices.set(index, price);
        descriptions.set(index, description);
        trailerUrls.set(index, trailerUrl);
        return true;
    }

    public boolean removeGame(int id) {
        int index = ids.indexOf(id);
        if (index == -1) return false;

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

    public int getIndexById(int id) {
        return ids.indexOf(id);
    }

    public int getId(int index) {
        return ids.get(index); }
    public String getTitle(int index) {
        return titles.get(index); }
    public String getGenre(int index) {
        return genres.get(index); }
    public String getPlatform(int index) {
        return platforms.get(index); }
    public double getPrice(int index) {
        return prices.get(index); }
    public String getDescription(int index) {
        return descriptions.get(index); }
    public String getTrailerUrl(int index) {
        return trailerUrls.get(index); }
    public double getAverageRating(int index) {
        return averageRatings.get(index); }
    public int getSize() { return ids.size(); }

    public void updateAverageRating(int id, double rating) {
        int index = ids.indexOf(id);
        if (index != -1) {
            averageRatings.set(index, rating);
        }
    }

    public ArrayList<Integer> getAllIds() { return ids; }
}