package app.services;

import app.dtos.unsplash.PhotoDTO;
import app.entities.Color;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UnsplashServiceTest {
    private UnsplashService unsplashService;

    @BeforeEach
    void setup() {
        unsplashService = new UnsplashService();
    }

    @Test
    @DisplayName("Test fetching photos by color returns a non-empty list of photos")
    void getPicturesWithPickedColor() {
         // Arrange & Act
        List<PhotoDTO> result = unsplashService.getPicturesWithPickedColor(Color.YELLOW);

        // Assert
        assertNotNull(result, "Listen må ikke være null");
        assertFalse(result.isEmpty(), "Listen skal indeholde billeder fra Unsplash");
        assertEquals(10, result.size(), "Der skal som standard hentes 10 billeder");

        // Tjek at det første billede har data udfyldt korrekt i DTO'en
        PhotoDTO firstPhoto = result.get(0);
        assertNotNull(firstPhoto.getId(), "Billedets id skal være udfyldt");
        assertNotNull(firstPhoto.getUrl(), "Billedets url-DTO skal være udfyldt");
        assertNotNull(firstPhoto.getUser(), "Billedets user-DTO skal være udfyldt");

        // Print det første billede til konsollen som en bonus
        System.out.println("Test succesfuld! Første billede: " + firstPhoto);
    }
}
