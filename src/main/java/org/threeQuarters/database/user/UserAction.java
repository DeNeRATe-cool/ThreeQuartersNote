package org.threeQuarters.database.user;

import org.threeQuarters.database.HibernateUtils;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.JSONObject;
import org.threeQuarters.util.MessageBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserAction implements IUser {
    private static final String path = "./cache/json";
    private static final String imagePath = "./cache/image";
    private static final String filename = "remember.json";
    private static User user = null;

    public static User getNowUser() {
        return user;
    }

    public static void setNowUser(User user) {
        UserAction.user = user;
    }

    private static UserAction instance;

    static{
        instance = new UserAction();
    }

    public static UserAction getInstance(){
        return instance;
    }

    private Session session;
    private Transaction transaction;

    static {
        JsonUtils.createJsonFile(path, filename);
    }

    private void initial() {
        session = HibernateUtils.openSession();
        transaction = session.beginTransaction();
    }

    private User getUser(String username) {
        initial();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);

        Root<User> root = criteria.from(User.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("username"), username));
        User user = session.createQuery(criteria).uniqueResult();
        transaction.commit();
        return user;
    }

    /**
     * check if username has been used
     * @param username username
     * @return true if used
     */
    private boolean exist(String username) {
        try {
            User user = getUser(username);
            return user != null;
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return true;
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }

    /**
     * sign up
     * @param username username
     * @param password password
     * @param rePassword double check password
     * @return true if sign up successfully
     *         false if username duplicated
     * @throws IllegalArgumentException if parameters wrong or password not match
     */
    @Override
    public boolean signUp(String username, String password, String rePassword) throws IllegalArgumentException {
        if(username == null || password == null || rePassword == null ||
                username.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            throw new IllegalArgumentException("empty arguments");
        }

        if(!password.equals(rePassword)) {
            throw new IllegalArgumentException("password not match!");
        }

        if(exist(username))
            return false;

        try {
            initial();
            User user = new User(username, password);
            session.persist(user);
            transaction.commit();
            System.out.println("User signed up successfully!");
            System.out.println(user);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File noAvatar.png not found!");
            return false;
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
     * write information into remember me file
     * @param username username
     * @param password password
     * @param rememberMe write new or delete the existed
     */
    private void writeRemember(String username, String password, boolean rememberMe) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", rememberMe ? username : "");
        jsonObject.put("password", rememberMe ? password : "");
        JsonUtils.writeJsonFile(path, filename, jsonObject);
    }

    /**
     * login if user exists and password matches
     * @param username input username
     * @param password input password
     * @param rememberMe if remember me for the later login action
     * @return true if action successfully
     * @throws IllegalArgumentException empty/null parameters
     */
    @Override
    public boolean login(String username, String password, boolean rememberMe) throws IllegalArgumentException {
        if(username == null || password == null ||
            username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("empty arguments");
        }
        if(!exist(username)) {
            return false;
        }
        try {
            user = getUser(username);
            if(user.getPassword().equals(password)) {
                writeRemember(username, password, rememberMe);
                return true;
            } else
                return false;
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
     * get remember.json file content
     * @return null if nothing written
     *         list of String if exists
     */
    @Override
    public List<String> checkIfRemember() {
        Map<String, Object> info = JsonUtils.readJsonFile(path, filename);
        if(info.get("username").toString().isEmpty() || info.get("password").toString().isEmpty())
            return null;
        else {
            List<String> result = new ArrayList<>();
            result.add(info.get("username").toString());
            result.add(info.get("password").toString());
            return result;
        }
    }

    public Map<String,Object> getRememberMap(){
        Map<String, Object> info = JsonUtils.readJsonFile(path, filename);
        return info;
    }

    /**
     * store avatar of user locally
     * @param username username
     * @param picture bytes of avatar
     */
    private void storeLocal(String username, byte[] picture) {
        String filePath = Paths.get(imagePath, username + ".png").toString();
        try(FileOutputStream fos = new FileOutputStream(new File(filePath))) {
            fos.write(picture);
        } catch (IOException e) {
            System.out.println("image storing fails...");
        }
    }

    /**
     * get avatar of given username
     * @param username username
     * @return bytes of avatar
     */
    @Override
    public byte[] getAvatar(String username) {
        if(username == null || username.isEmpty() || !exist(username)) {
            try {
                User user = new User();
                return user.getProfilePicture();
            } catch (FileNotFoundException e) {
                System.out.println("File noAvatar.png not found!");
            }
        }

        try {
            User user = getUser(username);
            storeLocal(username, user.getProfilePicture());
            return user.getProfilePicture();
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
     * update new avatar of user
     * @param username username
     * @param path path of new avatar picture
     * @return true if update successfully
     * @throws IllegalArgumentException illegal parameters
     */
    @Override
    public boolean updateAvatar(String username, String path) throws IllegalArgumentException, PersistenceException {
        if(username == null || username.isEmpty() || path == null || path.isEmpty()) {
            throw new IllegalArgumentException("empty arguments");
        }

        if(!exist(username)) {
            return false;
        }

        try {
            initial();
            byte[] newAvatar = Files.readAllBytes(Paths.get(path));
//            newAvatar = ImageUtils.compressToTargetSize(newAvatar,50);
            if(newAvatar.length > 50000)
            {
                System.out.println(newAvatar.length);
                new MessageBox("","","Picture too big");
                return false;
            }
            String hql = "FROM User WHERE username = :username";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("username", username);
            User user = query.uniqueResult();

            if(user != null) {
                user.setProfilePicture(newAvatar);
                session.merge(user);
            }
            transaction.commit();
            return true;
        } catch (IOException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }

    public static void main(String[] args) {
        boolean fl = UserAction.getInstance().login("GGengX","123456",true);
        System.out.println("login:" + fl);
        System.out.println(UserAction.getInstance().getNowUser().getUsername());
        System.out.println(UserAction.getInstance().checkIfRemember());
    }

}
