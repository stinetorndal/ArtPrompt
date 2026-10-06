package app.dao;

import app.entities.Note;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NoteDAO  implements IDAO<Note, Long>{

        private static final Logger logger = LoggerFactory.getLogger(app.dao.NoteDAO.class);
        private final EntityManagerFactory emf;

        public NoteDAO(EntityManagerFactory emf) {
            this.emf = emf;
        }
        @Override
        public Note create(Note note) {
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                em.persist(note);
                em.getTransaction().commit();
                return note;
            } finally {
                em.close();
            }
        }

        @Override
        public Note findById(Long id) {
            EntityManager em = emf.createEntityManager();
            try {
                return em.find(Note.class, id);
            } finally {
                em.close();
            }
        }

        @Override
        public List<Note> findAll() {
            EntityManager em = emf.createEntityManager();
            try {
                TypedQuery<Note> query = em.createQuery("SELECT n FROM Note n", Note.class);
                return query.getResultList();
            } finally {
                em.close();
            }
        }

        @Override
        public Note update(Note note) {
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                Note updatedNote = em.merge(note);
                em.getTransaction().commit();
                return updatedNote;
            } finally {
                em.close();
            }
        }

        @Override
        public boolean delete(Long id) {
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                Note note = em.find(Note.class, id);
                boolean isDeleted = false;

                if (note != null) {
                    em.remove(note);
                    isDeleted = true;
                }

                em.getTransaction().commit();
                return isDeleted;
            } finally {
                em.close();
            }
        }

        public List<Note> findNotesByUserId (Long userId){
            try (EntityManager em = emf.createEntityManager()) {
                TypedQuery<Note> query = em.createQuery(
                        "SELECT n FROM Note n WHERE n.user.id = :userId", Note.class);
                query.setParameter("userId", userId);
                return query.getResultList();
            }
        }

    }

