package app.dtos.savedimages;

import app.entities.SavedImage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)

public class SavedImageDTO {
    private Long id;
    private String url;
    private String externalId;
    private String title;
    private String source;
    private Long userId;

   }
