package app.services;

import app.dtos.savedpaintings.FetchPaintingsTimeStatsDTO;
import app.dtos.savedpaintings.FetchResultDTO;
import app.dtos.savedpaintings.SavedPaintingDTO;
import app.entities.ArtistCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

//Denne klasser håndterer threads ift fetching fra Rijksmuseum
public class PaintingsFetcherService {

    public static final int DEFAULT_THREADS = 4; //bruges i Controller
    public static final int MAX_THREADS = 8; //fx 20 giver risiko for blokering
    private final RijksmuseumService rijksmuseumService;
    private static final Logger logger = LoggerFactory.getLogger(PaintingsFetcherService.class);

    public PaintingsFetcherService(RijksmuseumService rijksmuseumService) {
        this.rijksmuseumService = rijksmuseumService;
    }

    //Hovedmetode - måler samlet tid og binder det sammen
    public FetchResultDTO fetchAllPaintingsConcurrently () {
        return fetchAllPaintingsConcurrently(DEFAULT_THREADS);
    }

    public FetchResultDTO fetchAllPaintingsConcurrently (int threadCount) {
        long start = System.currentTimeMillis();

        List<Callable<ArtistFetchResult>> tasks = createTasks();
        List<ArtistFetchResult> results = runInParallel(tasks, threadCount);

        long totalDuration = System.currentTimeMillis() -start;
        logger.info("Færdig med parallel hentning på {} ms", threadCount, totalDuration);
        return buildResults(results, totalDuration, threadCount);
    }
    //Opretter en Callable-opgave for hver kategori i enummet via for-løkke (isf streams)
        private List<Callable<ArtistFetchResult>> createTasks() {
        List<Callable<ArtistFetchResult>> tasks  =new ArrayList<>();
        for (ArtistCategory category: ArtistCategory.values()) {
            tasks.add(() -> fetchOneArtist(category));
        }
        return tasks;
    }

    //ENKELT trådopgave: Måler tid for EN specifik kunstner
    //Try catch indsat fordi hele fetching fejler, hvis en kunstner fejler
    private ArtistFetchResult fetchOneArtist(ArtistCategory category) {
        long start = System.currentTimeMillis();
        try {
            List<SavedPaintingDTO> paintings = rijksmuseumService.fetchPaintingsByCategory(category);
            long duration = System.currentTimeMillis() - start;
            logger.info("Hentede {} malerier for {} på {} ms", paintings.size(), category.name(), duration);

            FetchPaintingsTimeStatsDTO stat = new FetchPaintingsTimeStatsDTO(category.name(), duration, paintings.size());
            return new ArtistFetchResult(stat, paintings);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            logger.error("Kunne ikke hente malerier for {}: {}", category.name(), e.getMessage());
            FetchPaintingsTimeStatsDTO stat = new FetchPaintingsTimeStatsDTO(category.name(), duration, 0);
            return new ArtistFetchResult(stat, List.of());
        }
    }


    //PARALLEL KØRSEL: Tildeler opgaver til trådpuljen (ExecutorService)
    private List<ArtistFetchResult> runInParallel(List<Callable<ArtistFetchResult>> tasks, int threadcount) {
        int poolSize = Math.min(threadcount, tasks.size()); //Her hentes de fire tråde
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        try {
            List<ArtistFetchResult> results = new ArrayList<>();
            for (Future<ArtistFetchResult> future : executor.invokeAll(tasks)) {
                results.add(future.get());
            }
            return results;
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            logger.error("Fejl under parallel hentning af malerier", e);
            throw new RuntimeException("Fejl under parallel hentning af malerier", e);
        } finally {
            executor.shutdown();
        }
    }

    // SAMLER RESULTATET: Bygger DTO'en ud fra listen af resultater
    private FetchResultDTO buildResults(List<ArtistFetchResult> results, long totalDuration, int threadCount) {
        List<FetchPaintingsTimeStatsDTO> stats = new ArrayList<>();
        List<SavedPaintingDTO> allPaintings = new ArrayList<>();
                for (ArtistFetchResult result : results) {
                    stats.add(result.stat);
                    allPaintings.addAll(result.paintings());
                }
        return new FetchResultDTO(totalDuration,threadCount, allPaintings.size(), stats, allPaintings);
    }

    //Intern record til overførsel fra enkelt tråd
    private record ArtistFetchResult(FetchPaintingsTimeStatsDTO stat, List<SavedPaintingDTO> paintings) {}
}

