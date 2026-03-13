package org.example;

import java.sql.*;

public class UserService {

    private static final String DB_URL = "jdbc:sqlite:game_catalogue.db";

    public boolean registerUser(String username, String password, String role) {

        if (!role.equals("admin") && !role.equals("user")) {
            System.out.println("Invalid role. Must be 'admin' or 'user'.");
            return false;
        }

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return false;
        }

        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Username already taken.");
            return false;
        }
    }

    public boolean removeUser(String username) {
        String query = "DELETE FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getAllUsers() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery("SELECT id, username, role FROM users ORDER BY role ASC");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean userExists(String username) {
        String query = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changePassword(String username, String newPassword) {
        if (newPassword.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return false;
        }

        String query = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}