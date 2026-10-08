package app.controllers;

import app.dtos.savedpaintings.SavedPaintingDTO;
import app.entities.ArtistCategory;
import app.mappers.SavedPaintingMapper;
import app.services.RijksmuseumService;
import app.utils.RequestUtil;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class RijksmuseumController {

    private static final Logger logger = LoggerFactory.getLogger(RijksmuseumController.class);
    private static final int MAX_PAINTINGS = 5;

    private final RijksmuseumService rijksmuseumService;
    private final SavedPaintingMapper savedPaintingMapper;

    public RijksmuseumController(RijksmuseumService rijksmuseumService, SavedPaintingMapper savedPaintingMapper) {
        this.rijksmuseumService = rijksmuseumService;
        this.savedPaintingMapper = savedPaintingMapper;
    }

    // GET /api/v1/rijksmuseum/artist?artist=REMBRANDT
    public void getPaintingsByArtist(Context ctx) {
        ArtistCategory category = RequestUtil.getEnumQueryParam(ctx, "artist", ArtistCategory.class);

        List<String> ids = rijksmuseumService.fetchPaintingsIdsByArtist(category);
        logger.info("Fandt {} ID'er hos Rijksmuseum for kunstneren {}", ids.size(), category.name());

        List<SavedPaintingDTO> resultList = ids.stream()
                .limit(MAX_PAINTINGS)
                .map(rijksmuseumService::fetchPaintingDetailsById)
                .filter(Objects::nonNull)
                .map(dto -> savedPaintingMapper.fromApiDTO(dto, category))
                .toList();

        logger.info("Returnerer {} malerier for kunstneren {}", resultList.size(), category.name());
        ctx.status(200).json(resultList);
    }
}