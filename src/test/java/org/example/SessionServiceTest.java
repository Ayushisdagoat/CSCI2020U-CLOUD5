package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessionServiceTest {

    @BeforeEach
    void setUp() {
        SessionService.endSession();
    }

    @Test
    void startSession_shouldSetUsernameAndRoleAndLoggedInState() {
        SessionService.startSession("adminUser", "admin");

        assertTrue(SessionService.isLoggedIn());
        assertEquals("adminUser", SessionService.getCurrentUsername());
        assertEquals("admin", SessionService.getCurrentRole());
    }

    @Test
    void endSession_shouldClearSessionState() {
        SessionService.startSession("john", "user");

        SessionService.endSession();

        assertFalse(SessionService.isLoggedIn());
        assertNull(SessionService.getCurrentUsername());
        assertNull(SessionService.getCurrentRole());
    }

    @Test
    void isLoggedIn_shouldReturnFalseWhenNoSessionExists() {
        assertFalse(SessionService.isLoggedIn());
    }

    @Test
    void isAdmin_shouldReturnTrueOnlyForAdminRole() {
        SessionService.startSession("adminUser", "admin");

        assertTrue(SessionService.isAdmin());
        assertFalse(SessionService.isUser());
    }

    @Test
    void isUser_shouldReturnTrueOnlyForUserRole() {
        SessionService.startSession("normalUser", "user");

        assertTrue(SessionService.isUser());
        assertFalse(SessionService.isAdmin());
    }

    @Test
    void isAdmin_shouldReturnFalseWhenLoggedOut() {
        assertFalse(SessionService.isAdmin());
    }

    @Test
    void isUser_shouldReturnFalseWhenLoggedOut() {
        assertFalse(SessionService.isUser());
    }

    @Test
    void getCurrentUsername_shouldReturnNullWhenLoggedOut() {
        assertNull(SessionService.getCurrentUsername());
    }

    @Test
    void getCurrentRole_shouldReturnNullWhenLoggedOut() {
        assertNull(SessionService.getCurrentRole());
    }
}