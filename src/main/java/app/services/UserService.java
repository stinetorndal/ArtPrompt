package app.services;

import app.dao.UserDAO;
import app.dtos.user.UserDTO;
import app.entities.User;
import app.exceptions.ApiException;
import app.mappers.UserMapper;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Denne klasse skal validere brugers email og checke om bruger allerede eksisterer i db
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO;
    private final UserMapper userMapper = new UserMapper();

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserDTO createUser(UserDTO userDTO) {
        //Validér email
        if (!Validator.isEmailValid(userDTO.getEmail())) {
            logger.warn("Forsøg på oprettelse med ugyldig email: {}", userDTO.getEmail());
            throw new ApiException(400, "Ugyldig emailadresse. Skal indeholde @ og domæne, f.eks. .dk");
        }
        //Validér password
        if (!Validator.isPasswordValid(userDTO.getPassword())) {
            logger.warn("Forsøg på oprettelse med for kort password for email: {}", userDTO.getEmail());
            throw new ApiException(400, "Adgangskode skal være på mindst 8 tegn");
        }
        //Er brugernavn allerede i brug i db?
        if (userDAO.doesEmailExist(userDTO.getEmail())) {
            logger.warn("Oprettelse afvist. Emailen {} allerede i brug", userDTO.getEmail());
            throw new ApiException(400, "E-mailen er allerede i brug");

        }
        //Hash med BCrypt
        String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
        //Opret User-objekt med hashede pw
        User newUser = new User(userDTO.getEmail(), hashedPassword);
        User savedUser = userDAO.create(newUser); //Sender til DAO for at gemme
        logger.info("Bruger er oprettet med email {}", userDTO.getEmail());
        return userMapper.toDTO(savedUser);
    }

    public UserDTO login(UserDTO userDTO) {
        //Er email gyldigt format
        if (!Validator.isEmailValid(userDTO.getEmail())) {
            logger.warn("Emailadressen {} er i forkert format", userDTO.getEmail());
            throw new ApiException(400, "Ugyldig emailadresse");
        }
        //Hent bruger - samme fejlbesked længere nede. God sikkerhedsskik
        User user = userDAO.findByEmail(userDTO.getEmail());
        if (user == null) {
            logger.warn("Der er indtastet forkert email {} eller password", userDTO.getEmail());
            throw new ApiException(400, "Forkert email eller adgangskode");
        }
        //Valider password
        if (!BCrypt.checkpw(userDTO.getPassword(), user.getPassword())) {
            logger.warn("Der er indtastet forkert email {} eller password", userDTO.getEmail());
            throw new ApiException(400, "Forkert email eller adgangskode");
        }
        //Returnér bruger hvis alt er ok
        logger.info("Login lykkedes for email: {}", userDTO.getEmail());
        return userMapper.toDTO(user);
    }
}
