package app.dtos.savedimages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)

//Denne klasse håndterer JSON-felter på det billede, bruger vil gemme
public class SavedImageDTO {
    private Long id;
    private String url;
    private String externalId;
    private String title;
    private String source;
    private Long userId;

   }
