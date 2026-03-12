package org.example;

import java.sql.*;

public class ReviewService {

    private static final String DB_URL = "jdbc:sqlite:game_catalogue.db";

    public static void initDB() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS reviews (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    game_id INTEGER NOT NULL,
                    username TEXT NOT NULL,
                    rating INTEGER NOT NULL CHECK(rating BETWEEN 1 AND 5),
                    comment TEXT,
                    approved INTEGER NOT NULL DEFAULT 0
                )
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean submitReview(int gameId, String username, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5.");
            return false;
        }

        if (hasReviewed(gameId, username)) {
            System.out.println("User has already reviewed this game.");
            return false;
        }

        String query = "INSERT INTO reviews (game_id, username, rating, comment, approved) VALUES (?, ?, ?, ?, 0)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, gameId);
            stmt.setString(2, username);
            stmt.setInt(3, rating);
            stmt.setString(4, comment);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean hasReviewed(int gameId, String username) {
        String query = "SELECT id FROM reviews WHERE game_id = ? AND username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, gameId);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getPendingReviews() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery("SELECT * FROM reviews WHERE approved = 0");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean approveReview(int reviewId) {
        String query = "UPDATE reviews SET approved = 1 WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reviewId);
            stmt.executeUpdate();

            updateAverageRating(getGameIdForReview(reviewId));
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean rejectReview(int reviewId) {
        String query = "DELETE FROM reviews WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reviewId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getApprovedReviews(int gameId) {
        String query = "SELECT * FROM reviews WHERE game_id = ? AND approved = 1";

        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, gameId);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getGameIdForReview(int reviewId) {
        String query = "SELECT game_id FROM reviews WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reviewId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("game_id");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void updateAverageRating(int gameId) {
        String query = "SELECT AVG(rating) FROM reviews WHERE game_id = ? AND approved = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, gameId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double avg = rs.getDouble(1);
                PreparedStatement update = conn.prepareStatement(
                        "UPDATE games SET average_rating = ? WHERE id = ?"
                );
                update.setDouble(1, avg);
                update.setInt(2, gameId);
                update.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}