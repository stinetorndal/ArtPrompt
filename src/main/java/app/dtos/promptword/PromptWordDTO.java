package app.dtos.promptword;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
//Denne klasse bruges at levere færdigt PromptWord-objekt = to ord
public class PromptWordDTO {
    private String word1;
    private String word2;

}
