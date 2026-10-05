package ui;

import app.dtos.user.UserDTO;
import app.exceptions.ApiException;
import app.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class UserUIHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserUIHandler.class);
    private final Scanner scanner;
    private final UserService userService;

    public UserUIHandler(Scanner scanner, UserService userService) {
        this.scanner = scanner;
        this.userService = userService;
    }

    public UserDTO handlerRegister () {
        System.out.println("\n--- OPRET BRUGER ---");
        System.out.println("Indtast email: ");
        String email = scanner.nextLine().trim();

        System.out.println("Indtast adgangskode (mindst 8 tegn, 1 stort bogstav, tal og specialtegn):");
        String password = scanner.nextLine().trim();
        try {
            UserDTO newUser = new UserDTO(email, password);
            UserDTO createdUser = userService.createUser(newUser);
            System.out.println("Tillykke. Du er blevet oprettet som bruger med email: " + createdUser.getEmail());
            return createdUser;
        } catch (ApiException e) {
            logger.warn("Fejl ved brugeroprettelse i UI: {}", e.getMessage());
            System.out.println("FEJL: " + e.getMessage());
            return null;
        }
    }

    public UserDTO handlerLogin () {
        System.out.println("\n--- LOG IN ---");
        System.out.println("Indtast email: ");
        String email = scanner.nextLine().trim();

        System.out.println("Indtast adgangskode: ");
        String password = scanner.nextLine().trim();
        try {
            UserDTO loginDTO = new UserDTO(email, password);
            UserDTO loggedInUser = userService.login(loginDTO);
            System.out.println("Du er nu logget ind med email: " + loggedInUser.getEmail());
            return loggedInUser;
        } catch (ApiException e) {
            //logger
            logger.warn("Fejl ved login i UI for email {}: {}", email, e.getMessage());
            System.out.println("FEJL: " + e.getMessage());
            return null;
        }
    }
}
