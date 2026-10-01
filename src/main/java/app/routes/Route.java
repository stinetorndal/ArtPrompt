package app.routes;

import app.controllers.UserController;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

//Her ligger alle routes og controllerne instantieres, applikationens vejkort
//Route er bindelig mellem controller og Javalin-server
public class Route {

    private final UserController userController;

    public Route(UserController userController) {
        this.userController = userController;
    }
    public EndpointGroup getRoutes () {
        return () -> {
            path("/api/v1", () -> {  // fortæller det er api
                path("/auth", () -> { //standard for Authentication = email + pw. Javalin sætter flg sammen:
                    post("/register", userController::register);
                    post("/login", userController::login);
                });
            });
        };
    }
    }

