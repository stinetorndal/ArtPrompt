package app.dao;

import app.entities.PromptCategory;
import app.entities.PromptWord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PromptWordDAO {

    private final EntityManagerFactory emf;

    public PromptWordDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    //getRandomWordByCategory
    //Henter alle ord fra databasen og bruger Random til at vælge vilkårligt
    public List<PromptWord> getWordByCategory(PromptCategory category) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(  //Vi leder efter et PromptWord-objekt
                            "SELECT p FROM PromptWord p WHERE p.category = :category", PromptWord.class)
                    .setParameter("category", category)
                    .getResultList();

        }
    }

}