package app.controllers;

import app.dtos.note.NoteDTO;
import app.services.NoteService;
import app.utils.RequestUtil;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

//Denne klasse håndterer indkommende note-HTTP-forespørgsler fra bruger
public class NoteController {
    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    public void saveNote(Context ctx) {
        NoteDTO inputDTO = ctx.bodyAsClass(NoteDTO.class);
        //Bruger kommer fra token, userId i body ignoreres
        inputDTO.setUserId(RequestUtil.getAuthenticatedUserId(ctx));
        NoteDTO resultDTO = noteService.saveNoteForUser(inputDTO);

        logger.info("Gemte ny note for bruger ID: {}", resultDTO.getUserId());
        ctx.status(201).json(resultDTO);
    }

    public void getSavedNotesByUser(Context ctx) {
        Long userId = RequestUtil.getLongPathParam(ctx,"userId");
        RequestUtil.ensureOwnerIsAdminOrUser(ctx, userId);
        List<NoteDTO> noteDTOS = noteService.getSavedNotesByUserId(userId);
        logger.info("Hentede {} noter for bruger ID: {}",  noteDTOS.size(), userId);

        ctx.status(200).json(noteDTOS);
    }
    //Retter teksten i en note. Kun ejeren eller en admin må rette den
    public void updateNote(Context ctx) {
        Long noteId = RequestUtil.getLongPathParam(ctx, "id");
        RequestUtil.ensureOwnerIsAdminOrUser(ctx, noteService.getOwnerId(noteId));

        NoteDTO inputDTO = ctx.bodyAsClass(NoteDTO.class); // Laver JSON om til NoteDTO
        NoteDTO resultDTO = noteService.updateNote(noteId, inputDTO.getText()); //COntroller kalder Service, service retter og sender tilbage

        logger.info("Opdaterede note ID: {}", noteId);
        ctx.status(200).json(resultDTO);
    }

    //Sletter en note. Kun ejeren eller en admin må slette den
    public void deleteNote(Context ctx) {
        Long noteId = RequestUtil.getLongPathParam(ctx, "id");
        RequestUtil.ensureOwnerIsAdminOrUser(ctx, noteService.getOwnerId(noteId));

        noteService.deleteNote(noteId);

        logger.info("Slettede note ID: {}", noteId);
        ctx.status(204);
    }
}