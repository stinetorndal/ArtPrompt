package app.controllers;

import app.dtos.note.NoteDTO;
import app.services.NoteService;
import io.javalin.http.Context;

import java.util.List;

public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    public void saveNote(Context ctx) {
        NoteDTO inputDTO = ctx.bodyAsClass(NoteDTO.class);
        NoteDTO resultDTO = noteService.saveNoteForUser(inputDTO);

        ctx.status(201); //201 = created
        ctx.json(resultDTO);
    }

    public void getSavedNotesByUser(Context ctx) {
        Long userId = Long.parseLong(ctx.pathParam("userId"));
        List<NoteDTO> noteDTOS = noteService.getSavedNotesByUserId(userId);

        ctx.status(200);
        ctx.json(noteDTOS);
    }
}