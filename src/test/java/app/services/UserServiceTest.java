package app.services;

import app.config.HibernateConfig;
import app.dao.UserDAO;
import app.entities.User;
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
        emf = HibernateConfig.getEntityManagerFactory();
    }

    @BeforeEach
    void setUp() {
        UserDAO userDAO = new UserDAO(emf);
        userService = new UserService(userDAO);
    }

    // POSITIV TEST: Bruger oprettes korrekt
    @Test
    void testCreateUserSuccess() {
        System.out.println("------Bruger oprettes------");
        // Arrange
        String email = "artprompt1@test.dk";
        String validPassword = "Password1!";

        // Act
        User createdUser = userService.createUser(email, validPassword);
        System.out.println(createdUser.getId() + ", " + createdUser.getEmail() + ", " + createdUser.getPassword());
        // Assert
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(email, createdUser.getEmail());

        // Check password er blevet hashed og IKKE gemt i klar tekst
        assertNotEquals(validPassword, createdUser.getPassword());
        assertTrue(createdUser.getPassword().startsWith("$2a$")); // BCrypt prefix
    }

    // NEGATIV TEST: Ugyldig e-mail kaster fejl
    @Test
    void testCreateUserInvalidEmailThrowsException() {
        // Arrange
        String invalidEmail = "hej.dk";
        String validPassword = "Password1!";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(invalidEmail, validPassword)
        );

        System.out.println("Forventet fejlmeddelelse: " + exception.getMessage());
        assertTrue(exception.getMessage().toLowerCase().contains("email"));
    }

    // NEGATIV TEST: For svagt password kaster fejl
    @Test
    void testCreateUserWeakPasswordThrowsException() {
        // Arrange
        String email = "artist2@test.dk";
        String weakPassword = "hej";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(email, weakPassword)
        );

        System.out.println("Forventet fejlmeddelelse: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Adgangskode skal være mindst 8 tegn"));
    }

    @Test
    void testLoginSucces() {
        System.out.println("------positiv test / login ------");
        //Arrange
        String email = "test@test.dk";
        String password = "Qwerty1!";
        userService.createUser(email, password);
        //act
        User loggedInUser = userService.login(email, password);
        //Print og assert
        System.out.println("Login lykkedes for: "+  loggedInUser.getEmail());
        assertNotNull(loggedInUser);
        assertEquals(email, loggedInUser.getEmail());
    }

    @Test
    void testLoginSuccess() {
        System.out.println("\n--- POSITIV TEST: LOGIN ---");

        // Arrange: Opretter først en bruger i DB
        String email = "login@test.dk";
        String password = "Password1!";
        userService.createUser(email, password);

        // Act: Forsøger at logge ind med de rigtige oplysninger
        User loggedInUser = userService.login(email, password);

        // Print & Assert
        System.out.println("Login lykkedes for: " + loggedInUser.getEmail());
        assertNotNull(loggedInUser);
        assertEquals(email, loggedInUser.getEmail());
    }

    @Test
    void testLoginWrongPasswordThrowsException() {
        System.out.println("------ negativ test / forkert pw ------");

        // Arrange
        String email = "test2@test.dk";
        String password = "Password1!";
        userService.createUser(email, password);

        // Act & Assert: Forsøger login med forkert password
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.login(email, "ForkertPassword1!")
        );

        System.out.println("Fanget forventet fejl: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Forkert email eller adgangskode"));
    }
}