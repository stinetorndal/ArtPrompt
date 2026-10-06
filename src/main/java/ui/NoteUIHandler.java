package ui;

import app.dtos.note.NoteDTO;
import app.dtos.user.UserDTO;
import app.exceptions.ApiException;
import app.services.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class NoteUIHandler {
    private static final Logger logger = LoggerFactory.getLogger(NoteUIHandler.class);
    private final NoteService noteService;
    private final Scanner scanner;

    public NoteUIHandler(NoteService noteService, Scanner scanner) {
        this.noteService = noteService;
        this.scanner = scanner;
    }
    // Opret ny note for den indloggede bruger
    public void handleCreateNote(UserDTO currentUser) {
        System.out.println("\n--- SKRIV NY NOTE ---");
        System.out.print("Indtast din note: ");
        String text = scanner.nextLine().trim();

        if (text.isEmpty()) {
            System.out.println("Noten kan ikke være tom.");
            return;
        }
        try {
            NoteDTO newNote = new NoteDTO(null, text, currentUser.getId()); //null fordi der ike er en note + tilknytter bruger endnu
            NoteDTO savedNote = noteService.saveNoteForUser(newNote);
            System.out.println("Noten er gemt! (ID: " + savedNote.getId() + ")");
        } catch (ApiException e) {
            logger.warn("Fejl ved oprettelse af note for bruger ID {}: {}", currentUser.getId(), e.getMessage());
            System.out.println("FEJL: " + e.getMessage());
        }
    }

    // Vis gemte noter for den indloggede bruger
    public void handleShowNotes(UserDTO currentUser) {
        System.out.println("\n--- DINE GEMTE NOTER ---");
        try {
            List<NoteDTO> notes = noteService.getSavedNotesByUserId(currentUser.getId());
            if (notes.isEmpty()) {
                System.out.println("Du har ingen gemte noter endnu.");
            } else {
                for (NoteDTO note : notes) {
                    System.out.println("- [# " + note.getId() + "] " + note.getText());
                }
            }
        } catch (ApiException e) {
            logger.warn("Fejl ved hentning af noter for bruger ID {}: {}", currentUser.getId(), e.getMessage());
            System.out.println("FEJL: " + e.getMessage());
        }
    }
}

