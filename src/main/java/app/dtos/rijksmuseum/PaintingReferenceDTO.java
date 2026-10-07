package app.dtos.rijksmuseum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class PaintingReferenceDTO {
    // Modtager id-url ("https://id.rijksmuseum.nl/200106038") på det enkelte maleri i søgningen
    private String id;

}
