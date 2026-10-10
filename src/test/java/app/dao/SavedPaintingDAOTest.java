package app.services;

import app.config.HibernateConfig;
import app.dao.UserDAO;
import app.dtos.savedpaintings.SavedPaintingDTO;
import app.entities.SavedPainting;
import app.entities.User;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SavedPaintingServiceTest {

    private static EntityManagerFactory emf;
    private static SavedPaintingService service;
    private static UserDAO userDAO;

    private User user1;
    private User user2;

    @BeforeAll
    static void setUpAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        service = new SavedPaintingService(emf);
        userDAO = new UserDAO(emf);
    }

    @BeforeEach
    void setUp() {
        // Ryd tabeller i korrekt rækkefølge (SavedPainting før User pga. FK)
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM SavedPainting").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.getTransaction().commit();
        }

        // Opret to testbrugere i databasen
        user1 = userDAO.create(new User("bruger1@test.dk", "password123"));
        user2 = userDAO.create(new User("bruger2@test.dk", "password123"));
    }

    @Test
    @DisplayName("savePaintingForUser gemmer maleriet korrekt koblet til brugerens ID")
    void savePainting_isStoredWithCorrectUserId() {
        SavedPaintingDTO dto = makeDto("SK-C-5", "Nattevagten", user1.getId());
        SavedPaintingDTO saved = service.savePaintingForUser(dto);

        // Verificer direkte i databasen
        try (EntityManager em = emf.createEntityManager()) {
            List<SavedPainting> paintings = em.createQuery(
                            "SELECT s FROM SavedPainting s WHERE s.user.id = :userId", SavedPainting.class)
                    .setParameter("userId", user1.getId())
                    .getResultList();

            assertEquals(1, paintings.size());
            SavedPainting fromDb = paintings.get(0);

            assertEquals("Nattevagten", fromDb.getTitle());
            assertEquals("SK-C-5", fromDb.getExternalId());
            assertNotNull(fromDb.getUser());
            assertEquals(user1.getId(), fromDb.getUser().getId());
        }
    }

    @Test
    @DisplayName("Et gemt maleri vises kun for den bruger der har gemt det")
    void savePainting_doesNotAppearForOtherUser() {
        service.savePaintingForUser(makeDto("SK-C-5", "Nattevagten", user1.getId()));

        List<SavedPaintingDTO> forUser1 = service.getSavedPaintingsByUserId(user1.getId());
        List<SavedPaintingDTO> forUser2 = service.getSavedPaintingsByUserId(user2.getId());

        assertEquals(1, forUser1.size());
        assertTrue(forUser2.isEmpty());
    }

    @Test
    @DisplayName("savePaintingForUser kaster 404 ApiException hvis brugeren ikke findes")
    void savePainting_unknownUser_throws404() {
        SavedPaintingDTO dto = makeDto("SK-C-5", "Nattevagten", 999999L);

        ApiException ex = assertThrows(ApiException.class, () -> service.savePaintingForUser(dto));
        assertEquals(404, ex.getCode());
    }

    @Test
    @DisplayName("savePaintingForUser kaster 400 ApiException ved forsøg på at gemme dublet")
    void savePainting_duplicateForSameUser_throws400() {
        service.savePaintingForUser(makeDto("SK-C-5", "Nattevagten", user1.getId()));

        ApiException ex = assertThrows(ApiException.class,
                () -> service.savePaintingForUser(makeDto("SK-C-5", "Nattevagten", user1.getId())));
        assertEquals(400, ex.getCode());
    }

    @Test
    @DisplayName("Samme externalId må gerne gemmes af to forskellige brugere")
    void savePainting_sameExternalIdForDifferentUsers_isAllowed() {
        service.savePaintingForUser(makeDto("SK-C-5", "Nattevagten", user1.getId()));
        SavedPaintingDTO second = service.savePaintingForUser(makeDto("SK-C-5", "Nattevagten", user2.getId()));

        assertNotNull(second);
        assertEquals(user2.getId(), second.getUserId());
    }

    // Hjælpemetode til at bygge SavedPaintingDTO
    private SavedPaintingDTO makeDto(String externalId, String title, Long userId) {
        SavedPaintingDTO dto = new SavedPaintingDTO();
        dto.setExternalId(externalId);
        dto.setTitle(title);
        dto.setArtist("Rembrandt van Rijn");
        dto.setUrl("https://lh3.googleusercontent.com/test-image.jpg");
        dto.setYear(1642);
        dto.setUserId(userId);
        return dto;
    }
}