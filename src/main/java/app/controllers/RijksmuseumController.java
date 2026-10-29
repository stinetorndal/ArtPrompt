package app.controllers;

import app.dtos.savedpaintings.FetchResultDTO;
import app.dtos.savedpaintings.SavedPaintingDTO;
import app.entities.ArtistCategory;
import app.mappers.SavedPaintingMapper;
import app.services.PaintingsFetcherService;
import app.services.RijksmuseumService;
import app.services.SaveFetchedPaintingsService;
import app.utils.Utils;
import app.utils.RequestUtil;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import app.exceptions.ApiException;

import java.util.concurrent.atomic.AtomicBoolean;

import java.util.List;
import java.util.Objects;

//Denne klasse tager sig af henvendelser fra bruger , der handler om malerier fra API Rijksmuseum
public class RijksmuseumController {

    private static final Logger logger = LoggerFactory.getLogger(RijksmuseumController.class);
    private final AtomicBoolean fetchRunning = new AtomicBoolean(false);
    private static final int MAX_PAINTINGS = 5;

    private final RijksmuseumService rijksmuseumService;
    private final SavedPaintingMapper savedPaintingMapper;
    private final PaintingsFetcherService paintingsFetcherService;
    private final SaveFetchedPaintingsService saveFetchedPaintingsService;


    public RijksmuseumController(RijksmuseumService rijksmuseumService,
                                 SavedPaintingMapper savedPaintingMapper,
                                 PaintingsFetcherService paintingsFetcherService,
                                 SaveFetchedPaintingsService saveFetchedPaintingsService) {
        this.rijksmuseumService = rijksmuseumService;
        this.savedPaintingMapper = savedPaintingMapper;
        this.paintingsFetcherService = paintingsFetcherService;
        this.saveFetchedPaintingsService = saveFetchedPaintingsService;
    }

    // GET /api/v1/rijksmuseum/artist?artist=REMBRANDT ... eller =HALS
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

    // GET /api/v1/rijksmuseum/fetch-all?threads=6
    //Order: enabled? -> valid thread count? -> take the lock -> fetch -> release the lock
    //Der kører kun et kald af gangen tl Rijksmuseum. Endnu et kald giver HTTP 409.
    public void fetchAllConcurrently(Context ctx) {
        ensureFetchIsEnabled();
        int threadCount = readThreadCount(ctx);
        claimFetchSlot();
        try {
            runFetchAndSave(ctx, threadCount);
        } finally {
            fetchRunning.set(false);
            //frigør selv om fetch fejler så kald kan køre igen. SKAL ligge i finally ellers kan man ikke fetche igen
        }
    }
    //læser threads fra url. Hvis dt mangler, returnerer den DEFAULT = 4
    private int readThreadCount(Context ctx) {
        String raw = ctx.queryParam("threads");
        if (raw == null || raw.isBlank()) {
            return PaintingsFetcherService.DEFAULT_THREADS;
        }
        int threadCount = parseThreadCount(raw.trim());
        if(threadCount < 1 || threadCount > PaintingsFetcherService.MAX_THREADS){
            throw new ApiException(400, "threads must be between 1 and " + PaintingsFetcherService.MAX_THREADS);
        }
        return threadCount;
    }
    //Laver tekst til tal, parser fx abc til fejl 400
    private int parseThreadCount(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Threads must be a whole number, for example 2 or 4");
        }
    }

    //Virker fetching
    private void runFetchAndSave(Context ctx, int threadCount) {
        FetchResultDTO result = paintingsFetcherService.fetchAllPaintingsConcurrently(threadCount);
        int savedCount = saveFetchedPaintingsService.saveAllNewPaintings(result.getPaintings());
        logger.info("Gemte {} malerier i kataloget under fecthAllConcurrently", savedCount);
        ctx.status(200).json(result);
    }

    //Sikrer at kun en tråd af gangen kan starte en fetch-handling. Hvis fetching er i gang, logges advarsel
    private void claimFetchSlot() {
        if (!fetchRunning.compareAndSet(false, true)) {
            logger.warn("Fetch rejected: A fetch is already running");
            throw new ApiException(409, "A fetch is already running. Try again when it has finished");
        }
    }

    //Checker om fetching er slået fra i env
    private void ensureFetchIsEnabled() {
        if (!isFetchEnabled()) {
            logger.warn("Fetch rejected. FETCH_ENABLED is false");
            throw new ApiException(503, "Fetching is currently disabled");
        }
    }
    //Fortæller hvor miljøvariable skal læses fra. False = disabled. True eller ingenting = enabled
    private boolean isFetchEnabled() {
        try{
            String values = Utils.getPropertyValue("FETCH_ENABLED", "config.properties");
            return !values.equalsIgnoreCase("false");
        } catch (ApiException e) {
            return true;
        }
    }
}