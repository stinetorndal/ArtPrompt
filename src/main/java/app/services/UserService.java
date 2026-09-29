package app.services;

import app.dao.UserDAO;
import app.entities.User;
import app.exceptions.ApiException;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Denne klasse skal validere brugers email og checke om bruger allerede eksisterer i db
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User createUser(String email, String rawPassword) {
        //Validér email
        if (!Validator.isEmailValid(email)) {
            logger.warn("Forsøg på oprettelse med ugyldig email: {}", email);
            throw new ApiException(400, "Ugyldig emailadresse. Skal indeholde @ og domæne, f.eks. .dk");
        }
        //Validér password
        if (!Validator.isPasswordValid(rawPassword)) {
            logger.warn("Forsøg på oprettelse med for kort password for email: {}", email);
            throw new ApiException(400, "Adgangskode skal være på mindst 8 tegn");
        }
        //Er brugernavn allerede i brug i db?
        if (userDAO.doesEmailExist(email)) {
            logger.warn("Oprettelse afvist. Emailen {} allerede i brug", email);
            throw new ApiException(400, "E-mailen er allerede i brug");

        }
        //Hash med BCrypt
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        //Opret User-objekt med hashede pw
        User newUser = new User(email, hashedPassword);
        logger.info("Bruger er oprettet med email {}", email);
        return userDAO.create(newUser);
    }

    public User login(String email, String rawPassword) {
        //Er email gyldigt format
        if (!Validator.isEmailValid(email)) {
            logger.warn("Emailadressen {} er i forkert format", email);
            throw new ApiException(400, "Ugyldig emailadresse");
        }
        //Hent bruger - samme fejlbesked længere nede. God sikkerhedsskik
        User user = userDAO.findByEmail(email);
        if (user == null) {
            logger.warn("Der er indtastet forkert email {} eller password", email);
            throw new ApiException(400, "Forkert email eller adgangskode");
        }
        //Valider password
        if (!BCrypt.checkpw(rawPassword, user.getPassword())) {
            logger.warn("Der er indtastet forkert email {} eller password", email);
            throw new ApiException(400, "Forkert email eller adgangskode");
        }
        //Returnér bruger hvis alt er ok
        logger.info("Login lykkedes for email: {}", email);
        return user;
    }
}
