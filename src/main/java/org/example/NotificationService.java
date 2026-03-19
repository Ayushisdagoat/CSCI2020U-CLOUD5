package org.example;

import java.util.ArrayList;

/*
 NotificationService.java

 This class handles notifications for the admin.
 When a user submits a review, a notification is automatically created
 to alert the admin that there is a review waiting for approval.

 How it works:
   1. User submits a review = ReviewService calls NotificationService.addNotification()
   2. Admin logs in = sees a count of unread notifications
   3. Admin views notifications = sees which games have pending reviews
   4. Admin reads notifications = they are marked as read
 */
public class NotificationService {

    // Each index across all lists represents ONE notification.
    private static ArrayList<Integer> notificationIds = new ArrayList<>();
    private static ArrayList<String>  messages = new ArrayList<>();
    private static ArrayList<Boolean> isRead = new ArrayList<>();
    private static int nextNotificationId = 1;


    public static void addNotification(String message) {
        notificationIds.add(nextNotificationId++);
        messages.add(message);
        isRead.add(false);
    }


    public String getUnreadNotifications() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < notificationIds.size(); i++) {
            if (!isRead.get(i)) {
                sb.append("[").append(notificationIds.get(i)).append("] ")
                        .append(messages.get(i)).append("\n");
            }
        }
        return sb.isEmpty() ? "No new notifications." : sb.toString();
    }


    public String getAllNotifications() {
        if (notificationIds.isEmpty()) return "No notifications.";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < notificationIds.size(); i++) {
            String status = isRead.get(i) ? "[READ]  " : "[UNREAD]";
            sb.append("[").append(notificationIds.get(i)).append("] ")
                    .append(status).append(" ")
                    .append(messages.get(i)).append("\n");
        }
        return sb.toString();
    }


    public boolean markAsRead(int notificationId) {
        int index = notificationIds.indexOf(notificationId);
        if (index == -1) return false;

        isRead.set(index, true);
        return true;
    }


    public void markAllAsRead() {
        for (int i = 0; i < isRead.size(); i++) {
            isRead.set(i, true);
        }
    }


    public int getUnreadCount() {
        int count = 0;
        for (boolean read : isRead) {
            if (!read) count++;
        }
        return count;
    }


    public void clearAll() {
        notificationIds.clear();
        messages.clear();
        isRead.clear();
    }
}