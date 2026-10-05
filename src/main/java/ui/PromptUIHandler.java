package ui;

import app.dtos.promptword.PromptWordDTO;
import app.entities.PromptCategory;
import app.exceptions.ApiException;
import app.services.PromptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Scanner;

public class PromptUIHandler {

    private static final Logger logger = LoggerFactory.getLogger(PromptUIHandler.class);
    private final PromptService promptService;
    private final GenericUI genericUI;

    public PromptUIHandler(PromptService promptService, GenericUI genericUI) {
        this.promptService = promptService;
        this.genericUI = genericUI;
    }

    public void handleGeneratePrompts() {
        System.out.println("\n--- GENERÉR ORDPROMPTER ---");
        try {
            //Henter alle kategorier
            List<PromptCategory> allCategories = promptService.getAllCategories();
            PromptCategory category1 = genericUI.chooseFromList("Vælg første kategori: ", allCategories);
            if (category1 == null) return; //returnerer hvis der ikke vælges noget

            // 2. Henter kategorier men uden den der allerede er valgt)
            List<PromptCategory> remainingCategories = promptService.getCategoriesAgainExcludingPickedOne(category1);
            PromptCategory category2 = genericUI.chooseFromList("Vælg anden kategori: ", remainingCategories);
            if (category2 == null) return;

            // 3. Genererer DTO
            PromptWordDTO promptWordDTO = promptService.generatePromptWordDTO(category1, category2);

            // 3. Udskriv resultatet
            System.out.println("\nDINE PROMPT-ORD:");
            System.out.println("-> " + category1 + ": " + promptWordDTO.getWord1());
            System.out.println("-> " + category2 + ": " + promptWordDTO.getWord2());

        } catch (ApiException e) {
            logger.warn("Fejl ved generering af ordprompter: {}", e.getMessage());
            System.out.println("FEJL: " + e.getMessage());
        }
    }
    }


