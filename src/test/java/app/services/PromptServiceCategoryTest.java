package app.services;

import app.config.HibernateConfig;
import app.entities.PromptCategory;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class PromptServiceCategoryTest {

    private static PromptService promptService;

    @BeforeAll
    static void setup(){
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        promptService=new PromptService(emf);
    }

    @Test
    void testGetAllCategoriesReturnsAllEnumValues(){
        //Act
        List<PromptCategory> categories = promptService.getAllCategories();
        // Udskriv resultatet i konsollen
        System.out.println("--- ALLE KATEGORIER ---");
        for (PromptCategory cat : categories) {
            System.out.println("- " + cat);
        }
        //Assert
        assertNotNull(categories);
        assertEquals(PromptCategory.values().length, categories.size());
        assertTrue(categories.contains(PromptCategory.SEASONS));
    }

    @Test
    void testGetCategoriesAgainExcludingPickedOneInFirstChoice() {
        // Arrange
        PromptCategory pickedCategory = PromptCategory.SEASONS;
        System.out.println("\n--- VALGT KATEGORI DER SKAL FJERNES FRA MIT ÅSYN: " + pickedCategory + " ---");

        // Act
        List <PromptCategory> availableCategories =
                promptService.getCategoriesAgainExcludingPickedOne(pickedCategory);
        // Udskriv de resterende kategorier i konsollen
        System.out.println("RESTERENDE KATEGORIER:");
        for (PromptCategory cat : availableCategories) {
            System.out.println("- " + cat);
        }

        // Assert
        assertNotNull(availableCategories);
        assertFalse(availableCategories.contains(pickedCategory), "Den valgte kategori må ikke være i listen");
        assertEquals(PromptCategory.values().length - 1, availableCategories.size());
    }
}

