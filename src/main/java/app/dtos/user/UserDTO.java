package app.dtos.user;

import app.entities.User;
import app.routes.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Long id;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // læses fra input, men sendes aldrig ud
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // sendes ud, men ignoreres fra input
    private Role role;

    // Til oprettelse/login (uden id og rolle endnu)
    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Fra User-entitet (inkl. id og rolle)
    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}