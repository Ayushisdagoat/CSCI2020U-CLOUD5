# CLOUD5 User Documentation

## What This App Is

CLOUD5 is a Java desktop app for browsing a video game catalog, viewing game details, and managing a personal cart (wishlist).

It supports two roles:

- `admin` (catalog management)
- `user` (browse and cart features)

## Default Login Accounts

- Admin: `admin` / `admin123`
- User: `user` / `user123`

## System Requirements

- Windows 10/11, macOS, or Linux
- Java JDK 23
- Maven
- Internet connection (game data and images are fetched from Steam API)

## Starting the App

### Option 1 (Windows quick start)

Run `build.bat` from the project root.

This script will:

1. Build the project
2. Run tests
3. Launch the Swing app

### Option 2 (manual)

From the project root:

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="org.example.FrontEnd"
```

## How To Use

### 1) Log In

Open the app and enter one of the valid usernames/passwords.

### 2) Browse the Catalog

After login, you will see a catalog page with game cards.

- Use the search bar at the top to filter visible game cards by text.
- Click any game card to open its details page.

### 3) Use the Cart (User role)

- On a game details page, click `Add to Cart`.
- Use the `Cart` button in the top-right area of the catalog page to view all saved games.
- A game can only be added once per user.

### 4) Admin Features (Admin role)

Admins can:

- Add a game using the `Add Game` button
- Remove a game by ID using the `Remove Game` button

## Notes and Limitations

- Data in services is currently in-memory (`ArrayList`), so state does not persist after app exit.
- The repository includes a `game_catalogue.db` file, but core services in this version use in-memory storage.
- Genre tags shown in the UI are placeholders in some views.
- If Steam API is unavailable, game cards/details may fail to load.

## Troubleshooting

- **App does not start**
  Confirm `java -version` shows JDK 23, confirm `mvn -version` works, and re-run `mvn clean install` to inspect build errors.

- **No games appear**
  Check internet access. Steam API requests may be blocked or rate-limited.

- **Login fails**
  Use exact default credentials above. Username/password are case-sensitive.

- **Cart appears empty**
  Cart is per-user; make sure you are logged into the same account used to add items.
