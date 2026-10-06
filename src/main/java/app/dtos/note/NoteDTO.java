package app.dtos.note;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

//Kontrakt mellem  API og frontend/konsol. Felter der er relevante at sende over netværk
public class NoteDTO {
    private Long id;
    private String text;
    private Long userId; //fremmednøgle


}


