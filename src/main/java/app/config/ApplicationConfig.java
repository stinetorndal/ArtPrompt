package app.config;

import app.controllers.*;
import app.dao.UserDAO;
import app.dtos.MessageDTO;
import app.exceptions.ApiException;
import app.mappers.SavedPaintingMapper;
import app.routes.Route;
import app.services.*;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//Konfigurerer og opretter Javalin-server + starter den og returnerer app-instans
//Controller er kun handler
public class ApplicationConfig {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    public static Javalin startServer(int port) {

        // Afhængighedskæden: EMF -> DAO -> Service -> Controller -> Route
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        UserDAO userDAO = new UserDAO(emf);
        UserService userService = new UserService(userDAO);
        UserController userController = new UserController(userService);

        PromptService promptService = new PromptService(emf);
        PromptWordController promptWordController = new PromptWordController(promptService);

        UnsplashService unsplashService = new UnsplashService();
        UnsplashController unsplashController = new UnsplashController(unsplashService);

        SavedImageService savedImageService = new SavedImageService(emf);
        SavedImageController savedImageController = new SavedImageController(savedImageService);

        NoteService noteService = new NoteService(emf);
        NoteController noteController = new NoteController(noteService);

        RijksmuseumService rijksmuseumService = new RijksmuseumService();
        SavedPaintingMapper savedPaintingMapper = new SavedPaintingMapper();
        RijksmuseumController rijksmuseumController = new RijksmuseumController(rijksmuseumService, savedPaintingMapper);

        SavedPaintingService savedPaintingService = new SavedPaintingService(emf);
        SavedPaintingController savedPaintingController = new SavedPaintingController(savedPaintingService);

        SecurityController securityController = new SecurityController();

        Route route = new Route(userController, promptWordController, unsplashController,
                savedImageController, noteController, rijksmuseumController, savedPaintingController);

        // 2. Opret Javalin
        Javalin app = Javalin.create(config -> {
            config.router.apiBuilder(route.getRoutes());
        });

        // 2. Tilknyt sikkerhedscheck efter oprettelsen af app
        app.beforeMatched(securityController::authenticate);
        app.beforeMatched(securityController::authorize);


        // Globale exception handling
        app.exception(ApiException.class, (e, ctx) -> {
            ctx.status(e.getCode());
            ctx.json(new MessageDTO(e.getMessage()));
        });

        return app.start(port);
    }

    public static void stopServer(Javalin app) {
        if (app != null) {
            app.stop();
        }
    }
}