package app.services;

import app.config.HibernateConfig;
import app.dao.PaintingDAO;
import app.dtos.savedpaintings.FetchResultDTO;
import app.entities.Painting;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaveFetchedPaintingsServiceTest {

    private static EntityManagerFactory emf;
    private PaintingDAO paintingDAO;
    private SaveFetchedPaintingsService saveService;
    private PaintingsFetcherService fetcherService;

    @BeforeAll
    static void setUpAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
    }

    @BeforeEach
    void setUp() {
        paintingDAO = new PaintingDAO(emf);
        saveService = new SaveFetchedPaintingsService(paintingDAO);

        RijksmuseumService rijksmuseumService = new RijksmuseumService();
        fetcherService = new PaintingsFetcherService(rijksmuseumService);

        // Rydder painting-tabellen i testdatabasen før hver test
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Painting").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Gemmer alle hentede malerier og undgår dubletter ved næste kørsel")
    void saveAllNewPaintings_savesPaintingsAndIgnoresDuplicates() {
        // 1. ACT: Hent malerier fra API'et
        FetchResultDTO fetchResult = fetcherService.fetchAllPaintingsConcurrently();
        assertFalse(fetchResult.getPaintings().isEmpty(), "Der skal være hentet malerier fra API'et");

        // 2. ACT: Gem alle nye malerier i testdatabasen
        int firstSaveCount = saveService.saveAllNewPaintings(fetchResult.getPaintings());

        // 3. ASSERT: Verificer at de blev gemt i databasen
        List<Painting> allInDb = paintingDAO.findAll();
        assertEquals(firstSaveCount, allInDb.size(), "Antal gemte malerier i DB skal matche servicens tæller");
        assertTrue(firstSaveCount > 0, "Der bør være gemt mere end 0 malerier");

        // 4. ACT & ASSERT: Kør gem-metoden igen med samme data og verificer at 0 bliver gemt
        int secondSaveCount = saveService.saveAllNewPaintings(fetchResult.getPaintings());
        assertEquals(0, secondSaveCount, "Dubletter må ikke gemmes anden gang");
    }
}