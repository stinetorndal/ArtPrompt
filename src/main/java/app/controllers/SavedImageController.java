package app.controllers;

import app.dtos.savedimages.SavedImageDTO;
import app.services.SavedImageService;
import io.javalin.http.Context;

import java.util.List;

public class SavedImageController {
    private final SavedImageService savedImageService;

    public SavedImageController(SavedImageService savedImageService) {
        this.savedImageService = savedImageService;
    }

    public void saveImage(Context ctx) {

        SavedImageDTO inputDTO = ctx.bodyAsClass(SavedImageDTO.class);
        SavedImageDTO resultDTO = savedImageService.saveImageForUser(inputDTO);

        ctx.status(201); //201 = created
        ctx.json(resultDTO);
    }

    public void getSavedImagesByUser(Context ctx) {
        Long userId = Long.parseLong(ctx.pathParam("userId"));
        List<SavedImageDTO> imageDTOS = savedImageService.getSavedImagesByUserId(userId);

        ctx.status(200);
        ctx.json(imageDTOS);
    }

}
