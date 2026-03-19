package org.example;

import java.util.ArrayList;
/*
 ReviewService.java

 This class handles everything related to game reviews.
 Users can submit reviews, but they stay hidden until an admin approves them.
 Admins can approve or reject any pending review.

 How the review flow works:
   1. User submits a review → saved with approved = false (pending)
        A notification is automatically sent to alert the admin
   2. Admin sees the notification and opens the pending reviews list
   3. Admin approves = review becomes publicly visible, game rating updates
        OR
      Admin rejects = review is deleted entirely
 */
public class ReviewService {

    private static ArrayList<Integer> reviewIds = new ArrayList<>(); // unique ID for each review
    private static ArrayList<Integer> gameIds   = new ArrayList<>(); // which game this review is for
    private static ArrayList<String>  usernames = new ArrayList<>(); // who wrote the review
    private static ArrayList<Integer> ratings   = new ArrayList<>(); // star rating (1 to 5)
    private static ArrayList<String>  comments  = new ArrayList<>(); // written comment
    private static ArrayList<Boolean> approved  = new ArrayList<>(); // true = public, false = pending

    private static int nextReviewId = 1;

    private static GameService gameService = new GameService();


    public static void initDB() {
        System.out.println("ReviewService ready.");
    }

    public boolean submitReview(int gameId, String username, int rating, String comment, String gameTitle) {
        if (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5.");
            return false;
        }

        for (int i = 0; i < reviewIds.size(); i++) {
            if (gameIds.get(i) == gameId && usernames.get(i).equals(username)) {
                System.out.println("User already reviewed this game.");
                return false;
            }
        }

        reviewIds.add(nextReviewId++);
        gameIds.add(gameId);
        usernames.add(username);
        ratings.add(rating);
        comments.add(comment);
        approved.add(false);

        NotificationService.addNotification(
                "New review pending for: " + gameTitle + " by " + username
        );

        return true;
    }


    public String getPendingReviews() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < reviewIds.size(); i++) {
            if (!approved.get(i)) {
                sb.append("Review ID: ").append(reviewIds.get(i))
                        .append(" | Game ID: ").append(gameIds.get(i))
                        .append(" | User: ").append(usernames.get(i))
                        .append(" | Rating: ").append(ratings.get(i))
                        .append(" | Comment: ").append(comments.get(i))
                        .append("\n");
            }
        }
        return sb.isEmpty() ? "No pending reviews." : sb.toString();
    }


    public boolean approveReview(int reviewId) {
        int index = reviewIds.indexOf(reviewId);
        if (index == -1) return false;

        approved.set(index, true);
        updateAverageRating(gameIds.get(index));
        return true;
    }


    public boolean rejectReview(int reviewId) {
        int index = reviewIds.indexOf(reviewId);
        if (index == -1) return false;

        reviewIds.remove(index);
        gameIds.remove(index);
        usernames.remove(index);
        ratings.remove(index);
        comments.remove(index);
        approved.remove(index);
        return true;
    }


    public String getApprovedReviews(int gameId) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < reviewIds.size(); i++) {
            if (gameIds.get(i) == gameId && approved.get(i)) {
                sb.append("User: ").append(usernames.get(i))
                        .append(" | Rating: ").append(ratings.get(i))
                        .append(" | Comment: ").append(comments.get(i))
                        .append("\n");
            }
        }
        return sb.isEmpty() ? "No reviews yet." : sb.toString();
    }


    private void updateAverageRating(int gameId) {
        double total = 0;
        int count = 0;

        for (int i = 0; i < reviewIds.size(); i++) {
            if (gameIds.get(i) == gameId && approved.get(i)) {
                total += ratings.get(i);
                count++;
            }
        }

        if (count > 0) {
            gameService.updateAverageRating(gameId, total / count);
        }
    }
}