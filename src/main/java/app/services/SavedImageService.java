package app.services;

import app.dao.SavedImageDAO;
import app.dao.UserDAO;
import app.dtos.savedimages.SavedImageDTO;
import app.entities.SavedImage;
import app.entities.User;
import app.exceptions.ApiException;
import app.mappers.SavedImageMapper;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//Denne klasse gemmer billeder fra Unsplash for bruger
public class SavedImageService {
    private static final Logger logger = LoggerFactory.getLogger(SavedImageService.class);
    private final SavedImageDAO savedImageDAO;
    private final UserDAO userDAO;
    private final SavedImageMapper savedImageMapper;

    public SavedImageService(EntityManagerFactory emf) {
        this.savedImageDAO = new SavedImageDAO(emf);
        this.userDAO = new UserDAO(emf);
        this.savedImageMapper = new SavedImageMapper();
    }

    //slår bruger op via dto, opretter entitet, gemmer i DAO, returnerer savedImageDTO
    public SavedImageDTO saveImageForUser(SavedImageDTO dto) {
        User user = userDAO.findById(dto.getUserId());
        if (user == null) {
            throw new ApiException(404, "Brugeren blev ikke fundet med ID: " + dto.getUserId());
        }

        SavedImage imageToSave = savedImageMapper.toEntity(dto, user);

        boolean alreadySaved = savedImageDAO.hasImageAlreadyBeenSavedByThisUserId(
                dto.getExternalId(),
                dto.getUserId()
        );
        if (alreadySaved) {
            logger.warn("Forsøg på at gemme dublet; externalId '{}' er allerede gemt for user ID {}",
                    dto.getExternalId(), dto.getUserId());
            throw new ApiException(400, "Dette maleri er allerede gemt på din liste.");
        }
        SavedImage savedEntity = savedImageDAO.create(imageToSave);
        return savedImageMapper.toDTO(savedEntity);
    }


    //Henter liste af entiteter fra DAO og mapper dem
    public List<SavedImageDTO> getSavedImagesByUserId(Long userId) {
        List<SavedImage> images = savedImageDAO.findImagesByUserId(userId);
        List<SavedImageDTO> dtos = new ArrayList<>();
        for (SavedImage image : images) {
            dtos.add(savedImageMapper.toDTO(image));
        }
        return dtos;
    }
}
