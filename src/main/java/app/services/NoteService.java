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

public class NoteService {
      private final NoteDAO noteDAO;
    private final UserDAO userDAO;
    private final NoteMapper noteMapper;

    public NoteService(EntityManagerFactory emf) {
        this.noteDAO = new NoteDAO(emf);
        this.userDAO = new UserDAO(emf);
        this.noteMapper = new NoteMapper();
    }

    //slår bruger op via dto, opretter entitet, gemmer i DAO, returnerer noteDTO
    public NoteDTO saveNoteForUser(NoteDTO dto) {
        // 1. Check om bruger findes via UserDAO.findById
        User user = userDAO.findById(dto.getUserId());
        if (user == null) {
            throw new ApiException(404, "Brugeren blev ikke fundet med ID: " + dto.getUserId());
        }

        // 2. Opret entitet vha mapper
        Note noteToSave = noteMapper.toEntity(dto, user);
        //Gem i DB
        Note savedEntity = noteDAO.create(noteToSave);
        //Returner som DTO
        return noteMapper.toDTO(savedEntity);
    }


    //Henter liste af entiteter fra DAO og mapper dem
    public List<NoteDTO> getSavedNotesByUserId (Long userId) {
        List<Note> notes = noteDAO.findNotesByUserId(userId);
        List<NoteDTO> dtos = new ArrayList<>();
        for (Note note : notes){
            dtos.add(noteMapper.toDTO(note));
        }
        return dtos;
    }
}

