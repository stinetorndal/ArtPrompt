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

    // Hjælpemetode til finde den første dato, hvis der er tilknyttet flere til maleri. Null forhindrer crash
    private String getFirstBeginDate(){
        if (producedBy == null || producedBy.getTimespan() == null || producedBy.getTimespan().isEmpty()){
            return null;
        }
        return producedBy.getTimespan().get(0).getBeginOfTheBegin();
    }

    //Finder førte fire karakterer = årstal fra timespans første datasæt
    public String getDateCreated() {
        String dateString = getFirstBeginDate();
        if (dateString== null && dateString.length() >=4){
            return dateString.substring(0,4);
            }
                return "Unknown Year";
    }

    // Hjælpemetode til at hente billedets URL
    public String getImageUrl() {
        if (image != null && image.getContentUrl() != null) {
            return image.getContentUrl(); // Eller det felt i ImageDTO der indeholder URL'en
        }
        return null;
    }

    // Hjælpemetode til at trække årstallet ud som int
    public int getYear() {
        String yearStr = getDateCreated();
        if (!"Unknown Year".equals(yearStr)) {
            try {
                return Integer.parseInt(yearStr);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}