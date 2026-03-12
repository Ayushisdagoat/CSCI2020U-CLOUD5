package org.example;

import java.sql.*;

public class GameService {

    private static final String DB_URL = "jdbc:sqlite:game_catalogue.db";

    public static void initDB() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS games (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    genre TEXT,
                    platform TEXT,
                    price REAL DEFAULT 0.0,
                    description TEXT,
                    trailer_url TEXT,
                    average_rating REAL DEFAULT 0.0
                )
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addGame(String title, String genre, String platform, double price, String description, String trailerUrl) {
        String query = "INSERT INTO games (title, genre, platform, price, description, trailer_url) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, genre);
            stmt.setString(3, platform);
            stmt.setDouble(4, price);
            stmt.setString(5, description);
            stmt.setString(6, trailerUrl);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editGame(int id, String title, String genre, String platform, double price, String description, String trailerUrl) {
        String query = "UPDATE games SET title = ?, genre = ?, platform = ?, price = ?, description = ?, trailer_url = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, genre);
            stmt.setString(3, platform);
            stmt.setDouble(4, price);
            stmt.setString(5, description);
            stmt.setString(6, trailerUrl);
            stmt.setInt(7, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeGame(int id) {
        String query = "DELETE FROM games WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getAllGames() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery("SELECT * FROM games ORDER BY title ASC");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getGameById(int id) {
        String query = "SELECT * FROM games WHERE id = ?";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}