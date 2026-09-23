package app.dtos.unsplash;

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

//Her vil jeg gerne kunne søge på en farve
//Wrapper-class - tager imod json, smider overskydende væk og leverer en List
public class SearchDTO {
    @JsonProperty("results")
    private List<PhotoDTO> searchResult;

}
