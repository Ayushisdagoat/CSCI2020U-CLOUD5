package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceSearchTest {

    private GameService gameService;

    @BeforeEach
    void setUp() throws Exception {
        gameService = new GameService();
        resetGameServiceState();

        gameService.addGame("Elden Ring", "RPG", "PC", 79.99, "Open world RPG", "url1");
        gameService.addGame("FIFA 24", "Sports", "PlayStation", 69.99, "Football game", "url2");
        gameService.addGame("Cyberpunk 2077", "RPG", "PC", 59.99, "Sci-fi RPG", "url3");
        gameService.addGame("Call of Duty", "Shooter", "Xbox", 89.99, "FPS game", "url4");

        gameService.updateAverageRating(1, 4.8);
        gameService.updateAverageRating(2, 3.9);
        gameService.updateAverageRating(3, 4.2);
        gameService.updateAverageRating(4, 4.9);
    }

    @SuppressWarnings("unchecked")
    private void resetGameServiceState() throws Exception {
        String[] fields = {
                "ids", "titles", "genres", "platforms",
                "prices", "descriptions", "trailerUrls", "averageRatings"
        };

        for (String fieldName : fields) {
            Field field = GameService.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            ((ArrayList<?>) field.get(null)).clear();
        }

        Field nextIdField = GameService.class.getDeclaredField("nextId");
        nextIdField.setAccessible(true);
        nextIdField.setInt(null, 1);
    }

    @Test
    void testSearchPlatformReturnsOnlyPcGames() {
        String result = gameService.searchGames("", "PC", "", 0.0);

        assertTrue(result.contains("Elden Ring"));
        assertTrue(result.contains("Cyberpunk 2077"));
        assertFalse(result.contains("FIFA 24"));
        assertFalse(result.contains("Call of Duty"));
    }
}
