// griber produced_by. I Rijksmuseum ligger info pakket ind i objekter der er pakket ind i 0bjekter
package app.dtos.rijksmuseum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductionDTO {

    @JsonProperty("timespan")
    private TimeSpanDTO timespan;
}