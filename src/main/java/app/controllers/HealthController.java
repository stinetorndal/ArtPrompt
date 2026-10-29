package app.controllers;

import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

//Holder øje med om applikation og DB er oppe
public class HealthController {
    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);
    private final EntityManagerFactory emf;

    public HealthController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    //Svarer 200 UP hvis DB svarer og 503 DOWN hvis den ikke er
    public void check(Context ctx){
        if (isDatabaseUp()){
            ctx.status(200).json(Map.of("status", "UP"));
        } else {
            ctx.status(503).json(Map.of("status", "DOWN"));
        }
    }
    //Kører query mod DB, returnerer false hvis noget går galt
    private boolean isDatabaseUp() {
        try (EntityManager em = emf.createEntityManager()){
            em.createNativeQuery("SELECT 1").getSingleResult();
            return true;
        } catch (Exception e){
            logger.error("Healthcheck: Databasen svarer ikke: {}", e.getMessage());
            return false;
        }
    }
}
