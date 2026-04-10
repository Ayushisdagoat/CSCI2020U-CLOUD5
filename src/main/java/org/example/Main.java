package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Main {

    static final int PORT = 6789;

    static AuthService authService             = new AuthService();
    static UserService userService             = new UserService();
    static GameService gameService             = new GameService();
    static GenreService genreService           = new GenreService();
    static ReviewService reviewService         = new ReviewService();
    static NotificationService notifService    = new NotificationService();
    static WishlistService wishlistService     = new WishlistService();

    public static void main(String[] args) throws Exception {

        AuthService.initDB();
        GameService.initDB();
        ReviewService.initDB();
        WishlistService.initDB();
        GenreService.initDB();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/", (HttpExchange ex) -> {
            if (!ex.getRequestURI().getPath().equals("/")) { send404(ex); return; }
            File htmlFile = new File("src/main/resources/index.html");
            byte[] htmlBytes = java.nio.file.Files.readAllBytes(htmlFile.toPath());
            sendResponse(ex, 200, "text/html; charset=utf-8", htmlBytes);
        });

        server.createContext("/api/auth/login", (HttpExchange ex) -> {
            Map<String, String> body = readBody(ex);
            String role = authService.login(body.get("username"), body.get("password"));
            sendJson(ex, 200, "{\"role\":\"" + role + "\"}");
        });

        server.createContext("/api/session/start", (HttpExchange ex) -> {
            Map<String, String> body = readBody(ex);
            SessionService.startSession(body.get("username"), body.get("role"));
            sendJson(ex, 200, "{\"ok\":true}");
        });

        server.createContext("/api/session/end", (HttpExchange ex) -> {
            SessionService.endSession();
            sendJson(ex, 200, "{\"ok\":true}");
        });

        server.createContext("/api/session", (HttpExchange ex) -> {
            String json = "{"
                    + "\"loggedIn\":"  + SessionService.isLoggedIn()
                    + ",\"username\":" + toJsonString(SessionService.getCurrentUsername())
                    + ",\"role\":"     + toJsonString(SessionService.getCurrentRole())
                    + ",\"isAdmin\":"  + SessionService.isAdmin()
                    + "}";
            sendJson(ex, 200, json);
        });

        server.createContext("/api/users", (HttpExchange ex) -> {
            String method = ex.getRequestMethod();

            if (method.equals("GET")) {
                sendJson(ex, 200, "{\"users\":" + toJsonString(userService.getAllUsers()) + "}");

            } else if (method.equals("POST")) {
                Map<String, String> body = readBody(ex);
                boolean ok = userService.registerUser(body.get("username"), body.get("password"), body.get("role"));
                sendJson(ex, 200, "{\"ok\":" + ok + "}");

            } else if (method.equals("DELETE")) {
                Map<String, String> body = readBody(ex);
                boolean ok = userService.removeUser(body.get("username"));
                sendJson(ex, 200, "{\"ok\":" + ok + "}");
            }
        });

        server.createContext("/api/users/password", (HttpExchange ex) -> {
            Map<String, String> body = readBody(ex);
            boolean ok = userService.changePassword(body.get("username"), body.get("newPassword"));
            sendJson(ex, 200, "{\"ok\":" + ok + "}");
        });

        server.createContext("/api/games", (HttpExchange ex) -> {
            String method = ex.getRequestMethod();

            if (method.equals("GET")) {
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < gameService.getSize(); i++) {
                    if (i > 0) sb.append(",");
                    sb.append("{")
                            .append("\"id\":").append(gameService.getId(i)).append(",")
                            .append("\"title\":").append(toJsonString(gameService.getTitle(i))).append(",")
                            .append("\"genre\":").append(toJsonString(gameService.getGenre(i))).append(",")
                            .append("\"platform\":").append(toJsonString(gameService.getPlatform(i))).append(",")
                            .append("\"price\":").append(gameService.getPrice(i)).append(",")
                            .append("\"description\":").append(toJsonString(gameService.getDescription(i))).append(",")
                            .append("\"trailerUrl\":").append(toJsonString(gameService.getTrailerUrl(i))).append(",")
                            .append("\"averageRating\":").append(gameService.getAverageRating(i))
                            .append("}");
                }
                sb.append("]");
                sendJson(ex, 200, sb.toString());

            } else if (method.equals("POST")) {
                Map<String, String> body = readBody(ex);
                boolean ok = gameService.addGame(
                        body.get("title"),
                        body.get("genre"),
                        body.get("platform"),
                        toDouble(body.get("price")),
                        body.get("description"),
                        body.get("trailerUrl")
                );
                sendJson(ex, 200, "{\"ok\":" + ok + "}");
            }
        });

        server.createContext("/api/games/", (HttpExchange ex) -> {
            String path = ex.getRequestURI().getPath();
            String[] parts = path.split("/");

            if (parts[3].equals("search")) {
                Map<String, String> query = readQuery(ex.getRequestURI().getQuery());
                String result = gameService.searchGames(query.get("title"), query.get("platform"), query.get("genre"), toDouble(query.get("minRating")));
                sendJson(ex, 200, "{\"result\":" + toJsonString(result) + "}");
                return;
            }

            int id = toInt(parts[3]);
            String method = ex.getRequestMethod();

            if (method.equals("PUT")) {
                Map<String, String> body = readBody(ex);
                boolean ok = gameService.editGame(id, body.get("title"), body.get("genre"), body.get("platform"), toDouble(body.get("price")), body.get("description"), body.get("trailerUrl"));
                sendJson(ex, 200, "{\"ok\":" + ok + "}");

            } else if (method.equals("DELETE")) {
                boolean ok = gameService.removeGame(id);
                sendJson(ex, 200, "{\"ok\":" + ok + "}");
            }
        });

        server.createContext("/api/genres", (HttpExchange ex) -> {
            String method = ex.getRequestMethod();

            if (method.equals("GET")) {
                java.util.ArrayList<String> list = genreService.getGenreList();
                StringBuilder sb = new StringBuilder("{\"genres\":[");
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) sb.append(",");
                    sb.append(toJsonString(list.get(i)));
                }
                sb.append("],\"remainingSlots\":").append(genreService.getRemainingSlots()).append("}");
                sendJson(ex, 200, sb.toString());

            } else if (method.equals("POST")) {
                Map<String, String> body = readBody(ex);
                boolean ok = genreService.addGenre(body.get("name"));
                sendJson(ex, 200, "{\"ok\":" + ok + "}");

            } else if (method.equals("DELETE")) {
                Map<String, String> body = readBody(ex);
                boolean ok = genreService.removeGenre(body.get("name"));
                sendJson(ex, 200, "{\"ok\":" + ok + "}");
            }
        });

        server.createContext("/api/reviews/pending", (HttpExchange ex) -> {
            sendJson(ex, 200, "{\"result\":" + toJsonString(reviewService.getPendingReviews()) + "}");
        });

        server.createContext("/api/reviews/approve", (HttpExchange ex) -> {
            Map<String, String> body = readBody(ex);
            boolean ok = reviewService.approveReview(toInt(body.get("reviewId")));
            sendJson(ex, 200, "{\"ok\":" + ok + "}");
        });

        server.createContext("/api/reviews/reject", (HttpExchange ex) -> {
            Map<String, String> body = readBody(ex);
            boolean ok = reviewService.rejectReview(toInt(body.get("reviewId")));
            sendJson(ex, 200, "{\"ok\":" + ok + "}");
        });

        server.createContext("/api/reviews", (HttpExchange ex) -> {
            String method = ex.getRequestMethod();

            if (method.equals("POST")) {
                Map<String, String> body = readBody(ex);
                boolean ok = reviewService.submitReview(toInt(body.get("gameId")), body.get("username"), toInt(body.get("rating")), body.get("comment"), body.get("gameTitle"));
                sendJson(ex, 200, "{\"ok\":" + ok + "}");

            } else if (method.equals("GET")) {
                Map<String, String> query = readQuery(ex.getRequestURI().getQuery());
                String result = reviewService.getApprovedReviews(toInt(query.get("gameId")));
                sendJson(ex, 200, "{\"result\":" + toJsonString(result) + "}");
            }
        });

        server.createContext("/api/notifications", (HttpExchange ex) -> {
            String json = "{"
                    + "\"all\":"     + toJsonString(notifService.getAllNotifications())
                    + ",\"unread\":" + toJsonString(notifService.getUnreadNotifications())
                    + ",\"count\":"  + notifService.getUnreadCount()
                    + "}";
            sendJson(ex, 200, json);
        });

        server.createContext("/api/notifications/markAllRead", (HttpExchange ex) -> {
            notifService.markAllAsRead();
            sendJson(ex, 200, "{\"ok\":true}");
        });

        server.createContext("/api/notifications/markRead", (HttpExchange ex) -> {
            Map<String, String> body = readBody(ex);
            boolean ok = notifService.markAsRead(toInt(body.get("notificationId")));
            sendJson(ex, 200, "{\"ok\":" + ok + "}");
        });

        server.createContext("/api/wishlist", (HttpExchange ex) -> {
            String method = ex.getRequestMethod();

            if (method.equals("GET")) {
                Map<String, String> query = readQuery(ex.getRequestURI().getQuery());
                String result = wishlistService.getWishlist(query.get("username"));
                sendJson(ex, 200, "{\"result\":" + toJsonString(result) + "}");

            } else if (method.equals("POST")) {
                Map<String, String> body = readBody(ex);
                boolean ok = wishlistService.addToWishlist(body.get("username"), toInt(body.get("gameId")), body.get("gameTitle"));
                sendJson(ex, 200, "{\"ok\":" + ok + "}");

            } else if (method.equals("DELETE")) {
                Map<String, String> body = readBody(ex);
                boolean ok = wishlistService.removeFromWishlist(body.get("username"), toInt(body.get("gameId")));
                sendJson(ex, 200, "{\"ok\":" + ok + "}");
            }
        });

        server.createContext("/api/wishlist/check", (HttpExchange ex) -> {
            Map<String, String> query = readQuery(ex.getRequestURI().getQuery());
            boolean inWishlist = wishlistService.isInWishlist(query.get("username"), toInt(query.get("gameId")));
            sendJson(ex, 200, "{\"inWishlist\":" + inWishlist + "}");
        });

        server.createContext("/api/wishlist/clear", (HttpExchange ex) -> {
            Map<String, String> body = readBody(ex);
            wishlistService.clearWishlist(body.get("username"));
            sendJson(ex, 200, "{\"ok\":true}");
        });

        server.start();
        System.out.println("Server running at http://localhost:" + PORT);
        openBrowser("http://localhost:" + PORT);
    }

    static void sendResponse(HttpExchange ex, int statusCode, String contentType, byte[] body) throws IOException {
        ex.getResponseHeaders().set("Content-Type", contentType);
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.sendResponseHeaders(statusCode, body.length);
        OutputStream os = ex.getResponseBody();
        os.write(body);
        os.close();
    }

    static void sendJson(HttpExchange ex, int statusCode, String json) throws IOException {
        sendResponse(ex, statusCode, "application/json; charset=utf-8", json.getBytes(StandardCharsets.UTF_8));
    }

    static void send404(HttpExchange ex) throws IOException {
        sendJson(ex, 404, "{\"error\":\"Not found\"}");
    }

    static Map<String, String> readBody(HttpExchange ex) throws IOException {
        byte[] bytes = ex.getRequestBody().readAllBytes();
        String bodyText = new String(bytes, StandardCharsets.UTF_8);
        return readQuery(bodyText);
    }

    static Map<String, String> readQuery(String queryString) {
        Map<String, String> map = new HashMap<>();
        if (queryString == null || queryString.isEmpty()) return map;
        for (String pair : queryString.split("&")) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                try {
                    String key   = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                    map.put(key, value);
                } catch (Exception e) {}
            }
        }
        return map;
    }

    static String toJsonString(String value) {
        if (value == null) return "null";
        return "\"" + value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                + "\"";
    }

    static double toDouble(String value) {
        if (value == null) return 0.0;
        try { return Double.parseDouble(value); }
        catch (NumberFormatException e) { return 0.0; }
    }

    static int toInt(String value) {
        if (value == null) return 0;
        try { return Integer.parseInt(value); }
        catch (NumberFormatException e) { return 0; }
    }

    static void openBrowser(String url) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
            } else {
                Runtime.getRuntime().exec(new String[]{"xdg-open", url});
            }
        } catch (IOException e) {
            System.out.println(url);
        }
    }
}