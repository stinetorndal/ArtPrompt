package app.services;

import app.dao.NoteDAO;
import app.dao.UserDAO;
import app.dtos.note.NoteDTO;
import app.entities.Note;
import app.entities.User;
import app.exceptions.ApiException;
import app.mappers.NoteMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

//Denne klasser gemmer noter og henter noter fra bruger
public class NoteService {
    private final NoteDAO noteDAO;
    private final UserDAO userDAO;
    private final NoteMapper noteMapper;

    public NoteService(EntityManagerFactory emf) {
        this.noteDAO = new NoteDAO(emf);
        this.userDAO = new UserDAO(emf);
        this.noteMapper = new NoteMapper();
    }

    //slår bruger op via dto, tjekker om bruger findes, opretter entitet, gemmer i DAO, returnerer noteDTO
    public NoteDTO saveNoteForUser(NoteDTO dto) {
        User user = userDAO.findById(dto.getUserId());
        if (user == null) {
            throw new ApiException(404, "Brugeren blev ikke fundet med ID: " + dto.getUserId());
        }

        Note noteToSave = noteMapper.toEntity(dto, user);

        Note savedEntity = noteDAO.create(noteToSave);

        return noteMapper.toDTO(savedEntity);
    }


    //Henter liste af entiteter fra DAO og mapper dem
    public List<NoteDTO> getSavedNotesByUserId(Long userId) {
        List<Note> notes = noteDAO.findNotesByUserId(userId);
        List<NoteDTO> dtos = new ArrayList<>();
        for (Note note : notes) {
            dtos.add(noteMapper.toDTO(note));
        }
        return dtos;
    }

    //Retter teksten i en eksisterende note og returnerer den opdaterede note
    public NoteDTO updateNote(Long noteId, String newText) {
        if (newText == null || newText.isBlank()) {
            throw new ApiException(400, "Noten må ikke være tom");
        }
        Note note = findNoteOrThrow(noteId);
        note.setText(newText);
        Note updatedNote = noteDAO.update(note);
        return noteMapper.toDTO(updatedNote);
    }

    //Sletter en note. Kaster 404, hvis noten ikke findes
    public void deleteNote(Long noteId) {
        findNoteOrThrow(noteId);
        noteDAO.delete(noteId);
    }

    //Finder id på den bruger, der ejer noten, så controlleren kan tjekke ejerskab
    public Long getOwnerId(Long noteId) {
        return findNoteOrThrow(noteId).getUser().getId();
    }

    //Henter en note eller kaster 404, hvis den ikke findes
    private Note findNoteOrThrow(Long noteId) {
        Note note = noteDAO.findById(noteId);
        if (note == null) {
            throw new ApiException(404, "Noten blev ikke fundet med ID: " + noteId);
        }
        return note;
    }
}

