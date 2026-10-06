package app.controllers;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.dtos.savedimages.SavedImageDTO;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SavedImageControllerTest {

    private static Javalin app;
    private static EntityManagerFactory emf;

    @BeforeAll
    static void beforeAll() {
        // Hent EMF til test-databasen (artprompt_test)
        emf = HibernateConfig.getEntityManagerFactory();

        // Start Javalin test-server på port 7777
        app = ApplicationConfig.startServer(7777);

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7777;
    }

    @AfterAll
    static void afterAll() {
        if (app != null) {
            ApplicationConfig.stopServer(app);
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    // ==========================================
    // POST /api/v1/images/save
    // ==========================================
    @Test
    @DisplayName("POST /api/v1/images/save gemmer et billede for bruger med ID 1")
    void saveImage_validData_returns201() {
        // Opretter DTO for billedet koblet til bruger-ID 1 fra testDB
        SavedImageDTO inputDTO = new SavedImageDTO(
                null,
                "https://example.com/test-image.jpg",
                "ext-999",
                "Mona Lisa Test",
                "Rijksmuseum",
                1L // matcher bruger med id=1 i artprompt_test
        );

        given()
                .contentType(ContentType.JSON)
                .body(inputDTO)
                .when()
                .post("/api/v1/images/save")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", is("Mona Lisa Test"))
                .body("source", is("Rijksmuseum"));
    }

    // ==========================================
    // GET /api/v1/images/user/{userId}
    // ==========================================
    @Test
    @DisplayName("GET /api/v1/images/user/1 henter billeder for bruger ID 1")
    void getSavedImagesByUser_existingUserId_returns200() {
        given()
                .when()
                .get("/api/v1/images/user/1")
                .then()
                .statusCode(200);
    }
}