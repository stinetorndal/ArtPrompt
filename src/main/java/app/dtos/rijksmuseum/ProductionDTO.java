// griber produced_by. I Rijksmuseum ligger info pakket ind i objekter der er pakket ind i 0bjekter
package app.dtos.rijksmuseum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductionDTO {

    @JsonProperty("timespan")
    //Ændret fordi få malerier sendes som liste af objekter, da de har flere årstal for maleriets udførelse
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY) //Et objekt = jackson pakker det ind som liste af objekter
    private List<TimeSpanDTO> timespan;
}