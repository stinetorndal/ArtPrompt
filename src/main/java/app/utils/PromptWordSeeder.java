package app.utils;


import app.entities.PromptCategory;
import app.entities.PromptWord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Denne klasse skal lægge ord fra .csv-filen ned i db
public class PromptWordSeeder {
    private static final String CSV_PATH = "src/main/resources/prompts.csv";

    private final EntityManagerFactory emf;


    public PromptWordSeeder(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void seed() {
        if (hasData()) {
            System.out.println("Der er allerede data i databasen - springer seeding over");
            return;
        }
        List<PromptWord> parsedWords = parseCsv();
        saveWordsInDb(parsedWords);
        System.out.println("Seedede " + parsedWords.size() + " ord i database");
    }


        //Læser csv-filen og bygger objekter
        private List<PromptWord> parseCsv() {
            List<PromptWord> listOfWords = new ArrayList<>();
            File file = new File(CSV_PATH);
            try (Scanner scanner = new Scanner(file)) {
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        String wordText = parts[0].trim();
                        PromptCategory category =
                                PromptCategory.valueOf(parts[1].trim());
                        PromptWord promptWord =
                                new PromptWord(category, wordText);
                        listOfWords.add(promptWord);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Kunne ikke finde filen " + e.getMessage());
            }
            return listOfWords;
        }

        private void saveWordsInDb(List<PromptWord> wordsToSave) {
            EntityManager em = emf.createEntityManager();

            em.getTransaction().begin();
            for (PromptWord word : wordsToSave) {
                em.persist(word);
            }
            em.getTransaction().commit();
            em.close();
        }

        private boolean hasData() {
            try (EntityManager em = emf.createEntityManager()) {
                Long count = em.createQuery("SELECT COUNT(p) FROM PromptWord p", Long.class)
                        .getSingleResult();
                return count > 0;
            }
        }

}
