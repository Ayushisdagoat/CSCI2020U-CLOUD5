# Video Game Store Catalogue

## Overview
The **Video Game Store Catalogue** is a web-based system designed to organize and display detailed information about video games. The system allows users to browse, search, and evaluate games using categories such as genre, platform, rating, and price.

The goal of the project is to help users discover games easily while allowing administrators to manage the catalogue through a controlled interface.

---

## Features

### Admin Features
- Add new games to the catalogue
- Edit existing game information
- Remove games from the catalogue
- Manage genres, platforms, and features
- Approve or reject user reviews
- Set and update game prices
- Search and filter games within the admin panel

### User Features
- Browse the video game catalogue
- Search games using filters (genre, platform, rating, price)
- View detailed game pages
- See ratings and approved reviews
- Submit reviews and star ratings

---

## User Roles

### Admin
Admins have full control of the catalogue. They can manage game entries, moderate reviews, and maintain the overall system.

### User
Users can browse the catalogue, search for games, view game information, and submit reviews.

---

## Technologies Used
- HTML
- CSS
- JavaScript
- Java + Maven
- SQL or NoSQL Database
- Git & GitHub

---


---

## Build and Run Instructions (IMPORTANT)

### Requirements
- Java JDK 23 installed  
- Maven installed  

---

### Run the Project (Windows)

1. Clone the repository:


```
git clone https://github.com/yourusername/CSCI2020U-CLOUD5.git
```

2. Navigate to the project folder

```
cd CSCI2020U-CLOUD5.git
```


3. Double-click:


```
build.bat
```


---

### What the Build Script Does

The build script automatically:

- Cleans previous builds  
- Compiles the project  
- Runs all unit tests  
- Launches the application  

---

### 🧪 Manual Commands (Alternative)

If needed, the project can also be run manually:
```
mvn clean install
mvn exec:java -Dexec.mainClass="org.example.FrontEnd"
```
---

---

### Login Credentials

- Admin: `admin / admin123`  
- User: `user / user123`  

## Usage

1. Login as an **Admin** or **User**.
2. Browse the catalogue of games.
3. Use the **search and filter system** to find specific games.
4. View game details including description, features, and price.
5. Users can submit reviews and star ratings.
6. Admins approve reviews before they are published.

---


## Risks and Challenges
- Authentication and role-based access control complexity
- Database and filtering logic
- Integration between frontend and backend
- Time constraints during development

---

## Team Members
- Swhiab Sudaad
- Aaryan Kulkarni
- Ayush Koodathunkal Arunkumar
- Theevigan Jeyachandran
- Zayan Khan

---

## Future Improvements
- Game recommendation system
- Advanced search functionality
- External API integration
- Personalized user suggestions
