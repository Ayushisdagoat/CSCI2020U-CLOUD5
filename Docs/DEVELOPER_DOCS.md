# CLOUD5 Developer Documentation

## Project Overview

CLOUD5 is a Java + Maven project with:

- A Swing desktop UI (`FrontEnd`)
- In-memory service layer (`ArrayList`-backed)
- Optional simple HTTP launcher (`Main`) for serving `index.html`
- Unit tests using JUnit 5

## Tech Stack

- Java 23
- Maven
- JUnit 5
- `org.json` for JSON parsing
- Steam public app details API for game metadata in UI

## Project Structure

- `src/main/java/org/example/FrontEnd.java` - main Swing application
- `src/main/java/org/example/Main.java` - lightweight HTTP server at port `6789`
- `src/main/java/org/example/AuthService.java` - authentication/user store
- `src/main/java/org/example/UserService.java` - user management validations/wrapper
- `src/main/java/org/example/GameService.java` - game catalog CRUD + search/rating fields
- `src/main/java/org/example/ReviewService.java` - review workflow + rating updates
- `src/main/java/org/example/WishlistService.java` - per-user cart/wishlist
- `src/main/java/org/example/GenreService.java` - bounded genre list management
- `src/main/java/org/example/NotificationService.java` - in-memory notifications
- `src/main/resources/index.html` - static page used by `Main`
- `src/test/java/org/example/*` - unit tests

## Run and Build

### Build + test

```bash
mvn clean install
```

### Run Swing app

```bash
mvn exec:java -Dexec.mainClass="org.example.FrontEnd"
```

### Run HTTP server app

```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

### Windows helper script

`build.bat` performs build/test and launches `FrontEnd`.

## Service Design Notes

- Services are primarily static, in-memory stores.
- Data is shared across all instances because storage fields are static.
- `initDB()` methods seed/initialize runtime defaults.
- No persistence layer is wired into core service methods in this version.

## Key Flows

### Authentication

1. `FrontEnd` login invokes `AuthService.login(username, password)`.
2. Returns role string: `admin`, `user`, or `invalid`.

### Catalog Rendering

1. `FrontEnd` iterates hardcoded Steam app IDs.
2. Fetches metadata from Steam API.
3. Builds card UI and detail pages dynamically.

### Wishlist

1. User clicks `Add to Cart` in details view.
2. `WishlistService.addToWishlist(...)` de-duplicates by `(username, gameId)`.
3. Cart dialog uses `WishlistService.getWishlist(currentUser)`.

### Reviews and Ratings

1. `ReviewService.submitReview(...)` stores pending review.
2. `NotificationService.addNotification(...)` is triggered.
3. On approval, `ReviewService` recomputes average rating and updates `GameService`.

## Testing

Current tests:

- `GameServiceSearchTest`
- `SessionServiceTest`
- `WishlistServiceTest`

Because services use static lists, tests reset static state via reflection in `@BeforeEach`.

## Known Gaps / Risks

- No database persistence in active service methods
- No thread-safety for static mutable lists
- Validation and error handling are minimal in UI form input
- UI depends on external Steam API availability and latency
- `Main` (HTTP) and `FrontEnd` (Swing) are separate entry paths with different concerns

## Contribution Guidelines

- Keep business logic in service classes; keep UI-specific logic in `FrontEnd`.
- When adding tests for static services, isolate/reset static state per test.
- Prefer small methods and explicit validation returns (`boolean` + message/log).
- Update `README.md`, `USER_DOCS.md`, and `DEVELOPER_DOCS.md` for behavior changes.
