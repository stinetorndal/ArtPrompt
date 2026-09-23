package app.dtos.unsplash;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
//Denne klasse trækker fotograf-navn ud af Json-objekt
public class UserDTO {

    //Fotografens navn
    private String name;
}
