package app.services;

import app.dtos.savedpaintings.FetchResultDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaintingsFetcherServiceTest {
    private PaintingsFetcherService paintingsFetcherService;

    @BeforeEach
    void setup(){
        RijksmuseumService rijksmuseumService = new RijksmuseumService();
        paintingsFetcherService = new PaintingsFetcherService(rijksmuseumService);
    }

    @Test
    @DisplayName("1. Test om trådene fungerer og at der hentes malerier")
    void testThreadsAndFetchingWorks() {
        // Act
        FetchResultDTO result = paintingsFetcherService.fetchAllPaintingsConcurrently();

        //Assert
        assertNotNull(result, "Resultatet må ikke være null");
        assertNotNull(result.getPaintings(), "Listen af malerier må ikke være null");
        assertFalse(result.getPaintings().isEmpty(), "Der skal hentes mindst et maleri samlet");

        System.out.println("------ TEST 1 SUCCES ------");
        System.out.println("Antal kategorier behandlet i tråde: " + result.getStats().size());
        System.out.println("Første maleri hentet: " + result.getPaintings().get(0).getTitle());
    }

    @Test
    @DisplayName("2. Mål og log hvor hurtigt alt hentes parallelt")
    void testExecutionSpeed() {
        // Act
        FetchResultDTO result =  paintingsFetcherService.fetchAllPaintingsConcurrently();

        // Assert
        assertTrue(result.getTotalDurationMs() > 0, "Tidsforbruger skal være større end 0 ms");

        System.out.println("------ TEST 2: TIDSFORBRUG ------");
        System.out.println("Samlet itdsforbrug for fetching: " + result.getTotalDurationMs() + " ms");

        //Udskriv tid per kunstner-kategori
        result.getStats().forEach(stat ->
                System.out.println("Kunstner: " + stat.getArtist() +
                        " | Tid: " + stat.getDurationMs() + " ms" +
                        " | Antal: " + stat.getCount()));
    }

    @Test
    @DisplayName("3. Check og log hvor mange malerier der hentes i alt")
    void testTotalCountOfPaintings() {
        // Act
        FetchResultDTO result  = paintingsFetcherService.fetchAllPaintingsConcurrently();

        // Assert
        assertEquals(result.getPaintings().size(), result.getTotalPaintings(),
                "Tælleren for totalPaintings skal matche listens faktiske størrelse");

        System.out.println("------ TEST 3: TOTAL ANTAL ------");
        System.out.println("Totalt antal hentede malerier: " + result.getTotalPaintings());
    }
   }

