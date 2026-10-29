package app.services;

import app.dao.UserDAO;
import app.dtos.user.UserDTO;
import app.entities.User;
import app.exceptions.UserAlreadyExistsException;
import app.exceptions.ValidationException;
import app.exceptions.WrongEmailOrPasswordException;
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
        if (!Validator.isEmailValid(userDTO.getEmail())) {
            logger.warn("Forsøg på oprettelse med ugyldig email: {}", userDTO.getEmail());
            throw new ValidationException("Ugyldig emailadresse. Skal indeholde @ og domæne, f.eks. .dk");
        }
        if (!Validator.isPasswordValid(userDTO.getPassword())) {
            logger.warn("Forsøg på oprettelse med ugyldigt password for email: {}", userDTO.getEmail());
            throw new ValidationException("Adgangskode skal være på mindst 8 tegn og indeholde mindst ét stort bogstav, ét tal og ét specialtegn");
        }
        if (userDAO.doesEmailExist(userDTO.getEmail())) {
            logger.warn("Oprettelse afvist. Emailen {} allerede i brug", userDTO.getEmail());
            throw new UserAlreadyExistsException("E-mailen er allerede i brug");

        }
        String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());

        User newUser = new User(userDTO.getEmail(), hashedPassword);
        User savedUser = userDAO.create(newUser);
        logger.info("Bruger er oprettet med email {}", userDTO.getEmail());
        return userMapper.toDTO(savedUser);
    }

    public UserDTO login(UserDTO userDTO) {
        if (!Validator.isEmailValid(userDTO.getEmail())) {
            logger.warn("Emailadressen {} er i forkert format", userDTO.getEmail());
            throw new ValidationException("Ugyldig emailadresse");
        }
        User user = userDAO.findByEmail(userDTO.getEmail());
        if (user == null) {
            logger.warn("Der er indtastet forkert email {} eller password", userDTO.getEmail());
            throw new WrongEmailOrPasswordException("Forkert email eller adgangskode");
        }
        if (!BCrypt.checkpw(userDTO.getPassword(), user.getPassword())) {
            logger.warn("Der er indtastet forkert email {} eller password", userDTO.getEmail());
            throw new WrongEmailOrPasswordException("Forkert email eller adgangskode");
        }
        logger.info("Login lykkedes for email: {}", userDTO.getEmail());
        return userMapper.toDTO(user);
    }
}
