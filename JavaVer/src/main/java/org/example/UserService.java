package org.example;

public class UserService {

    public boolean registerUser(String username, String password, String role) {
        if (!role.equals("admin") && !role.equals("user")) {
            System.out.println("Invalid role. Must be 'admin' or 'user'.");
            return false;
        }
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return false;
        }
        if (AuthService.userExists(username)) {
            System.out.println("Username already taken.");
            return false;
        }
        AuthService.addUser(username, password, role);
        return true;
    }

    // ADMIN: Remove a user
    public boolean removeUser(String username) {
        if (!AuthService.userExists(username)) {
            System.out.println("User not found.");
            return false;
        }
        AuthService.removeUser(username);
        return true;
    }

    // ADMIN: Get all users
    public String getAllUsers() {
        return AuthService.getAllUsers();
    }

    // Check if user exists
    public boolean userExists(String username) {
        return AuthService.userExists(username);
    }

    // Change password
    public boolean changePassword(String username, String newPassword) {
        if (newPassword.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return false;
        }
        AuthService.changePassword(username, newPassword);
        return true;
    }
}