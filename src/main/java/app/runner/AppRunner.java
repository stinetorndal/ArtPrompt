package app.runner;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.dtos.rijksmuseum.PaintingDTO;
import app.entities.ArtistCategory;
import app.services.RijksmuseumService;
import app.services.UnsplashService;
import app.utils.PromptWordSeeder;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AppRunner {
    private UnsplashService unsplashService = new UnsplashService();
    private RijksmuseumService service = new RijksmuseumService();

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    PromptWordSeeder seeder = new PromptWordSeeder(emf);


    public void run() {
        System.out.println("Seeder databasen med initial data...");
        seeder.seed();


        System.out.println("=== TEST 1: Henter ID'er for Rembrandt ===");
        ArtistCategory testArtist = ArtistCategory.REMBRANDT;

        List<String> ids = service.fetchPaintingsIdsByArtist(testArtist);
        System.out.println("Fandt " + ids.size() + " ID'er.");

        if (!ids.isEmpty()) {
            String firstId = ids.get(0);
            System.out.println("\n=== TEST 2: Henter detaljer for første ID (" + firstId + ") ===");

            PaintingDTO details = service.fetchPaintingDetailsById(firstId);

            if (details != null) {
                System.out.println("External ID: " + details.getId());
                System.out.println("Titel: " + details.getTitle());
                System.out.println("Dato: " + details.getDateCreated());

            }
        }
    }
}