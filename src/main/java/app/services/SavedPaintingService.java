package app.services;

import app.dao.SavedPaintingDAO;
import app.dao.UserDAO;
import app.dtos.savedpaintings.SavedPaintingDTO;
import app.entities.SavedPainting;
import app.entities.User;
import app.exceptions.ApiException;
import app.mappers.SavedPaintingMapper;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SavedPaintingService {
    private static final Logger logger = LoggerFactory.getLogger(SavedPaintingService.class);
    private final SavedPaintingDAO savedPaintingDAO;
    private final UserDAO userDAO;
    private final SavedPaintingMapper savedPaintingMapper;

    public SavedPaintingService(EntityManagerFactory emf) {
        this.savedPaintingDAO = new SavedPaintingDAO(emf);
        this.userDAO = new UserDAO(emf);
        this.savedPaintingMapper = new SavedPaintingMapper();
    }

    //slår bruger op via dto, opretter entitet, gemmer i DAO, returnerer savedPaintingDTO
    public SavedPaintingDTO savePaintingForUser(SavedPaintingDTO dto) {
        // Check om bruger findes via UserDAO.findById
        User user = userDAO.findById(dto.getUserId());
        if (user == null) {
            logger.warn("Der findes ikke en bruger med det ID {}", dto.getUserId());
            throw new ApiException(404, "Brugeren blev ikke fundet med ID: " + dto.getUserId());
        }

        // Sikkerhedsventil: Check om maleriet allerede er gemt af denne bruger
        boolean alreadySaved = savedPaintingDAO.hasPaintingAlreadyBeenSavedByThisUserId(
                dto.getExternalId(),
                dto.getUserId()
        );
        if (alreadySaved) {
            logger.warn("Forsøg på at gemme dublet; externalId '{}' er allerede gemt for user ID {}",
                    dto.getExternalId(), dto.getUserId());
            throw new ApiException(400, "Dette maleri er allerede gemt på din liste.");
        }
        // Opret entitet ud fra DTO og User
        SavedPainting paintingToSave = savedPaintingMapper.toEntity(dto, user);

        //Gem i DB
        SavedPainting savedEntity = savedPaintingDAO.create(paintingToSave);
        //Returner som DTO
        return savedPaintingMapper.toDTO(savedEntity);
    }

    //Henter liste af entiteter fra DAO og mapper dem
    public List<SavedPaintingDTO> getSavedPaintingsByUserId(Long userId) {
        List<SavedPainting> paintings = savedPaintingDAO.findPaintingsByUserId(userId);
        List<SavedPaintingDTO> dtos = new ArrayList<>();
        for (SavedPainting painting : paintings) {
            dtos.add(savedPaintingMapper.toDTO(painting));
        }
        return dtos;
    }

    // Henter et tilfældigt udvalg af gemte malerier fra databasen
    public List<SavedPaintingDTO> getRandomPaintings(int limit) {
        List<SavedPainting> paintings = new ArrayList<>(savedPaintingDAO.findAll());
        Collections.shuffle(paintings);

        List<SavedPaintingDTO> dtos = new ArrayList<>();
        int count = Math.min(limit, paintings.size());

        for (int i = 0; i < count; i++) {
            dtos.add(savedPaintingMapper.toDTO(paintings.get(i)));
        }

        logger.info("Returnerede {} tilfældige malerier fra databasen", dtos.size());
        return dtos;
    }
}


