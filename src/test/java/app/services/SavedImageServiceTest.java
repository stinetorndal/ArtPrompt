package app.services;

import app.config.HibernateConfig;
import app.dtos.savedimages.SavedImageDTO;
import app.entities.User;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SavedImageServiceTest {

    private static EntityManagerFactory emf;
    private SavedImageService savedImageService;
    private Long existingUserId; // Bruges til at gemme det faktiske oprettede ID

    @BeforeAll
    static void setUpAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        savedImageService = new SavedImageService(emf);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // 1. Tøm eksisterende billeder
            em.createQuery("DELETE FROM SavedImage").executeUpdate();

            // 2. Tjek om en testbruger findes, ellers opret en
            List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            User testUser;
            if (users.isEmpty()) {
                testUser = new User("testbruger@test.dk", "password123");
                em.persist(testUser);
            } else {
                testUser = users.get(0);
            }

            em.getTransaction().commit();

            // Gem det reelle ID fra databasen
            this.existingUserId = testUser.getId();
        }
    }

    @Test
    void testSaveImageForUserSuccess() {
        // Arrange
        SavedImageDTO inputDto = new SavedImageDTO(
                null,
                "https://images.unsplash.com/photo-test",
                "unsplash-id-123",
                "Test Title",
                "Unsplash",
                existingUserId
        );

        // Act
        SavedImageDTO result = savedImageService.saveImageForUser(inputDto);

        // Assert
        assertNotNull(result.getId(), "Det gemte billede bør have fået tildelt et ID fra databasen");
        assertEquals("unsplash-id-123", result.getExternalId());
        assertEquals(existingUserId, result.getUserId());
    }

    @Test
    void testSaveImageForUserUserNotFoundThrowsApiException() {
        // Arrange - Et bruger-ID der garanteret IKKE findes
        Long nonExistingUserId = 9999L;
        SavedImageDTO inputDto = new SavedImageDTO(
                null,
                "https://images.unsplash.com/photo-test",
                "unsplash-id-999",
                "Test Title",
                "Unsplash",
                nonExistingUserId
        );

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            savedImageService.saveImageForUser(inputDto);
        });

        assertEquals(404, exception.getCode());
        assertTrue(exception.getMessage().contains("Brugeren blev ikke fundet"));
    }

    @Test
    void testGetSavedImagesByUserId() {
        // Arrange - Gem 2 billeder for test-brugeren
        SavedImageDTO dto1 = new SavedImageDTO(null, "https://url1.com", "id-1", "Billede 1", "Unsplash", existingUserId);
        SavedImageDTO dto2 = new SavedImageDTO(null, "https://url2.com", "id-2", "Billede 2", "Unsplash", existingUserId);

        savedImageService.saveImageForUser(dto1);
        savedImageService.saveImageForUser(dto2);

        // Act
        List userImages = savedImageService.getSavedImagesByUserId(existingUserId);

        // Assert
        assertNotNull(userImages);
        assertEquals(2, userImages.size(), "Brugeren bør have 2 gemte billeder i databasen");
    }
}