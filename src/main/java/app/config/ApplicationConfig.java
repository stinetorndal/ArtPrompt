package app.config;

import app.controllers.UserController;
import app.dao.UserDAO;
import app.dtos.MessageDTO;
import app.exceptions.ApiException;
import app.routes.Route;
import app.services.UserService;
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
        Route route = new Route(userController);

        // 2. Opret Javalin
        Javalin app = Javalin.create(config -> {
            config.router.apiBuilder(route.getRoutes());
        });


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