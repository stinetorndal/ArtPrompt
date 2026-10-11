package app.services;

import app.dao.PaintingDAO;
import app.dtos.savedpaintings.SavedPaintingDTO;
import app.entities.Painting;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SaveFetchedPaintingsService {
    private static final Logger logger = LoggerFactory.getLogger(SaveFetchedPaintingsService.class);
    private final PaintingDAO paintingDAO;

    public SaveFetchedPaintingsService(EntityManagerFactory emf) {
        this.paintingDAO = new PaintingDAO(emf);
    }

    // Gemmer de fetchede malerier i DB hvis de ikke findes i forvejen
    public int saveAllNewPaintings (List<SavedPaintingDTO> paintings) {
        int savedCount = 0;

        for (SavedPaintingDTO dto : paintings){
            //Check om maleri eksisterer i DB
            boolean exists = paintingDAO.existsByExternalId(dto.getExternalId());

            if (!exists) {
                Painting painting = new Painting();
                painting.setExternalId(dto.getExternalId());
                painting.setTitle(dto.getTitle());
                painting.setArtist(dto.getArtist());
                painting.setUrl(dto.getUrl());
                painting.setYear(dto.getYear());

                paintingDAO.create(painting);
                savedCount++;
            }
        }
        logger.info("Gemte {} nye malerier i databasen ud af {} hentede", savedCount, paintings.size());
        return savedCount;
    }
}
