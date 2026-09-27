package app.services;

import app.dao.UserDAO;
import app.entities.User;
import org.mindrot.jbcrypt.BCrypt;

//Denne klasse skal validere brugers email og checke om bruger allerede eksisterer i db
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User createUser(String email, String rawPassword) {
        //Validér email
        if (!Validator.isEmailValid(email)) {
            throw new IllegalArgumentException("Ugyldig emailadresse. Skal indeholde @ og domæne, f.eks. .dk");
        }
        //Validér password
        if (!Validator.isPasswordValid(rawPassword)) {
            throw new IllegalArgumentException("Adgangskode skal være på mindst 8 tegn");
        }
        //Er brugernavn allerede i brug i db?
        if (userDAO.doesEmailExist(email)) {
            throw new IllegalArgumentException("E-mailen er allerede i brug");
        }
        //Hash med BCrypt
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        //Opret User-objekt med hashede pw
        User newUser = new User(email, hashedPassword);
        return userDAO.create(newUser);
    }

    public User login(String email, String rawPassword) {
        //Er email gyldigt format
        if (!Validator.isEmailValid(email)) {
            throw new IllegalArgumentException("Ugyldig emailadresse");
        }
        //Hent bruger - samme fejlbesked længere nede. God sikkerhedsskik
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Forkert email eller adgangskode");
        }
        //Valider password
        if (!BCrypt.checkpw(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Forkert email eller adgangskode");
        }
        //Returnér bruger hvis alt er ok
        return user;
    }
}
