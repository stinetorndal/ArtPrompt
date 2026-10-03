package app.controllers;

import app.dtos.promptword.PromptWordDTO;
import app.entities.PromptCategory;
import app.exceptions.ApiException;
import app.services.PromptService;
import io.javalin.http.Context;

import java.util.List;

public class PromptWordController {
    private final PromptService promptService;

    public PromptWordController(PromptService promptService) {
        this.promptService = promptService;
    }

    public void getCategories(Context ctx) {
        List<PromptCategory> categories = promptService.getAllCategories();
        ctx.status(200).json(categories);
    }

    public void getRandomPrompt(Context ctx) {
        // 1. Hent brugerens valg fra URL'en (query parameters)
        String cat1Param = ctx.queryParam("cat1");
        String cat2Param = ctx.queryParam("cat2");

        // 2. Check om brugeren har glemt at vælge kategorier
        if (cat1Param == null || cat2Param == null) {
            //Ingen logger, brugeren modtager fejlsvar
            throw new ApiException(400, "Vælg venligst to kategorier (cat1 og cat2)");
        }

        // 3. Konverter strengene fra URL'en til PromptCategory Enums
        PromptCategory cat1 = PromptCategory.valueOf(cat1Param.toUpperCase());
        PromptCategory cat2 = PromptCategory.valueOf(cat2Param.toUpperCase());

        // 4. Generer DTO ud fra brugerens kategori-valg
        PromptWordDTO dto = promptService.generatePromptWordDTO(cat1, cat2);
        ctx.status(200).json(dto);
    }
}
