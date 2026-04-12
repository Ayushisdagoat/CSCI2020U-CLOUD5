package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewServiceTest {

    private ReviewService reviewService;
    private GameService gameService;
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        // create new objects
        reviewService = new ReviewService();
        gameService = new GameService();
        notificationService = new NotificationService();

        // add a sample game (ID will be 1)
        gameService.addGame("Elden Ring", "RPG", "PC", 79.99, "Open world RPG", "url");
    }

    @Test
    void testSubmitReview() {
        // try to submit a review
        boolean result = reviewService.submitReview(1, "john", 4, "Good game", "Elden Ring");

        // check if it worked
        assertTrue(result);
    }

    @Test
    void testReviewCreatesNotification() {
        // submit a review
        reviewService.submitReview(1, "john", 5, "Amazing game", "Elden Ring");

        // get notifications
        String notifications = notificationService.getUnreadNotifications();

        // check if notification was created
        assertTrue(notifications.contains("john"));
        assertTrue(notifications.contains("Elden Ring"));
    }

    @Test
    void testInvalidReview() {
        // try invalid rating
        boolean result = reviewService.submitReview(1, "john", -1, "Bad input", "Elden Ring");

        // should fail
        assertFalse(result);
    }
}