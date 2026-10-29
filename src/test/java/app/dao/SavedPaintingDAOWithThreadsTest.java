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
        // Act
        FetchResultDTO result = fetcherService.fetchAllPaintingsConcurrently();
        assertFalse(result.getPaintings().isEmpty(), "Der skal være hentet malerier");

        SavedPaintingDTO dto = result.getPaintings().get(0);

        SavedPainting entity = new SavedPainting();
        entity.setExternalId(dto.getExternalId());
        entity.setTitle(dto.getTitle());
        entity.setArtist(dto.getArtist());
        entity.setUrl(dto.getUrl());
        entity.setYear(dto.getYear());

        //Act
        SavedPainting createdEntity = savedPaintingDAO.create(entity);

        //Assert
        assertNotNull(createdEntity, "Den gemte entitet må ikke være null");
        assertNotNull(createdEntity.getId(), "Databasen skal generere et id (PK)");

        System.out.println("------ DAO TEST SUCCES ------");
        System.out.println("Gemte maleri i DB med ID: " + createdEntity.getId());
        System.out.println("Titel: " + createdEntity.getTitle());
        System.out.println("Kunstner: " + createdEntity.getArtist());
    }
}