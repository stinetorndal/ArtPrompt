package app.mappers;

import app.dtos.note.NoteDTO;
import app.entities.Note;
import app.entities.User;

public class NoteMapper {

    public NoteDTO toDTO(Note note) {
        if (note == null) return null;

        User user = note.getUser();
        return new NoteDTO(
                note.getId(),
                note.getText(),
                user != null ? user.getId() : null
        );
    }
    //Opretter nyt Note-objekt, bruger settere til at overføre data til DTO og knytte User-entitet
        public Note toEntity(NoteDTO dto, User user){
            if (dto == null) return null;

            Note note = new Note();
            note.setId(dto.getId());
            note.setText(dto.getText());
            note.setUser(user);
            return note;

       }
}
