package org.example;

import java.sql.*;

public class AuthService {

    private static final String DB_URL = "jdbc:sqlite:game_catalogue.db";

    public static void initDB() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL CHECK(role IN ('admin', 'user'))
                )
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO users (username, password, role)
                VALUES ('admin', 'admin123', 'admin')
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO users (username, password, role)
                VALUES ('user', 'user123', 'user')
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String login(String username, String password) {
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "invalid";
    }
}