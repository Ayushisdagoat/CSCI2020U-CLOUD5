# CLOUD5 Game Store Catalogue

## Overview

CLOUD5 is a Java-based game catalogue project with two runnable entry points:

- A Swing desktop app for login, browsing, and cart management
- A lightweight HTTP server that serves `index.html`

The project is designed around role-based access (`admin` and `user`) and service-layer logic implemented in Java.

## Table of Contents

- [Documentation](#documentation)
- [Architecture](#architecture)
- [Application Flow](#application-flow)
- [Feature Highlights](#feature-highlights)
- [Tech Stack](#tech-stack)
- [Requirements](#requirements)
- [Getting Started](#getting-started)
- [Run Modes](#run-modes)
- [Command Reference](#command-reference)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Service Responsibility Map](#service-responsibility-map)
- [Configuration Notes](#configuration-notes)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [Default Credentials](#default-credentials)
- [Known Limitations](#known-limitations)
- [Future Enhancements](#future-enhancements)
- [Team Members](#team-members)

## Documentation

- User guide: `USER_DOCS.md`
- Developer guide: `DEVELOPER_DOCS.md`

## Architecture

The codebase follows a simple layered architecture:

- **Presentation layer**: `FrontEnd` (Swing UI) and `Main` (HTTP entry point)
- **Service layer**: business logic in `AuthService`, `GameService`, `ReviewService`, `WishlistService`, `GenreService`, `NotificationService`, and `UserService`
- **Data layer (current)**: in-memory static collections (`ArrayList`)
- **External integration**: Steam API for game metadata in UI rendering

### Architecture Diagram

```text
User
  |
  v
FrontEnd (Swing UI) ----------------------+
  |                                        |
  v                                        v
Service Layer (Auth/Game/Review/etc.)   Main (HTTP server)
  |                                        |
  v                                        v
In-memory state (ArrayLists)            index.html
  |
  v
Steam API (metadata/images fetched by UI)
```

## Application Flow

### Login and Role Routing

1. User submits credentials in `FrontEnd`
2. `AuthService.login(...)` returns `admin`, `user`, or `invalid`
3. UI routes to catalog with role-specific actions

### Catalog and Details

1. UI loads configured Steam app IDs
2. Metadata/images are fetched from Steam API
3. Cards are rendered; selecting a card opens detail view

### Cart/Wishlist

1. User clicks `Add to Cart` in details view
2. `WishlistService.addToWishlist(...)` validates duplicates
3. `Cart` button shows user-specific wishlist entries

### Review Moderation (Service Level)

1. User review submission is stored as pending
2. Notification is generated for admin
3. Approval updates game average rating

## Feature Highlights

### User

- Log in and browse a Steam-backed catalog
- Search visible games from the catalog screen
- Open detailed game views
- Add games to personal cart/wishlist

### Admin

- All user capabilities, plus:
- Add games to the in-memory catalog
- Remove games by ID
- Access service-level moderation/workflow features through backend classes

## Tech Stack

- Java 23
- Maven
- Swing (`javax.swing`) for desktop UI
- `com.sun.net.httpserver.HttpServer` for local HTTP serving
- `org.json` for JSON parsing
- JUnit 5 for tests

## Requirements

- Java JDK 23
- Maven 3.9+
- Internet connection (Steam API is used by the Swing UI for game metadata/images)

## Getting Started

### Windows quick start

Run:

```bat
build.bat
```

The script will:

1. Run `mvn clean install`
2. Execute tests
3. Launch the Swing app (`org.example.FrontEnd`)

### Manual setup

```bash
mvn clean install
```

## Run Modes

### 1) Desktop App (primary)

```bash
mvn exec:java -Dexec.mainClass="org.example.FrontEnd"
```

### 2) HTTP Server (serves static page)

```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

Server URL after startup:

```text
http://localhost:6789
```

## Command Reference

Common commands from the repository root:

```bash
# Clean + compile + test + package
mvn clean install

# Run tests only
mvn test

# Run Swing app
mvn exec:java -Dexec.mainClass="org.example.FrontEnd"

# Run HTTP server
mvn exec:java -Dexec.mainClass="org.example.Main"
```

## Testing

Run all tests:

```bash
mvn test
```

Current test classes include:

- `GameServiceSearchTest`
- `SessionServiceTest`
- `WishlistServiceTest`

## Project Structure

- `src/main/java/org/example` - application classes and services
- `src/main/resources` - static resources (`index.html`)
- `src/test/java/org/example` - JUnit test suite
- `build.bat` - Windows helper script for build/test/run

## Service Responsibility Map

- `AuthService`: user authentication and credential-role storage
- `UserService`: user management validations and admin-facing account operations
- `SessionService`: current-session state and role checks
- `GameService`: catalog CRUD, metadata fields, search, and average rating storage
- `ReviewService`: review submission, moderation state, rating aggregation
- `WishlistService`: user-specific cart entries with duplicate prevention
- `GenreService`: bounded list of catalog genres
- `NotificationService`: unread/read admin notifications for review workflow

## Configuration Notes

- HTTP server port in `Main`: `6789`
- Java compiler target/source in `pom.xml`: `23`
- Build plugin: Maven Surefire for tests
- Runtime helper: Maven Exec plugin for launching main classes

## Troubleshooting

- **Build fails on Maven**
  Ensure `java -version` reports JDK 23 and `mvn -version` resolves correctly in PATH.

- **Application launches but no games render**
  Verify internet access and retry; catalog data and images are loaded from the Steam API.

- **Port conflict for HTTP mode**
  Change the port constant in `Main` if `6789` is already in use.

- **Tests behave inconsistently**
  Re-run from a clean state with `mvn clean test`; some service tests rely on static in-memory state reset.

- **Login rejected**
  Re-enter exact default credentials and confirm case-sensitive username/password input.

## Contributing

1. Create a feature branch from your main working branch.
2. Make focused changes with clear commit messages.
3. Run:
   - `mvn clean test`
   - Markdown lint checks for docs updates (if available in your environment)
4. Update relevant docs (`README.md`, `USER_DOCS.md`, `DEVELOPER_DOCS.md`) when behavior changes.
5. Open a pull request with:
   - What changed
   - Why it changed
   - How it was tested

## Operational Notes

- Use `mvn clean install` before demos to ensure tests pass
- If Steam API is slow/unavailable, the Swing catalog may load partially
- Services are static and stateful during process runtime; restart resets data

## Default Credentials

- Admin: `admin` / `admin123`
- User: `user` / `user123`

## Known Limitations

- Core service data is currently in-memory and not persisted across restarts
- Steam API availability affects catalog/image loading
- Some backend services are more complete than current UI exposure
- Static mutable service state is not designed for concurrent multi-user deployment

## Future Enhancements

- Replace in-memory stores with persistent database-backed repositories
- Add robust input validation and error feedback in the Swing UI
- Expand admin UI for review moderation and notification management
- Add CI checks for tests and markdown/docs validation
- Improve API abstraction to support non-Steam data sources

## Team Members

- Swhiab Sudaad
- Aaryan Kulkarni
- Ayush Koodathunkal Arunkumar
- Theevigan Jeyachandran
- Zayan Khan
