package org.example;

/*
 SessionService.java

 This class tracks who is currently logged into the app.
 It acts like a security guard — before any sensitive action is performed,
 the system checks with SessionService to confirm the user is logged in
 and has the right role (admin or user).

 How it works:
   1. User logs in → SessionService.startSession() is called
   2. User performs actions → SessionService.isLoggedIn() / isAdmin() checks access
   3. User logs out → SessionService.endSession() is called

 Only one user can be logged in at a time
 */
public class SessionService {

    // Stores the currently logged-in user's information.
    // These are null when no one is logged in.
    private static String currentUsername = null;
    private static String currentRole = null;


    public static void startSession(String username, String role) {
        currentUsername = username;
        currentRole     = role;
        System.out.println("Session started for: " + username + " (" + role + ")");
    }

    public static void endSession() {
        System.out.println("👋 Session ended for: " + currentUsername);
        currentUsername = null;
        currentRole     = null;
    }


    public static boolean isLoggedIn() {
        return currentUsername != null;
    }


    public static boolean isAdmin() {
        return isLoggedIn() && currentRole.equals("admin");
    }

    public static boolean isUser() {
        return isLoggedIn() && currentRole.equals("user");
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }


    public static String getCurrentRole() {
        return currentRole;
    }
}