package app.dtos.rijksmuseum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
//Rijksmuseum har oplysninger pakket ind i mange underobjekter, deraf strukturen
public class PaintingDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("identified_by")
    private List<NameDTO> identifiedBy;

    @JsonProperty("image")
    private ImageDTO image;

    @JsonProperty("produced_by")
    private ProductionDTO producedBy;

    // Hjælpemetode til at hente titlen fra identified_by-listen
    public String getTitle() {
        if (identifiedBy != null) {
            for (NameDTO name : identifiedBy) {
                if ("Name".equals(name.getType()) && name.getContent() != null) {
                    return name.getContent();
                }
            }
        }
        return "Unknown Title";
    }

    // Hjælpemetode til at trække årstallet ud
    public String getDateCreated() {
        if (producedBy != null && producedBy.getTimespan() != null && producedBy.getTimespan().getBeginOfTheBegin() != null) {
            String dateString = producedBy.getTimespan().getBeginOfTheBegin(); //henter fulde datostreng fra API
            if (dateString.length() >= 4) { //hiver første 4 ud, som er årstallet
                return dateString.substring(0, 4);
            }
        }
        return "Unknown Year";
    }
}