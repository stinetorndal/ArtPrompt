package app.mappers;

import app.dtos.user.UserDTO;
import app.entities.User;

//Denne klasse har til opgave en konverte entitet <-> DTO
public class UserMapper {

    //Fra entitet til DTO:
    //Hvis bruger ikke findes, går app'en ned med status 500. Nu returnerer den i stedet null
    public UserDTO toDTO (User user) {
        if (user == null){
            return null;
        }
        return new UserDTO(user.getEmail(), null); //null for ikke at sende password med som json
    }

    //Fra DTO til entitet:
    public User toEntity (UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        return new User(userDTO.getEmail(), null);
    }
}
