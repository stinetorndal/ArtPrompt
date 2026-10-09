package app.controllers;

import app.dtos.savedpaintings.SavedPaintingDTO;
import app.services.SavedPaintingService;
import app.utils.RequestUtil;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SavedPaintingController {

    private static final Logger logger = LoggerFactory.getLogger(SavedPaintingController.class);
    private final SavedPaintingService savedPaintingService;

    public SavedPaintingController(SavedPaintingService savedPaintingService) {
        this.savedPaintingService = savedPaintingService;
    }

    public void savePainting(Context ctx) {

        SavedPaintingDTO inputDTO = ctx.bodyAsClass(SavedPaintingDTO.class);
        SavedPaintingDTO resultDTO = savedPaintingService.savePaintingForUser(inputDTO);

        logger.info("Gemte billede for bruger ID: {}", resultDTO.getUserId());
        ctx.status(201).json(resultDTO);
    }

    public void getSavedPaintingsByUser(Context ctx) {
        Long userId = RequestUtil.getLongPathParam(ctx, "userId");
        List<SavedPaintingDTO> paintingDTOs = savedPaintingService.getSavedPaintingsByUserId(userId);
        logger.info("Hentede {} gemte billeder for bruger ID: {}", paintingDTOs.size(), userId);

        ctx.status(200).json(paintingDTOs);
    }
    //Sat til 5 - eventuelt ændre til 10 i frontend
    public void getRandomPaintings (Context ctx) {
        List<SavedPaintingDTO> randomPaintings =  savedPaintingService.getRandomPaintings(5);
        logger.info("Hentede {} tilfældige malerier fra database", randomPaintings.size());

        ctx.status(200).json(randomPaintings);
    }

}
