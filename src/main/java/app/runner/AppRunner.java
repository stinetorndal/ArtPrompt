package app.runner;

import app.dtos.unsplash.PhotoDTO;
import app.entities.Color;
import app.services.UnsplashService;

import java.util.List;

public class AppRunner {
    private UnsplashService unsplashService = new UnsplashService();

    public void run() {
        System.out.println("=== ART PROMPT INSPIRATION ===");
        System.out.println("Henter 10 gule billeder fra Unsplash...\n");
        List<PhotoDTO> photos = unsplashService.getPicturesWithPickedColor(Color.YELLOW);
        System.out.println(photos);



    }
}
