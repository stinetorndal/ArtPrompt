package app.mappers;

import app.dtos.savedpaintings.SavedPaintingDTO;
import app.entities.SavedPainting;
import app.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//Tester at data flyttes korrekt fra entitet til DTO
class SavedPaintingMapperTest {

    private SavedPaintingMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SavedPaintingMapper();
    }

    @Test
    @DisplayName("toDTO - sikrer at title og artist ikke ombyttes")
    void toDTO_verificerPlaceringAfFelter() {
        // Arrange
        User user = new User("test@example.com", "password123");

        SavedPainting painting = new SavedPainting(
                1L,
                "SK-C-5",
                "https://mh.nl/SK-C-5.jpg",
                "Rembrandt van Rijn",
                "Nattevagten",
                1642,
                user
        );

        // Act
        SavedPaintingDTO dto = mapper.toDTO(painting);

        // Assert
        assertNotNull(dto);
        assertEquals("SK-C-5", dto.getExternalId());
        assertEquals("Nattevagten", dto.getTitle(), "Titel skal være Nattevagten");
        assertEquals("Rembrandt van Rijn", dto.getArtist(), "Artist skal være Rembrandt van Rijn");
        assertEquals("https://mh.nl/SK-C-5.jpg", dto.getUrl());
        assertEquals(1642, dto.getYear());
    }

    @Test
    @DisplayName("toEntity - mapper korrekt fra DTO til Entitet")
    void toEntity_success() {
        // Arrange
        User user = new User("test@example.com", "password123");
        SavedPaintingDTO dto = new SavedPaintingDTO(
                "SK-A-2344",
                "Mælkepigen",
                "Johannes Vermeer",
                "https://mh.nl/SK-A-2344.jpg",
                1660,
                1L
        );

        // Act
        SavedPainting entity = mapper.toEntity(dto, user);

        // Assert
        assertNotNull(entity);
        assertEquals("Mælkepigen", entity.getTitle());
        assertEquals("Johannes Vermeer", entity.getArtist());
    }

    @Test
    @DisplayName("toDTO - returnerer null hvis input er null")
    void toDTO_null() {
        assertNull(mapper.toDTO(null));
    }
}