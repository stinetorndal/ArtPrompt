package app.services;

import app.dao.SavedImageDAO;
import app.dao.UserDAO;
import app.dtos.savedimages.SavedImageDTO;
import app.entities.SavedImage;
import app.entities.User;
import app.exceptions.ApiException;
import app.mappers.SavedImageMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class SavedImageService {

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
        // 1. Check om bruger findes via UserDAO.findById
        User user = userDAO.findById(dto.getUserId());
        if (user == null) {
            throw new ApiException(404, "Brugeren blev ikke fundet med ID: " + dto.getUserId());
        }
        // 2. Opret entitet ud fra DTO og User
        SavedImage imageToSave = savedImageMapper.toEntity(dto, user);
        //Gem i DB
        SavedImage savedEntity = savedImageDAO.create(imageToSave);
        //Returner som DTO
        return savedImageMapper.toDTO(savedEntity);
    }

    //Henter liste af entiteter fra DAO og mapper dem
    public List<SavedImageDTO> getSavedImagesByUserId (Long userId) {
        List<SavedImage> images = savedImageDAO.findImagesByUserId(userId);
        List<SavedImageDTO> dtos = new ArrayList<>();
        for (SavedImage image : images){
            dtos.add(savedImageMapper.toDTO(image));
        }
        return dtos;
    }
}
