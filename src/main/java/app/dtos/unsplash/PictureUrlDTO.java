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
//Denne klasse trækker download-link ud af url-objektet
public class PictureUrlDTO {

    @JsonProperty("regular")
    private String url;
}
