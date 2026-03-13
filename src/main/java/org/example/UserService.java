package org.example;

/*
 UserService.java

 This class handles everything related to managing user accounts.
 Only admins can register new users, remove users, or change passwords.

 It uses AuthService to do the actual storing, since AuthService
 already holds all the user data in its ArrayLists.
 UserService just adds validation logic on top.
 */
public class UserService {

    /*
     registerUser(username, password, role)

     ADMIN ONLY — Creates a new user account.
     Validates the input before passing it to AuthService to store.

     Rules:
       - Role must be "admin" or "user" (nothing else allowed)
       - Username and password cannot be blank
       - Username must not already be taken

     Returns true if the account was created successfully.
     Returns false if any validation check fails.
     */
    public boolean registerUser(String username, String password, String role) {
        // Check that the role is valid — must be exactly "admin" or "user"
        if (!role.equals("admin") && !role.equals("user")) {
            System.out.println("Invalid role. Must be 'admin' or 'user'.");
            return false;
        }

        // Check that username and password are not empty
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return false;
        }

        // Check that the username is not already taken
        if (AuthService.userExists(username)) {
            System.out.println("Username already taken.");
            return false;
        }

        // All checks passed — add the new user to AuthService
        AuthService.addUser(username, password, role);
        return true;
    }

    /*
     removeUser(username)

     ADMIN ONLY — Deletes a user account by username.
     Checks that the user exists before trying to remove them.

     Returns true if the user was found and removed.
     Returns false if no user with that username exists.
     */
    public boolean removeUser(String username) {
        // Make sure the user actually exists before trying to remove
        if (!AuthService.userExists(username)) {
            System.out.println("User not found.");
            return false;
        }

        AuthService.removeUser(username); // delegate the actual removal to AuthService
        return true;
    }

    /*
     getAllUsers()

     ADMIN ONLY — Returns a formatted string listing all user accounts.
     Delegates to AuthService which holds the actual user data.

     Example output:
        Username: admin | Role: admin
       Username: john  | Role: user
     */
    public String getAllUsers() {
        return AuthService.getAllUsers();
    }

    /*
     userExists(username)

     Checks if a username is already registered in the system.
     Delegates to AuthService which holds the actual user data.

     Returns true if the username exists, false if it does not.
     */
    public boolean userExists(String username) {
        return AuthService.userExists(username);
    }

    /*
     changePassword(username, newPassword)

     Updates the password for an existing user account.
     Validates that the new password is not blank before updating.

     Returns true if the password was changed successfully.
     Returns false if the new password is empty.
     */
    public boolean changePassword(String username, String newPassword) {
        // Password cannot be blank
        if (newPassword.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return false;
        }

        AuthService.changePassword(username, newPassword); // delegate to AuthService
        return true;
    }
}