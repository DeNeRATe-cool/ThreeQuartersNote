package database;

import org.threeQuarters.database.DirectoryInitial;
import org.threeQuarters.database.user.IUser;
import org.threeQuarters.database.user.UserAction;

public class UserTest {

    private static final IUser iUser = new UserAction();

    private static final String username = "admin";
    private static final String password = "abc";
    private static final String rePassword = "abc";
    private static final String newAvatarPath = "C:/Users/12298/Desktop/picture/正装照 - 副本.jpg";

    static {
        DirectoryInitial.initial();
    }

    public static void signUpTest() {
        try {
            if (iUser.signUp(username, password, rePassword)) {
                System.out.println("User sign up successfully");
            } else {
                System.out.println("User has existed");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void checkTest() {
        System.out.println(iUser.checkIfRemember());
    }

    public static void loginTest() {
        System.out.println(iUser.login(username, password, true));
    }

    public static void updateTest() {
        System.out.println(iUser.updateAvatar(username, newAvatarPath));
    }

    public static void getTest() {
        iUser.getAvatar(username);
    }

    public static void main(String[] args) {
//        signUpTest();
//        checkTest();
//        loginTest();
//        updateTest();
//        getTest();
    }

}
