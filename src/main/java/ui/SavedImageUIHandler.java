package ui;

import app.dtos.savedimages.SavedImageDTO;
import app.dtos.user.UserDTO;
import app.exceptions.ApiException;
import app.services.SavedImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class SavedImageUIHandler {
    private static final Logger logger = LoggerFactory.getLogger(SavedImageUIHandler.class);
    private final SavedImageService savedImageService;
    private final Scanner scanner;

    public SavedImageUIHandler(SavedImageService savedImageService, Scanner scanner) {
        this.savedImageService = savedImageService;
        this.scanner = scanner;
    }

    //Vis gemte billeder for indloggede bruger
    public void handleShowSavedImages (UserDTO currentUser) {
        System.out.println("--- DINE GEMTE BILLEDER ---");
        try {
            List<SavedImageDTO> savedImages = savedImageService.getSavedImagesByUserId(currentUser.getId());
            if (savedImages.isEmpty()) {
                System.out.println("Du har ingen gemte billeder endnu.");
            } else {
                for (SavedImageDTO img : savedImages) {
                    System.out.println("- [" + img.getSource() + "] " + img.getTitle() + " (URL: " + img.getUrl() + ")");
                }
            }
        } catch (ApiException e) {
            logger.warn("Fejl ved hentning af gemte billeder for bruger ID {}: {}", currentUser.getId(), e.getMessage());
            System.out.println("FEJL: " + e.getMessage());
        }
    }
}