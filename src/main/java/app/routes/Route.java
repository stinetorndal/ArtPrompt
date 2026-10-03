package app.routes;

import app.controllers.PromptWordController;
import app.controllers.UserController;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

//Her ligger alle routes og controllerne instantieres, applikationens vejkort
//Route er bindelig mellem controller og Javalin-server
public class Route {

    private final UserController userController;
    private final PromptWordController promptWordController;

    public Route(UserController userController, PromptWordController promptWordController) {
        this.userController = userController;
        this.promptWordController = promptWordController;
    }
    public EndpointGroup getRoutes () {
        return () -> path("/api/v1", () -> {  // fortæller det er api

            //auth-endpoints = email+login
            path("/auth", () -> { //standard for Authentication = email + pw. Javalin sætter flg sammen:
                post("/register", userController::register);
                post("/login", userController::login);
            });

            // Prompt endpoints
            path("/prompts", () -> {
                get("/categories", promptWordController::getCategories);
                get("/random", promptWordController::getRandomPrompt);
            });
        });
    }
    }

