package app.controllers;

import app.dtos.savedimages.SavedImageDTO;
import app.services.SavedImageService;
import app.utils.RequestUtil;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SavedImageController {
    private static final Logger logger = LoggerFactory.getLogger(SavedImageController.class);
    private final SavedImageService savedImageService;

    public SavedImageController(SavedImageService savedImageService) {
        this.savedImageService = savedImageService;
    }

    public void saveImage(Context ctx) {

        SavedImageDTO inputDTO = ctx.bodyAsClass(SavedImageDTO.class);
        SavedImageDTO resultDTO = savedImageService.saveImageForUser(inputDTO);

        logger.info("Gemte billede for bruger ID: {}", resultDTO.getUserId());
        ctx.status(201).json(resultDTO);
    }

    public void getSavedImagesByUser(Context ctx) {
        Long userId = RequestUtil.getLongPathParam(ctx, "userId");
        List<SavedImageDTO> imageDTOs = savedImageService.getSavedImagesByUserId(userId);
        logger.info("Hentede {} gemte billeder for bruger ID: {}", imageDTOs.size(), userId);

        ctx.status(200).json(imageDTOs);
    }

}
