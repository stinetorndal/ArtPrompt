package app.controllers;

import app.dtos.unsplash.PhotoDTO;
import app.entities.Color;
import app.exceptions.ApiException;
import app.services.UnsplashService;
import app.utils.RequestUtil;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UnsplashController {
    private static final Logger logger = LoggerFactory.getLogger(RijksmuseumController.class);
    private final UnsplashService unsplashService;

    public UnsplashController(UnsplashService unsplashService) {
        this.unsplashService = unsplashService;
    }

    // GET /api/unsplash/photos?color=RED
    public void getPicturesByColor (Context ctx) {
        Color color = RequestUtil.getEnumQueryParam(ctx, "color", Color.class);

        //Hent billeder fra Unsplash via Service
        List< PhotoDTO> photos = unsplashService.getPicturesWithPickedColor(color);
        ctx.status(200).json(photos);
    }
}
