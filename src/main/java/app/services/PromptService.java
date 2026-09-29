package app.services;

import app.dao.PromptWordDAO;
import app.entities.PromptCategory;
import app.entities.PromptWord;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//I denne klasse udvælges to ord ud fra kategorier. To prompts
//Kategori 1 må ikke genbruges i kategori 2
public class PromptService {

    private static final Logger logger = LoggerFactory.getLogger(PromptService.class);
    private final PromptWordDAO promptWordDAO;
    private final Random random = new Random();

    // Servicen opretter selv sin DAO for at undgå DAO i AppRunner
    public PromptService(EntityManagerFactory emf) {
        this.promptWordDAO = new PromptWordDAO(emf);
    }

    //returnerer List<PromptCategory> med alle værdierne fra ENUM
    public List<PromptCategory> getAllCategories(){
        return Arrays.asList(PromptCategory.values());
    }

    //returnerer alle kategorier minus den brugeren allerede har valgt
    public List<PromptCategory> getCategoriesAgainExcludingPickedOne (PromptCategory selectecCategory){
        List<PromptCategory> availableCategories = new ArrayList<>();
        for (PromptCategory category : PromptCategory.values()){
            if (category!=selectecCategory){
                availableCategories.add(category);
            }
        }
        return availableCategories;
    }
    public PromptWord randomizeWordsFromPromptWordDAO(PromptCategory category){
        logger.info("Udvælger tilfældigt ord for kategori: {}", category);
        List<PromptWord> wordsToBeRandomized= promptWordDAO.getWordByCategory(category);
        if (wordsToBeRandomized.isEmpty()) {
            logger.warn("Der blev ikke fundet nogle ord i denne kategori: {}", category);
            throw new ApiException(404, "Ingen ord fundet for den valgte kategori");
        }
        //randomize
            int randomIndex = random.nextInt(wordsToBeRandomized.size());
            return wordsToBeRandomized.get(randomIndex);
        }
    }

