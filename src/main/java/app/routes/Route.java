package app.routes;

import app.controllers.*;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

//Her ligger alle routes og controllerne instantieres, applikationens vejkort
//Route er bindeled mellem controller og Javalin-server
public class Route {

    private final UserController userController;
    private final PromptWordController promptWordController;
    private final UnsplashController unsplashController;
    private final SavedImageController savedImageController;
    private final NoteController noteController;
    private final RijksmuseumController rijksmuseumController;
    private final SavedPaintingController savedPaintingController;

    public Route(UserController userController, PromptWordController promptWordController,
                 UnsplashController unsplashController, SavedImageController savedImageController,
                 NoteController noteController, RijksmuseumController rijksmuseumController,
                 SavedPaintingController savedPaintingController) {
        this.userController = userController;
        this.promptWordController = promptWordController;
        this.unsplashController = unsplashController;
        this.savedImageController = savedImageController;
        this.noteController= noteController;
        this.rijksmuseumController = rijksmuseumController;
        this.savedPaintingController = savedPaintingController;
    }
    public EndpointGroup getRoutes () {
        return () -> path("/api/v1", () -> {  // fortæller det er api

            //auth-endpoints = email+login
            path("/auth", () -> { //standard for Authentication = email + pw. Javalin sætter flg sammen:
                post("/register", userController::register, Role.ANYONE);
                post("/login", userController::login, Role.ANYONE);
            });

            // Prompt endpoints
            path("/prompts", () -> {
                get("/categories", promptWordController::getCategories, Role.ANYONE);
                get("/random", promptWordController::getRandomPrompt, Role.ANYONE);
            });

            // Unsplash endpoints
            path("/unsplash", () -> {
                get("/color", unsplashController::getPicturesByColor, Role.ANYONE);
            });

            //SavedImage endpoints
            path("/images", () -> {
                post("/save", savedImageController::saveImage,Role.USER, Role.ADMIN);
                get("/user/{userId}", savedImageController::getSavedImagesByUser, Role.USER, Role.ADMIN);
            });

            //Note endpoints
            path("/notes", () -> {
                post("/save", noteController::saveNote,Role.USER, Role.ADMIN);
                get("/user/{userId}", noteController::getSavedNotesByUser, Role.USER, Role.ADMIN);
            });

            // Rijksmuseum endpoints
            path("/rijksmuseum", () -> {
                get("/artist", rijksmuseumController::getPaintingsByArtist, Role.ANYONE);
            });

            //SavedPainting endpoints
            path("/paintings", () -> {
                post("/save", savedPaintingController::savePainting,Role.USER, Role.ADMIN);
                get("/user/{userId}", savedPaintingController::getSavedPaintingsByUser, Role.USER, Role.ADMIN);
                get("/random", savedPaintingController::getRandomPaintings, Role.ANYONE);
            });
        });
    }
    }

