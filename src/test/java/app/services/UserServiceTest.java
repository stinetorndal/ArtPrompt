package app.services;

import app.config.HibernateConfig;
import app.dao.UserDAO;
import app.dtos.user.UserDTO;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static EntityManagerFactory emf;
    private UserService userService;

    @BeforeAll
    static void setUpAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
    }

    @BeforeEach
    void setUp() {
        UserDAO userDAO = new UserDAO(emf);
        userService = new UserService(userDAO);
    }

    // POSITIV TEST: Bruger oprettes korrekt
    @Test
    void testCreateUserSuccess() {
        System.out.println("------ Bruger oprettes ------");
        // Arrange
        UserDTO inputDTO = new UserDTO("testbruger@test.dk", "Password1!");

        // Act
        UserDTO createdUser = userService.createUser(inputDTO);

        // Assert
        assertNotNull(createdUser);
        assertEquals("testbruger@test.dk", createdUser.getEmail());

        // Password skal være null i DTO retur af sikkerhedshensyn
        assertNull(createdUser.getPassword());
    }

    // NEGATIV TEST: Ugyldig e-mail kaster ApiException
    @Test
    void testCreateUserInvalidEmailThrowsException() {
        // Arrange
        UserDTO inputDTO = new UserDTO("hej.dk", "Password1!");

        // Act & Assert
        ApiException exception = assertThrows(
                ApiException.class,
                () -> userService.createUser(inputDTO)
        );

        assertEquals(400, exception.getCode());
        assertTrue(exception.getMessage().toLowerCase().contains("email"));
    }

    // NEGATIV TEST: For svagt password kaster ApiException
    @Test
    void testCreateUserWeakPasswordThrowsException() {
        // Arrange
        UserDTO inputDTO = new UserDTO("testbruger@test.dk", "hej");

        // Act & Assert
        ApiException exception = assertThrows(
                ApiException.class,
                () -> userService.createUser(inputDTO)
        );

        assertEquals(400, exception.getCode());
        assertTrue(exception.getMessage().contains("mindst 8 tegn"));
    }

    // POSITIV TEST: Login lykkedes
    @Test
    void testLoginSuccess() {
        System.out.println("------ POSITIV TEST: LOGIN ------");

        // Arrange: Opretter først bruger i DB
        UserDTO inputDTO = new UserDTO("testbruger@test.dk", "Password1!");
        userService.createUser(inputDTO);

        // Act: Login med samme DTO
        UserDTO loggedInUser = userService.login(inputDTO);

        // Assert
        assertNotNull(loggedInUser);
        assertEquals("testbruger@test.dk", loggedInUser.getEmail());
        assertNull(loggedInUser.getPassword()); // Sikrer at password ikke sendes med ud
    }

    // NEGATIV TEST: Forkert password ved login kaster ApiException
    @Test
    void testLoginWrongPasswordThrowsException() {
        System.out.println("------ NEGATIV TEST: FORKERT PW ------");

        // Arrange
        UserDTO originalUser = new UserDTO("testbruger2@test.dk", "Password1!");
        userService.createUser(originalUser);

        UserDTO wrongPasswordInput = new UserDTO("testbruger2@test.dk", "SkrevForkertPassword1!");

        // Act & Assert
        ApiException exception = assertThrows(
                ApiException.class,
                () -> userService.login(wrongPasswordInput)
        );

        assertEquals(400, exception.getCode());
        assertTrue(exception.getMessage().contains("Forkert email eller adgangskode"));
    }
}