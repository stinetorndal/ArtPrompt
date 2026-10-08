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
// griber titlen under identified_by. I Rijksmuseum ligger info pakket ind i objekter der er pakket ind i 0bjekter
public class NameDTO {

    @JsonProperty("content")
    private String content;

    @JsonProperty("type")
    private String type;
}