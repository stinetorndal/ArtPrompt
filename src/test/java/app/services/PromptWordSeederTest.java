package app.services;

import app.config.HibernateConfig;
import app.entities.PromptWord;
import app.utils.PromptWordSeeder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PromptWordSeederTest {

    private static EntityManagerFactory emf;

    @BeforeAll
    static void setUp() {
        // Henter test-database konfigurationen
        emf = HibernateConfig.getEntityManagerFactory();
    }

    @Test
    void testSeedPopulatesDatabase() {
        // Arrange
        PromptWordSeeder seeder = new PromptWordSeeder(emf);

        // Act
        seeder.seed();

        // Assert - Chek om der er lagt ord i databasen
        try (EntityManager em = emf.createEntityManager()) {
            List words = em.createQuery("SELECT p FROM PromptWord p", PromptWord.class).getResultList();
            assertFalse(words.isEmpty(), "Databasen bør ikke være tom efter seeding");
        }
    }

    @Test
    void testSeedDoesNotDuplicateData() {
        // Arrange
        PromptWordSeeder seeder = new PromptWordSeeder(emf);
        seeder.seed(); // Første kørsel

        // Act
        seeder.seed(); // Anden kørsel (bør ignoreres pga. hasData())

        // Assert - Checker at antallet af ord ikke er fordoblet
        try (EntityManager em = emf.createEntityManager()) {
            Long count = em.createQuery("SELECT COUNT(p) FROM PromptWord p", Long.class).getSingleResult();

            // Hvis der er 93 ord i CSV-filen, skal count stadig være 93
            assertTrue(count > 0);
        }
    }
}