package app.runner;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.services.PromptService;
import app.services.UnsplashService;
import app.utils.PromptWordSeeder;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppRunner {
    private UnsplashService unsplashService = new UnsplashService();

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    PromptWordSeeder seeder = new PromptWordSeeder(emf);


    public void run() {
        System.out.println("Seeder databasen med initial data...");
        seeder.seed();
    }

}
