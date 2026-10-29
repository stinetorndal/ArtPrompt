package app.services;

import app.config.HibernateConfig;
import app.dtos.note.NoteDTO;
import app.entities.User;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Denne test tester op imid testdb. Om note gemmes under rette bruger og får servers UTC-timestamp
public class NoteServiceTest {

    private static EntityManagerFactory emf;
    private NoteService noteService;
    private Long existingUserId;

    @BeforeAll
    static void setUpAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
    }

    //Tømmer note-tabellen og sørger for, der findes en testbruger at gemme noten under
    @BeforeEach
    void setup() {
        noteService = new NoteService(emf);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Note").executeUpdate();

            List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            User testUser;
            if (users.isEmpty()) {
                testUser = new User("testbruger@test.dk", "password123_");
                em.persist(testUser);
            } else {
                testUser = users.get(0);
            }
            em.getTransaction().commit();
            existingUserId = testUser.getId();
        }
    }

    //En gemt note får id, tilhører rette user og får datostempel
    @Test
    void testSaveNoteForUserSuccess() {
        //Arrange
        NoteDTO input = new NoteDTO(null, "Dark Fantasy aesthetic", existingUserId, null);
        LocalDate before = LocalDate.now();
        //Act
        NoteDTO result = noteService.saveNoteForUser(input);
        //Assert
        LocalDate after = LocalDate.now();
        assertNotNull(result.getId());
        assertEquals("Dark Fantasy aesthetic", result.getText());
        assertEquals(existingUserId, result.getUserId());
        assertNotNull(result.getCreatedAt());
        assertFalse(result.getCreatedAt().isBefore(before));
        assertFalse(result.getCreatedAt().isAfter(after));
    }

    // Dato sendt fra klient ignoreres fordi serveren sætter dato
    @Test
    void testCreatedAtFromClientIsIgnored() {
        //Arrange
        LocalDate fakeDate = LocalDate.of(2000, 1, 1);
        NoteDTO input = new NoteDTO(null, "Fake date", existingUserId, fakeDate);
        //Act
        NoteDTO result = noteService.saveNoteForUser(input);
        //Assert
        assertNotNull(result.getCreatedAt());
        assertNotEquals(fakeDate, result.getCreatedAt());
    }

    // Gemmer en note for bruger, der ikke eksisterer, Skal give  404
    @Test
    void testSaveNoteUserNotFoundThrowsApiException() {
        //Arrange
        NoteDTO input = new NoteDTO(null, "No owner", 9999L, null);
        //Act + Assert
        ApiException exception = assertThrows(ApiException.class, () -> noteService.saveNoteForUser(input));

        assertEquals(404, exception.getCode());
        assertTrue(exception.getMessage().contains("Brugeren blev ikke fundet"));
    }

    // Alle noter for en bruger skal vises med tekst, ejer og datostempel
    @Test
    void testGetSavedNotesByUserId() {
        noteService.saveNoteForUser(new NoteDTO(null, "Note 1", existingUserId, null));
        noteService.saveNoteForUser(new NoteDTO(null, "Note 2", existingUserId, null));

        List<NoteDTO> notes = noteService.getSavedNotesByUserId(existingUserId);

        assertEquals(2, notes.size());
        for (NoteDTO note : notes) {
            assertEquals(existingUserId, note.getUserId());
            assertNotNull(note.getCreatedAt());
        }
    }

}

