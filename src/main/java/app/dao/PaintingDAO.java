package app.dao;

import app.entities.Painting;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

//Denne klasse håndterer Painting-persistens
public class PaintingDAO implements IDAO<Painting, Long> {

    private static final Logger logger = LoggerFactory.getLogger(SavedImageDAO.class);
    private final EntityManagerFactory emf;

    public PaintingDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    @Override
    public Painting create(Painting painting) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(painting);
            em.getTransaction().commit();
            return painting;
        } finally {
            em.close();
        }
    }

    @Override
    public Painting findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Painting.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Painting> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Painting> query = em.createQuery("SELECT p FROM Painting p", Painting.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Painting update(Painting painting) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Painting updatedPainting = em.merge(painting);
            em.getTransaction().commit();
            return updatedPainting;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Painting savedPainting = em.find(Painting.class, id);
            boolean isDeleted = false;

            if (savedPainting != null) {
                em.remove(savedPainting);
                isDeleted = true;
            }

            em.getTransaction().commit();
            return isDeleted;
        } finally {
            em.close();
        }
    }
    // Findes maleriet fra API allerede DB
    public boolean existsByExternalId(String externalId) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(p) FROM Painting p WHERE p.externalId = :externalId", Long.class);
            query.setParameter("externalId", externalId);
            return query.getSingleResult() > 0;
        }
    }

}
