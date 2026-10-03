package app.controllers;

import app.dtos.unsplash.PhotoDTO;
import app.entities.Color;
import app.exceptions.ApiException;
import app.services.UnsplashService;
import io.javalin.http.Context;

import java.util.List;

public class UnsplashController {
    private final UnsplashService unsplashService;

    public UnsplashController(UnsplashService unsplashService) {
        this.unsplashService = unsplashService;
    }
    public void getPicturesByColor (Context ctx) {
        String colorParam = ctx.queryParam("color");
        if(colorParam == null || colorParam.trim().isEmpty()) {
            throw new ApiException(400, "Vælg venligst en farve som parameter");
        }
        //Konverter til ENUM
        Color colorEnum;
        try {
            //Her fanges IllegalArgumentException
            colorEnum = Color.valueOf(colorParam.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Ugyldig farve angivet. Tjek at farven findes");
        }
        //Hent billeder fra Unsplash via Service
        List< PhotoDTO> photos = unsplashService.getPicturesWithPickedColor(colorEnum);
        ctx.status(200).json(photos);
    }
}
