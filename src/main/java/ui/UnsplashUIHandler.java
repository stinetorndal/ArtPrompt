package ui;

import app.dtos.unsplash.PhotoDTO;
import app.entities.Color;
import app.exceptions.ApiException;
import app.services.UnsplashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class UnsplashUIHandler {
    private final Logger logger = LoggerFactory.getLogger(UnsplashUIHandler.class);
    private final UnsplashService unsplashService;
    private final Scanner scanner;

    public UnsplashUIHandler(UnsplashService unsplashService, Scanner scanner) {
        this.unsplashService = unsplashService;
        this.scanner = scanner;
    }

    public List<PhotoDTO> handlePickColorAndFetchImages(){
        Color[] colors = Color.values();

        System.out.println("\n--- VÆLG EN FARVE TIL BILLEDE-PROMPTS ---");
        for (int i = 0; i < colors.length; i++) {
            System.out.println((i + 1) + ". " + colors[i]);
        }
        System.out.println("\nIndtast nummer eller navn på ønsket farve: ");
        Color color = parseColor(scanner.nextLine().trim(), colors);
        if (color == null){
            System.out.println("Ugyldigt valg, prøv venligst igen");
            return null;
        }
        System.out.println("\nHenter billeder fra Unsplash med farven " + color);
        try {
            List<PhotoDTO> photos = unsplashService.getPicturesWithPickedColor(color);
            if (photos.isEmpty()){
                System.out.println("Ingen billeder fundet for farven: " + color);
            } else {
                printPhotos(photos);
            }
            return photos;
            }  catch (ApiException e) {
            logger.warn("Fejl ved hentning af Unsplash-billeder: {}", e.getMessage());
            System.out.println("FEJL: " + e.getMessage());
            return null;
        }
    }

    private void printPhotos(List<PhotoDTO> photos) {
        System.out.println("======= BILLEDE-OVERSIGT ======");
        for (PhotoDTO photo : photos) {
            System.out.println("Fotograf: " + photo.getUser());
            System.out.println("Id: " + photo.getId());
            System.out.println("Url: " + photo.getUrl());
            System.out.println("===============================");
        }
    }

    private Color parseColor(String input, Color[] colors) {
        //Hvis input.matches er true, har man indtastet tal og der smides ikke exception
        if (input.matches("\\d+")) { //\d betyder et ciffer, + betyder et eller flere
            int index = Integer.parseInt(input) - 1;
            return index >= 0 && index < colors.length ? colors[index] : null;
        }
        for (Color c : colors) {
            if (c.name().equalsIgnoreCase(input)) return c;
        }
        return null;
    }
}
