package app.mappers;

import app.dtos.user.UserDTO;
import app.entities.User;

//Oversætter objekt fra JPA til API og omvendt
public class UserMapper {

    //Hvis bruger ikke findes, går app'en ned med status 500. Nu returnerer den i stedet null
    public UserDTO toDTO (User user) {
        if (user == null){
            return null;
        }
        return new UserDTO(user);
    }

    public User toEntity (UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        return new User(userDTO.getEmail(), userDTO.getPassword());
    }
}
