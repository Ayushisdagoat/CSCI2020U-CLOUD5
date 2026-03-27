package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SessionServiceTest {

    @Test
    void testStartSession() {
        SessionService.startSession("adminUser", "admin");

        assertTrue(SessionService.isLoggedIn());
        assertEquals("adminUser", SessionService.getCurrentUsername());
        assertEquals("admin", SessionService.getCurrentRole());

        SessionService.endSession(); // cleanup
    }

    @Test
    void testEndSession() {
        SessionService.startSession("john", "user");

        SessionService.endSession();

        assertFalse(SessionService.isLoggedIn());
        assertNull(SessionService.getCurrentUsername());
        assertNull(SessionService.getCurrentRole());
    }

    @Test
    void testIsLoggedInWhenEmpty() {
        SessionService.endSession(); // make sure logged out
        assertFalse(SessionService.isLoggedIn());
    }

    @Test
    void testAdminRole() {
        SessionService.startSession("adminUser", "admin");

        assertTrue(SessionService.isAdmin());
        assertFalse(SessionService.isUser());

        SessionService.endSession();
    }

    @Test
    void testUserRole() {
        SessionService.startSession("user1", "user");

        assertTrue(SessionService.isUser());
        assertFalse(SessionService.isAdmin());

        SessionService.endSession();
    }

    @Test
    void testValuesWhenLoggedOut() {
        SessionService.endSession();

        assertNull(SessionService.getCurrentUsername());
        assertNull(SessionService.getCurrentRole());
    }
}