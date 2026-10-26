package app.dtos.note;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

//Kontrakt mellem  API og frontend/konsol. Felter der er relevante at sende over netværk
public class NoteDTO {
    private Long id;
    private String text;
    private Long userId; //fremmednøgle

    //Vises som 2026-10-26 i JSON og sættes af serveren, ikke klienten
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}


