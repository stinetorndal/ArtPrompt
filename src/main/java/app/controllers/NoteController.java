package app.controllers;

import app.dtos.note.NoteDTO;
import app.services.NoteService;
import app.utils.RequestUtil;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NoteController {
    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    public void saveNote(Context ctx) {
        NoteDTO inputDTO = ctx.bodyAsClass(NoteDTO.class);
        NoteDTO resultDTO = noteService.saveNoteForUser(inputDTO);

        logger.info("Gemte ny note for bruger ID: {}", resultDTO.getUserId());
        ctx.status(201).json(resultDTO);
    }

    public void getSavedNotesByUser(Context ctx) {
        Long userId = RequestUtil.getLongPathParam(ctx,"userId");
        List<NoteDTO> noteDTOS = noteService.getSavedNotesByUserId(userId);
        logger.info("Hentede {} noter for bruger ID: {}",  noteDTOS.size(), userId);

        ctx.status(200).json(noteDTOS);
    }
}