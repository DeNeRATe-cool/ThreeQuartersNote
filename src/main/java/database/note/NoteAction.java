package database.note;

import database.HibernateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class NoteAction implements INote {
    private Session session;
    private Transaction transaction;

    private void initial() {
        session = HibernateUtils.openSession();
        transaction = session.beginTransaction();
    }

    /**
     * query if the note with uuid has existed
     * @param note the note
     * @return true if exists
     */
    @Override
    public boolean exist(Note note) {
        if(note == null)
            return false;

        String uuid = note.getUuid();
        try {
            initial();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Note> criteria = builder.createQuery(Note.class);

            Root<Note> root = criteria.from(Note.class);
            criteria.select(root);
            criteria.where(builder.equal(root.get("uuid"), uuid));
            List<Note> notes = session.createQuery(criteria).getResultList();
            transaction.commit();
            return !notes.isEmpty();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }

    /**
     * insert a new shared resource
     * @param note object Note
     */
    @Override
    public void insert(Note note) throws IllegalArgumentException {
        if(note.getUuid() == null || note.getUuid().isEmpty())
            throw new IllegalArgumentException("Note uuid cannot be empty");

        try {
            initial();
            session.persist(note);
            transaction.commit();
            System.out.println("Note inserted successfully");
            System.out.println(note);
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }

    /**
     * delete note if exist
     * @param note object note
     */
    @Override
    public void delete(Note note) throws IllegalArgumentException {
        if(!exist(note)) {
            throw new IllegalArgumentException("Note does not exist");
        }

        try {
            initial();
            EntityManager entityManager = session.unwrap(EntityManager.class);
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaDelete<Note> delete = builder.createCriteriaDelete(Note.class);

            Root<Note> root = delete.from(Note.class);
            delete.where(builder.equal(root.get("uuid"), note.getUuid()));
            entityManager.createQuery(delete).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }

    /**
     * update the note which has existed
     * but renew the information
     * @param note object Note
     */
    @Override
    public void update(Note note) throws IllegalArgumentException {
        if(!exist(note)) {
            throw new IllegalArgumentException("Note does not exist");
        }

        try {
            initial();
            String hql = "FROM Note WHERE uuid = :uuid";
            Query<Note> query = session.createQuery(hql, Note.class);
            query.setParameter("uuid", note.getUuid());
            Note newNote = query.uniqueResult();

            if(newNote != null) {
                newNote.setName(note.getName());
                newNote.setCourse(note.getCourse());
                newNote.setWriter(note.getWriter());
                newNote.setUploadTime(new Date());
                newNote.setContent(note.getContent());

                session.merge(newNote);
            } else {
                System.out.println("No entity found with the uuid: " + note.getUuid());
            }

            transaction.commit();
        } catch(Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }

    /**
     * get ALL notes
     * @return list of all notes shared
     */
    @Override
    public List<Note> queryAll() {
        try {
            initial();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Note> criteriaQuery = criteriaBuilder.createQuery(Note.class);

            Root<Note> root = criteriaQuery.from(Note.class);
            criteriaQuery.select(root);
            List<Note> notes = session.createQuery(criteriaQuery).getResultList();
            transaction.commit();

            return notes;
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if(session != null) {
                session.close();
            }
        }
        return null;
    }

    /**
     * PARTIAL MATCH
     * get Note objects through course name
     * @param course course name
     * @return a list of notes contain the given information
     */
    @Override
    public List<Note> queryByCourse(String course) throws IllegalArgumentException {
        if(course == null || course.isEmpty()) {
            throw new IllegalArgumentException("Course cannot be empty");
        }

        try {
            initial();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Note> criteria = builder.createQuery(Note.class);

            Root<Note> root = criteria.from(Note.class);
            criteria.select(root);
            criteria.where(builder.like(root.get("course"), "%" + course + "%"));
            List<Note> notes = session.createQuery(criteria).getResultList();
            transaction.commit();

            return notes;
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            return null;
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }

    /**
     * PARTIAL MATCH
     * get Note objects through note name
     * @param noteName name of note
     * @return a list of notes contain the given information
     */
    @Override
    public List<Note> queryByNoteName(String noteName) throws IllegalArgumentException {
        if(noteName == null || noteName.isEmpty()) {
            throw new IllegalArgumentException("Course cannot be empty");
        }

        try {
            initial();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Note> criteria = builder.createQuery(Note.class);

            Root<Note> root = criteria.from(Note.class);
            criteria.select(root);
            criteria.where(builder.like(root.get("name"), "%" + noteName + "%"));
            List<Note> notes = session.createQuery(criteria).getResultList();
            transaction.commit();

            return notes;
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            return null;
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }

    /**
     * FULL MATCH
     * get Note objects through writer's name
     * @param writer writer of note
     * @return a list of notes contain the given information
     */
    @Override
    public List<Note> queryByWriter(String writer) throws IllegalArgumentException {
        if(writer == null || writer.isEmpty()) {
            throw new IllegalArgumentException("Course cannot be empty");
        }

        try {
            initial();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Note> criteria = builder.createQuery(Note.class);

            Root<Note> root = criteria.from(Note.class);
            criteria.select(root);
            criteria.where(builder.equal(root.get("writer"), writer));
            List<Note> notes = session.createQuery(criteria).getResultList();
            transaction.commit();

            return notes;
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }

}
