package app.dao;

import app.dtos.savedpaintings.SavedPaintingDTO;
import app.entities.SavedPainting;
import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class SavedPaintingDAO implements IDAO<SavedPainting, Long> {

    private static final Logger logger = LoggerFactory.getLogger(SavedImageDAO.class);
    private final EntityManagerFactory emf;

    public SavedPaintingDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    @Override
    public SavedPainting create(SavedPainting savedPainting) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(savedPainting);
            em.getTransaction().commit();
            return savedPainting;
        } finally {
            em.close();
        }
    }

    @Override
    public SavedPainting findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(SavedPainting.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<SavedPainting> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<SavedPainting> query = em.createQuery("SELECT s FROM SavedPainting s", SavedPainting.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public SavedPainting update(SavedPainting savedPainting) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            SavedPainting updatedSavedPainting = em.merge(savedPainting);
            em.getTransaction().commit();
            return updatedSavedPainting;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            SavedPainting savedPainting = em.find(SavedPainting.class, id);
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

    //Hent alle gemte billeder fra speciifik bruger
    public List<SavedPainting> findPaintingsByUserId (Long userId){
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<SavedPainting> query = em.createQuery(
                    "SELECT s " +
                            "FROM SavedPainting s " +
                            "   WHERE s.user.id = :userId", SavedPainting.class);
            query.setParameter("userId", userId);
            return query.getResultList();
                    }
    }

    //Er maleri allerede gemt af denne bruger?
    public boolean hasPaintingAlreadyBeenSavedByThisUserId (String externalId, Long userId){
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT (s) " +
                            "FROM SavedPainting s " +
                            "WHERE s.externalId = :externalId AND s.user.id = :userId", Long.class);
            query.setParameter("externalId", externalId);
            query.setParameter("userId", userId);
            Long count = query.getSingleResult();
            return count >0;
        }
    }

}


