package app.views;

import app.dtos.unsplash.PhotoDTO;
import app.entities.Color;
import app.services.UnsplashService;

import java.util.List;

public class ConsolePrint {
    UnsplashService unsplashService = new UnsplashService();

    public void print10RandomPictures(List<PhotoDTO> photoDTO) {
        if (photoDTO == null) {
            System.out.println("Der er ingen billeder at vise");
            return;
        }
        System.out.println("======= BILLEDE-OVERSIGT ======");
        int count = 1;
        for (PhotoDTO photo : photoDTO) {
            System.out.println("Navn på fotograf: " + photo.getUser());
            System.out.println("Id på billede: " + photo.getId());
            System.out.println("Url på billede: " + photo.getUrl());
            System.out.println("===============================");
        }
    }
}