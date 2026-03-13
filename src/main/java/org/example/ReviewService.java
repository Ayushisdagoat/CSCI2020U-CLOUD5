package org.example;

import java.util.ArrayList;

/*
 ReviewService.java

 This class handles everything related to game reviews.
 Users can submit reviews, but they are hidden until an admin approves them.
 Admins can approve or reject any pending review.

 How the review flow works:
   1. User submits a review → saved with approved = false (pending)
   2. Admin sees the pending review in a list
   3. Admin approves it → approved = true, review is now visible publicly
       OR
      Admin rejects it → review is deleted entirely

 * When a review is approved, the game's average rating automatically updates.
 */
public class ReviewService {

    // --- DATA STORAGE ---
    // Each ArrayList stores one "column" of review data.
    // The same index across all lists = the same review.
    private static ArrayList<Integer> reviewIds = new ArrayList<>(); // unique ID for each review
    private static ArrayList<Integer> gameIds   = new ArrayList<>(); // which game this review is for
    private static ArrayList<String>  usernames = new ArrayList<>(); // who wrote the review
    private static ArrayList<Integer> ratings   = new ArrayList<>(); // star rating (1 to 5)
    private static ArrayList<String>  comments  = new ArrayList<>(); // written comment
    private static ArrayList<Boolean> approved  = new ArrayList<>(); // true = public, false = pending

    // Tracks the next available review ID. Starts at 1 and increases with each new review.
    private static int nextReviewId = 1;

    // Used to update a game's average rating after a review is approved
    private static GameService gameService = new GameService();

    /*
     initDB()

     Called once at startup to confirm ReviewService is ready.
     No setup needed since ArrayLists start empty.
     */
    public static void initDB() {
        System.out.println("ReviewService ready.");
    }

    /*
     submitReview(gameId, username, rating, comment)

     USER — Submits a new review for a game.
     The review starts as pending (not visible to other users yet).
     An admin must approve it before it appears publicly.

     Rules:
       - Rating must be between 1 and 5
       - A user can only review the same game once

     Returns true if the review was submitted successfully.
     Returns false if the rating is invalid or user already reviewed this game.
     */
    public boolean submitReview(int gameId, String username, int rating, String comment) {
        // Validate the rating is in the allowed range
        if (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5.");
            return false;
        }

        // Check if this user has already reviewed this game
        for (int i = 0; i < reviewIds.size(); i++) {
            if (gameIds.get(i) == gameId && usernames.get(i).equals(username)) {
                System.out.println("User already reviewed this game.");
                return false;
            }
        }

        // Save the new review as pending (approved = false)
        reviewIds.add(nextReviewId++);
        gameIds.add(gameId);
        usernames.add(username);
        ratings.add(rating);
        comments.add(comment);
        approved.add(false); // starts as pending — not visible yet
        return true;
    }

    /*
     getPendingReviews()

     ADMIN ONLY — Returns a formatted string of all reviews waiting for approval.
     Only shows reviews where approved = false.

     Example output:
       Review ID: 1 | Game ID: 3 | User: john | Rating: 5 | Comment: Amazing!
     */
    public String getPendingReviews() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < reviewIds.size(); i++) {
            if (!approved.get(i)) { // only include unapproved reviews
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

    /*
     approveReview(reviewId)

     ADMIN ONLY — Approves a review by its ID.
     Sets approved = true so the review becomes publicly visible.
     Automatically recalculates the game's average star rating.

     Returns true if the review was found and approved.
      Returns false if no review with that ID exists.
     */
    public boolean approveReview(int reviewId) {
        int index = reviewIds.indexOf(reviewId); // find the review's position
        if (index == -1) return false; // review not found

        approved.set(index, true); // mark as approved — now publicly visible
        updateAverageRating(gameIds.get(index)); // recalculate the game's star rating
        return true;
    }

    /*
     rejectReview(reviewId)

     ADMIN ONLY — Rejects and permanently deletes a review by its ID.
     Removes the review from every ArrayList at the same index.

     Returns true if the review was found and deleted.
     Returns false if no review with that ID exists.
     */
    public boolean rejectReview(int reviewId) {
        int index = reviewIds.indexOf(reviewId); // find the review's position
        if (index == -1) return false; // review not found

        // Remove this review from every list at the same index
        reviewIds.remove(index);
        gameIds.remove(index);
        usernames.remove(index);
        ratings.remove(index);
        comments.remove(index);
        approved.remove(index);
        return true;
    }

    /*
     getApprovedReviews(gameId)

     Returns a formatted string of all approved reviews for a specific game.
     Only shows reviews where approved = true for that game.
     Used on the game detail page to display public reviews.

     Example output:
       User: john | Rating: 5 | Comment: Amazing game!

     */
    public String getApprovedReviews(int gameId) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < reviewIds.size(); i++) {
            // Only include approved reviews that belong to this specific game
            if (gameIds.get(i) == gameId && approved.get(i)) {
                sb.append("User: ").append(usernames.get(i))
                        .append(" | Rating: ").append(ratings.get(i))
                        .append(" | Comment: ").append(comments.get(i))
                        .append("\n");
            }
        }
        return sb.isEmpty() ? "No reviews yet." : sb.toString();
    }

    /*
     updateAverageRating(gameId)

     PRIVATE HELPER — Automatically called after a review is approved.
     Calculates the average of all approved ratings for a game
     and updates the game's rating in GameService.

     Example: if a game has approved ratings of 5, 4, 3 → average = 4.0
     */
    private void updateAverageRating(int gameId) {
        double total = 0;  // sum of all approved ratings
        int count = 0;     // number of approved ratings

        // Add up all approved ratings for this game
        for (int i = 0; i < reviewIds.size(); i++) {
            if (gameIds.get(i) == gameId && approved.get(i)) {
                total += ratings.get(i);
                count++;
            }
        }

        // Only update if there is at least one approved review
        if (count > 0) {
            gameService.updateAverageRating(gameId, total / count); // save the average
        }
    }
}