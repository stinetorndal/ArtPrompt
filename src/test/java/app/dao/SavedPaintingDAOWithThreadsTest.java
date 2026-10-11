package app.dao;

import app.config.HibernateConfig;
import app.dtos.savedpaintings.FetchResultDTO;
import app.dtos.savedpaintings.SavedPaintingDTO;
import app.entities.SavedPainting;
import app.services.PaintingsFetcherService;
import app.services.RijksmuseumService;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SavedPaintingDAOTest {

    private static EntityManagerFactory emf;
    private SavedPaintingDAO savedPaintingDAO;
    private PaintingsFetcherService fetcherService;

    @BeforeAll
    static void setUpAll() {
        // Forbinder til artprompt_test
        emf = HibernateConfig.getEntityManagerFactoryForTest();
    }

    @BeforeEach
    void setUp() {
        savedPaintingDAO = new SavedPaintingDAO(emf);
        RijksmuseumService rijksmuseumService = new RijksmuseumService();
        fetcherService = new PaintingsFetcherService(rijksmuseumService);
    }

    @Test
    @DisplayName("Gem et hentet maleri fra fetcher servicen i testdatabasen")
    void testSaveFetchedPaintingToDatabase() {
        // 1. ACT: Hent malerierne i parallel via servicen
        FetchResultDTO result = fetcherService.fetchAllPaintingsConcurrently();
        assertFalse(result.getPaintings().isEmpty(), "Der skal være hentet malerier");

        // 2. Vælg det første DTO
        SavedPaintingDTO dto = result.getPaintings().get(0);

        // 3. Konverter DTO til SavedPainting entitet
        SavedPainting entity = new SavedPainting();
        entity.setExternalId(dto.getExternalId());
        entity.setTitle(dto.getTitle());
        entity.setArtist(dto.getArtist());
        entity.setUrl(dto.getUrl());
        entity.setYear(dto.getYear());

        // 4. ACT: Gem entiteten via DAO
        SavedPainting createdEntity = savedPaintingDAO.create(entity);

        // 5. ASSERT: Tjek at databasen har tildelt et ID
        assertNotNull(createdEntity, "Den gemte entitet må ikke være null");
        assertNotNull(createdEntity.getId(), "Databasen skal generere et id (PK)");

        System.out.println("------ DAO TEST SUCCES ------");
        System.out.println("Gemte maleri i DB med ID: " + createdEntity.getId());
        System.out.println("Titel: " + createdEntity.getTitle());
        System.out.println("Kunstner: " + createdEntity.getArtist());
    }
}