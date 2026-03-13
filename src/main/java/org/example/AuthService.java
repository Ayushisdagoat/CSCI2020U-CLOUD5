package org.example;

import java.util.ArrayList;

/*
 This class handles everything related to user authentication.
 It stores all user accounts in ArrayLists.
 Each index across all three lists represents ONE user.

 *   index 0 = ("admin", "admin123", "admin")
 *   index 1 = ("user",  "user123",  "user")
 */
public class AuthService {

    // --- DATA STORAGE ---
    // Three ArrayLists .
    // The same index in each list belongs to the same user.
    private static ArrayList<String> usernames = new ArrayList<>(); // stores usernames
    private static ArrayList<String> passwords = new ArrayList<>(); // stores passwords
    private static ArrayList<String> roles     = new ArrayList<>(); // stores roles ("admin" or "user")

    /*
     initDB()

     Sets up the default accounts when the app first starts.
     Called once in main() before the window opens.
     Without this, there would be no accounts to log in with.
     */
    public static void initDB() {
        // Add the default admin account
        usernames.add("admin");
        passwords.add("admin123");
        roles.add("admin");

        // Add the default regular user account
        usernames.add("user");
        passwords.add("user123");
        roles.add("user");

        System.out.println("Default users created.");
    }

    /*
     login(username, password)

     Checks if the given username and password match any account.
     Goes through each stored user one by one and checks for a match.

     Returns:
        "admin"   - credentials match an admin account
        "user"    - credentials match a regular user account
        "invalid" - no match found (wrong username or password)
     */
    public String login(String username, String password) {
        // Loop through every stored user
        for (int i = 0; i < usernames.size(); i++) {
            // Check if both username AND password match at the same index
            if (usernames.get(i).equals(username) && passwords.get(i).equals(password)) {
                return roles.get(i); // return "admin" or "user"
            }
        }
        // If no match was found after checking everyone, login failed
        return "invalid";
    }

    /*
     addUser(username, password, role)

     Adds a new user to all three lists at the same index.
     Called by UserService.registerUser() when admin creates a new account.
     */
    public static void addUser(String username, String password, String role) {
        usernames.add(username);
        passwords.add(password);
        roles.add(role);
    }

    /*
     userExists(username)

     Checks if a username is already taken.
     Used before registering a new user to avoid duplicate usernames.

     Returns true if username exists, false if it is available.
     */
    public static boolean userExists(String username) {
        return usernames.contains(username);
    }

    /*
     removeUser(username)

     Deletes a user by finding their index and removing them
     from all three lists at the same time.
     If the user is not found, nothing happens.
     */
    public static void removeUser(String username) {
        int index = usernames.indexOf(username); // find which position the user is at
        if (index != -1) { // -1 means not found
            usernames.remove(index);
            passwords.remove(index);
            roles.remove(index);
        }
    }

    /*
     changePassword(username, newPassword)

     Updates the password for an existing user.
     Finds the user by username and replaces their password.
     */
    public static void changePassword(String username, String newPassword) {
        int index = usernames.indexOf(username);
        if (index != -1) { // only update if the user exists
            passwords.set(index, newPassword);
        }
    }

    /*
     getAllUsers()

     Returns a formatted string listing all users and their roles.
     Used by admins to see who has accounts in the system.

     *   Username: admin | Role: admin
     */
    public static String getAllUsers() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < usernames.size(); i++) {
            sb.append("Username: ").append(usernames.get(i))
                    .append(" | Role: ").append(roles.get(i)).append("\n");
        }
        return sb.toString();
    }
}