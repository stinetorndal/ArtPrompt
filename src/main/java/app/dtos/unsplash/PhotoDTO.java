package app.dtos.unsplash;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
//Denne klasse modtager data for hvert enkelt billede og trækker de felter ud, jeg skal bruge
public class PhotoDTO {

    private String id; //Billedets eksterne id fra Unsplash. Skal bruges til check i db, om bruger allerede har billede

    @JsonProperty("user")
    private UserDTO user;

    @JsonProperty("urls")
    private PictureUrlDTO url;
    //Jeg vil gerne hente ti billeder ud fra en farvesøgning

}
