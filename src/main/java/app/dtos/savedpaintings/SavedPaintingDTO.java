package app.dtos.savedpaintings;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
//Denne klasse modtager data for hvert enkelt maleri og trækker de felter ud, jeg skal bruge

public class SavedPaintingDTO {
    private String externalId; //id fra Rijksmuseum. Checker i DB om bruger har maleri gemt
    private String title;
    private String artist;
    private String url;
    private int year;
    private Long userId;
}
