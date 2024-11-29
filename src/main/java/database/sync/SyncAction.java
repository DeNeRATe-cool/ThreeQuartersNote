package database.sync;

import database.HibernateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class SyncAction implements ISync {
    public Session session;
    public Transaction transaction;

    private void initial() {
        session = HibernateUtils.openSession();
        transaction = session.beginTransaction();
    }

    /**
     * insert all .md notes into database
     * after walk all files
     * @param folderPath absolute root path
     * @param username online username
     */
    private void insert(String folderPath, String username) {
        try {
            initial();
            List<SyncNote> notes = FileUtils.walkMdFile(folderPath, username);
            if (notes != null) {
                for (SyncNote note : notes) {
                    session.persist(note);
                }
            }
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
     * clean the notes in database of user before
     * @param username online username
     */
    private void clean(String username) {
        try {
            initial();
            EntityManager entityManager = session.unwrap(EntityManager.class);
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaDelete<SyncNote> delete = builder.createCriteriaDelete(SyncNote.class);

            Root<SyncNote> root = delete.from(SyncNote.class);
            delete.where(builder.equal(root.get("username"), username));
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
     * back up all .md note of the given root directory
     * @param folderPath absolute directory path
     * @param username online username
     * @throws IllegalArgumentException if path error or username lacked
     */
    @Override
    public void backUp(String folderPath, String username) throws IllegalArgumentException {
        if(folderPath == null || folderPath.isEmpty()) {
            throw new IllegalArgumentException("Path error");
        }
        if(username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username error");
        }

        clean(username);
        insert(folderPath, username);
    }

    /**
     * get all sync notes of the user
     * @param username online username
     * @return a list of notes have backed up
     */
    private List<SyncNote> querySyncNotes(String username) {
        try {
            initial();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<SyncNote> criteria = builder.createQuery(SyncNote.class);

            Root<SyncNote> root = criteria.from(SyncNote.class);
            criteria.select(root);
            criteria.where(builder.equal(root.get("username"), username));
            List<SyncNote> notes = session.createQuery(criteria).getResultList();
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

    /**
     * transform SyncNote class to actual content text
     * @param note sync note
     * @return content of the note
     */
    private String object2Text(SyncNote note) {
        StringBuilder sb = new StringBuilder();
        if(note.getCourse() != null || note.getUuid() != null) {
            sb.append("---\n");
            if (note.getUuid() != null) {
                sb.append("uuid: ");
                sb.append(note.getUuid()).append("\n");
            }
            if (note.getCourse() != null) {
                sb.append("course: ");
                sb.append(note.getCourse()).append("\n");
            }
            sb.append("---\n");
        }
        sb.append(note.getContent());
        return sb.toString();
    }

    /**
     * write content to target .md file
     * @param filePath given target path
     * @param content text
     * @throws IOException if file error
     */
    private void writeToFile(String filePath, String content) throws IOException {
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }

    /**
     * deal one sync note
     * and load it to local
     * @param filePath target path
     * @param syncNote note
     */
    private void singleLocalSync(String filePath, SyncNote syncNote) {
        String content = object2Text(syncNote);
        try {
            writeToFile(filePath, content);
        } catch (IOException e) {
            System.err.println("写入文件时发生错误: " + e.getMessage());
        }
    }

    /**
     * sync all notes of the online user
     * no matter if the path existed
     * @param targetPath target path to save sync notes
     * @param username online username
     * @throws IllegalArgumentException if path or username error
     */
    @Override
    public void sync(String targetPath, String username) throws IllegalArgumentException {
        if(targetPath == null || targetPath.isEmpty()) {
            throw new IllegalArgumentException("Path error");
        }
        if(username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username error");
        }

        List<SyncNote> notes = querySyncNotes(username);
        if (notes != null) {
            for(SyncNote note : notes) {
                String filePath = Paths.get(targetPath, note.getPath()).toString();
                singleLocalSync(filePath, note);
            }
        }
    }
}
