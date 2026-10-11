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

public class PaintingsFetcherService {

    private final RijksmuseumService rijksmuseumService;
    private static final Logger logger = LoggerFactory.getLogger(PaintingsFetcherService.class);

    public PaintingsFetcherService(RijksmuseumService rijksmuseumService) {
        this.rijksmuseumService = rijksmuseumService;
    }

    //1. Hovedmetode - måler samlet tid og binder det sammen
    public FetchResultDTO fetchAllPaintingsConcurrently (){
        long start = System.currentTimeMillis();

        List<Callable<ArtistFetchResult>> tasks = createTasks();
        List<ArtistFetchResult> results = runInParallel(tasks);

        long totalDuration = System.currentTimeMillis() -start;
        logger.info("Færdig med parallel hentning på {} ms", totalDuration);
        return buildResults(results, System.currentTimeMillis() - start);
    }
    //2. Opretter en Callable-opgave for hver kategori i enummet via for-løkke (isf streams)
        private List<Callable<ArtistFetchResult>> createTasks() {
        List<Callable<ArtistFetchResult>> tasks  =new ArrayList<>();
        for (ArtistCategory category: ArtistCategory.values()) {
            tasks.add(() -> fetchOneArtist(category));
        }
        return tasks;
    }

    // 3. ENKELT trådopgave: Måler tid for EN specifik kunstner
    private ArtistFetchResult fetchOneArtist(ArtistCategory category) {
        long start = System.currentTimeMillis();
        List<SavedPaintingDTO> paintings =  rijksmuseumService.fetchPaintingsByCategory(category);
        long duration  = System.currentTimeMillis() - start;
        logger.info("Hentede {} malerier for {} på {} ms", paintings.size(), category.name(), duration);

        FetchPaintingsTimeStatsDTO stat = new FetchPaintingsTimeStatsDTO(category.name(), duration, paintings.size());
        return new ArtistFetchResult(stat, paintings);
    }


    // 4. PARALLEL KØRSEL: Tildeler opgaver til trådpuljen (ExecutorService)
    private List<ArtistFetchResult> runInParallel(List<Callable<ArtistFetchResult>> tasks) {
        ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
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

    // 5. SAMLER RESULTATET: Bygger DTO'en ud fra listen af resultater
    private FetchResultDTO buildResults(List<ArtistFetchResult> results, long totalDuration) {
        List<FetchPaintingsTimeStatsDTO> stats = new ArrayList<>();
        List<SavedPaintingDTO> allPaintings = new ArrayList<>();
                for (ArtistFetchResult result : results) {
                    stats.add(result.stat);
                    allPaintings.addAll(result.paintings());
                }
        return new FetchResultDTO(totalDuration, allPaintings.size(), stats, allPaintings);
    }

    //Intern record  til overførsel fra enkelt tråd
    private record ArtistFetchResult(FetchPaintingsTimeStatsDTO stat, List<SavedPaintingDTO> paintings) {}
}

