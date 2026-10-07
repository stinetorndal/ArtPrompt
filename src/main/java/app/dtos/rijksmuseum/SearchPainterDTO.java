package app.dtos.rijksmuseum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
//Denne klasse modtager listen fra søgningen
public class SearchPainterDTO {
    @JsonProperty("orderedItems")
    private  List<PaintingReferenceDTO> searchResults;



}