package database.user;

import jakarta.persistence.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Entity
@Table(name = "user")
public class User {
    private static final String noAvatarPath = "image/noAvatar.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "profile_picture")
    @Lob
    private byte[] profilePicture;

    public User() throws FileNotFoundException {
        initialNoAvatar();
    }
    public User(String username, String password) throws FileNotFoundException {
        this.username = username;
        this.password = password;
        initialNoAvatar();
    }

    private void initialNoAvatar() throws FileNotFoundException {
        ClassLoader classLoader = User.class.getClassLoader();
        try(InputStream in = classLoader.getResourceAsStream(noAvatarPath)) {
            byte[] imageBytes = null;
            if (in != null) {
                imageBytes = in.readAllBytes();
            }
            setProfilePicture(imageBytes);
        } catch (IOException e) {
            throw new FileNotFoundException("noAvatar.png not found!");
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString() {
        return "User [id = " + id + ", username = " + username + ", password = " + password + "]";
    }

}
