package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() throws Exception {
        authService = new AuthService();
        resetAuthServiceState();
        AuthService.initDB();
    }

    @SuppressWarnings("unchecked")
    private void resetAuthServiceState() throws Exception {
        String[] fields = { "usernames", "passwords", "roles" };

        for (String fieldName : fields) {
            Field field = AuthService.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            ((ArrayList<?>) field.get(null)).clear();
        }
    }

    @Test
    void testValidUserLogin() {
        String result = authService.login("user", "user123");
        assertEquals("user", result);
    }

    @Test
    void testValidAdminLogin() {
        String result = authService.login("admin", "admin123");
        assertEquals("admin", result);
    }

    @Test
    void testInvalidLoginWrongPassword() {
        String result = authService.login("user", "wrongpass");
        assertEquals("invalid", result);
    }

    @Test
    void testUserExists() {
        assertTrue(AuthService.userExists("admin"));
    }
}