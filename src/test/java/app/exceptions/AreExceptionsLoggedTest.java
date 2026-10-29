package app.exceptions;

import app.config.ApplicationConfig;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AreExceptionsLoggedTest {

    private static final Logger logger = LoggerFactory.getLogger(AreExceptionsLoggedTest.class);
    private static Javalin app;

    @BeforeAll
    static void setup() {
        app = ApplicationConfig.startServer(7070);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7070;
    }

    @AfterAll
    static void tearDown() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void testApiExceptionIsLoggedToFile() throws IOException {
        Path logFilePath = Path.of("logs/artprompt_errors.log");
        String expectedMessage = "Bruger ikke fundet";

        logger.error("ApiException (code=404): {}", expectedMessage);

        assertTrue(Files.exists(logFilePath), "Logfilen blev ikke oprettet på stien: " + logFilePath);

        List<String> logLines = Files.readAllLines(logFilePath);
        boolean containsExpectedLog = logLines.stream()
                .anyMatch(line -> line.contains(expectedMessage));

        assertTrue(containsExpectedLog, "Logfilen indeholder ikke forventet besked");
    }

    @Test
    void getPrompt_returns404WhenNotFound() {
        given()
                .accept("application/json")
                .when()
                .get("/api/v1/test-error")
                .then()
                .statusCode(404);
    }
}