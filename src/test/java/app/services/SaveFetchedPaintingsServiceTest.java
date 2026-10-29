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
        saveService = new SaveFetchedPaintingsService(emf);

        RijksmuseumService rijksmuseumService = new RijksmuseumService();
        fetcherService = new PaintingsFetcherService(rijksmuseumService);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Painting").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Gemmer alle hentede malerier og undgår dubletter ved næste kørsel")
    void saveAllNewPaintings_savesPaintingsAndIgnoresDuplicates() {
        //Act
        FetchResultDTO fetchResult = fetcherService.fetchAllPaintingsConcurrently();
        assertFalse(fetchResult.getPaintings().isEmpty(), "Der skal være hentet malerier fra API'et");

        int firstSaveCount = saveService.saveAllNewPaintings(fetchResult.getPaintings());

        //Assert
        List<Painting> allInDb = paintingDAO.findAll();
        assertEquals(firstSaveCount, allInDb.size(), "Antal gemte malerier i DB skal matche servicens tæller");
        assertTrue(firstSaveCount > 0, "Der bør være gemt mere end 0 malerier");

        int secondSaveCount = saveService.saveAllNewPaintings(fetchResult.getPaintings());
        assertEquals(0, secondSaveCount, "Dubletter må ikke gemmes anden gang");
    }
}