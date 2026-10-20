package app.controllers;

import app.config.ApplicationConfig;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

//Denne klase tester et endpoint på en tilfældig ledig port og checker server svarer med status 200 ok
class PublicApiTest {

    private static Javalin app;

    @BeforeAll
    static void setUp() {
        // Starter serveren på en ledig port
        app = ApplicationConfig.startServer(0);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = app.port();
    }

    @AfterAll
    static void tearDown() {
        if (app != null) {
            ApplicationConfig.stopServer(app);
        }
    }

    @Test
    void testGetCategoriesReturns200WithRestAssured() {
        given()
                .accept("application/json")
                .when()
                .get("/api/v1/prompts/categories")
                .then()
                .statusCode(200);
    }
}