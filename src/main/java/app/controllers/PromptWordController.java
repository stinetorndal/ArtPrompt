package app.controllers;

import app.dtos.promptword.PromptWordDTO;
import app.entities.PromptCategory;
import app.exceptions.ApiException;
import app.services.PromptService;
import app.utils.RequestUtil;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PromptWordController {
    private static final Logger logger = LoggerFactory.getLogger(PromptWordController.class);
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
        PromptCategory cat1 = RequestUtil.getEnumQueryParam(ctx, "cat1", PromptCategory.class);
        PromptCategory cat2 = RequestUtil.getEnumQueryParam(ctx, "cat2", PromptCategory.class);


        // 4. Generer DTO ud fra brugerens kategori-valg
        PromptWordDTO dto = promptService.generatePromptWordDTO(cat1, cat2);
        logger.info("Genererede prompt-ord for kategorierne {} og {}", cat1.name(), cat2.name());
        ctx.status(200).json(dto);
    }
}
