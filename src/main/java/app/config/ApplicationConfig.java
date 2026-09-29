package app.config;

import app.dtos.MessageDTO;
import app.exceptions.ApiException;
import io.javalin.Javalin;

//Konfigurerer og opretter Javalin-server + starter den  og returnerer app-instans
public class ApplicationConfig {

    public static Javalin startServer(int port) {
        Javalin app = Javalin.create(config -> {
            // Javalin konfiguration
        });

        //TODO til brug i test. SLET
        app.get("/api/v1/test-error", ctx -> {
            throw new ApiException(404, "Prompt ikke fundet");
        });

        // Vores globale exception handling
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