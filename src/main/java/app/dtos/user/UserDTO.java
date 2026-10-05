package app.dtos.user;

import app.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
//Denne klasse bruges til login og registrering af User
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    // Konstruktør til oprettelse/login (uden id endnu)
    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Hjælpekonstruktør fra User-entitet (inkl. id)
    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
    }
}



