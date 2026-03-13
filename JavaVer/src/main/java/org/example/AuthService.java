package org.example;

import java.util.ArrayList;

public class AuthService {

    private static ArrayList<String> usernames = new ArrayList<>();
    private static ArrayList<String> passwords = new ArrayList<>();
    private static ArrayList<String> roles = new ArrayList<>();

    public static void initDB() {
        usernames.add("admin");
        passwords.add("admin123");
        roles.add("admin");

        usernames.add("user");
        passwords.add("user123");
        roles.add("user");

        System.out.println("Default users created.");
    }

    public String login(String username, String password) {
        for (int i = 0; i < usernames.size(); i++) {
            if (usernames.get(i).equals(username) && passwords.get(i).equals(password)) {
                return roles.get(i);
            }
        }
        return "invalid";
    }

    public static void addUser(String username, String password, String role) {
        usernames.add(username);
        passwords.add(password);
        roles.add(role);
    }

    public static boolean userExists(String username) {
        return usernames.contains(username);
    }

    public static void removeUser(String username) {
        int index = usernames.indexOf(username);
        if (index != -1) {
            usernames.remove(index);
            passwords.remove(index);
            roles.remove(index);
        }
    }

    public static void changePassword(String username, String newPassword) {
        int index = usernames.indexOf(username);
        if (index != -1) {
            passwords.set(index, newPassword);
        }
    }

    public static String getAllUsers() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < usernames.size(); i++) {
            sb.append("Username: ").append(usernames.get(i))
                    .append(" | Role: ").append(roles.get(i)).append("\n");
        }
        return sb.toString();
    }
}