package app.dao;

import app.dtos.savedimages.SavedImageDTO;
import app.entities.SavedImage;
import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SavedImageDAO implements IDAO<SavedImage, Long>{

    private static final Logger logger = LoggerFactory.getLogger(SavedImageDAO.class);
    private final EntityManagerFactory emf;

    public SavedImageDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    @Override
    public SavedImage create(SavedImage savedImage) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(savedImage);
            em.getTransaction().commit();
            return savedImage;
        } finally {
            em.close();
        }
    }

    @Override
    public SavedImage findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(SavedImage.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<SavedImage> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<SavedImage> query = em.createQuery("SELECT s FROM SavedImage s", SavedImage.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public SavedImage update(SavedImage savedImage) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            SavedImage updatedSavedImage = em.merge(savedImage);
            em.getTransaction().commit();
            return updatedSavedImage;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            SavedImage savedImage = em.find(SavedImage.class, id);
            boolean isDeleted = false;

            if (savedImage != null) {
                em.remove(savedImage);
                isDeleted = true;
            }

            em.getTransaction().commit();
            return isDeleted;
        } finally {
            em.close();
        }
    }

    //Hent alle gemte billeder fra speciifik bruger
    public List<SavedImage> findImagesByUserId (Long userId){
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<SavedImage> query = em.createQuery(
                    "SELECT s FROM SavedImage s WHERE s.user.id = :userId", SavedImage.class);
            query.setParameter("userId", userId);
            return query.getResultList();
                    }
    }

}
