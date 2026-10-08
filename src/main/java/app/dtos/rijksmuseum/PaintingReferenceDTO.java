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
// Modtager id-url på det enkelte maleri fra søgningen
public class PaintingReferenceDTO {

    @JsonProperty("id")
    private String id;
}