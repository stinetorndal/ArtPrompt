package app.runner;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.dtos.unsplash.PhotoDTO;
import app.entities.Color;
import app.services.PromptService;
import app.services.UnsplashService;
import app.utils.PromptWordSeeder;
import app.views.ConsolePrint;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AppRunner {
    private UnsplashService unsplashService = new UnsplashService();
    private ConsolePrint printer = new ConsolePrint();
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    PromptWordSeeder seeder = new PromptWordSeeder(emf);
    PromptService service = new PromptService(emf);


    public void run() {
        System.out.println("Henter 10 billeder fra Unsplash...\n");
        //Sæt farve her:
        //List<PhotoDTO> photoDTOList =unsplashService.getPicturesWithPickedColor(Color.BLUE);
        //printer.print10RandomPictures(photoDTOList);
        seeder.seed();
    }

}
