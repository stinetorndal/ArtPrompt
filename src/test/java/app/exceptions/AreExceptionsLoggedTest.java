package app.exceptions;

import app.config.ApplicationConfig;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AreExceptionsLoggedTest {
        private static Javalin app;
    @BeforeAll
    static void setup() {
        // Starter Javalin serveren på port 7070 før alle tests kører
        app = ApplicationConfig.startServer(7070);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7070;
    }

    @AfterAll
    static void tearDown() {
        // Lukker serveren ned efter alle tests er færdige
        ApplicationConfig.stopServer(app);
    }


        @Test
    void testApiExceptionIsLoggedToFile() throws IOException {
        //Arrange
        Path logFilePath = Path.of("logs/artprompt_errors.log"); //Path er en repræsentation af sti på disken
        Files.deleteIfExists(logFilePath); //Sletter eventuel gammel log. File kan checke om en fil findes

        String expectedMessage = "Bruger ikke fundet";
        int statusCode = 404;

        //Act
        try {
            throw new ApiException(statusCode, expectedMessage);
        } catch (ApiException e) {
            //Her fanges fejl kun. Bør være skrevet til filen i loggen
        }

        //Assert
        assertTrue(Files.exists(logFilePath), "Logfilen blev ikke oprettet på stien: " + logFilePath);

        List<String> logLines = Files.readAllLines(logFilePath);
        boolean containsExpectedLog = false;
        for (String line : logLines) {
            if (line.contains("ApiException (code=404): Bruger ikke fundet")) {
                containsExpectedLog = true;
                break;
            }
        }
        assertTrue(containsExpectedLog, "Logfilen indeholder ikke forventet besked");
    }

    //Starter server op, fanger den fejlen og returnerer den det korrekte json-format
    @Test
    void getPrompt_returns404WhenNotFound() {

        given()
                .contentType("application/json")
        .when()
                .get("/api/v1/test-error") // Et ID der ikke findes
        .then()
                .statusCode(404)
                .body("message", equalTo("Prompt ikke fundet"));
    }
}
