package app.dao;

import app.config.HibernateConfig;
import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

//Denne klasse skal gemme brugerdata i DB
public class UserDAO implements IDAO<User, Long> { //JPA-standar er Long

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public User create(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        } finally {
            em.close();
        }
    }

    @Override
    public User findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public User update(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User updatedUser = em.merge(user);
            em.getTransaction().commit();
            return updatedUser;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            boolean isDeleted = false;

            if (user != null) {
                em.remove(user);
                isDeleted = true;
            }

            em.getTransaction().commit();
            return isDeleted;
        } finally {
            em.close();
        }
    }
}