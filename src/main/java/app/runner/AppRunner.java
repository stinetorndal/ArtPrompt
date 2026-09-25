package app.runner;

import app.dtos.unsplash.PhotoDTO;
import app.entities.Color;
import app.services.UnsplashService;
import app.views.ConsolePrint;

import java.util.List;

public class AppRunner {
    private UnsplashService unsplashService = new UnsplashService();
    private ConsolePrint printer = new ConsolePrint();

    public void run() {
        System.out.println("Henter 10 billeder fra Unsplash...\n");
        //Sæt farve her!!!
        List<PhotoDTO> photoDTOList =unsplashService.getPicturesWithPickedColor(Color.BLUE);
        printer.print10RandomPictures(photoDTOList);
    }
}
