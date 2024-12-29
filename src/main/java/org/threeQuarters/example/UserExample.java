package org.threeQuarters.example;

import org.threeQuarters.database.DirectoryInitial;
import org.threeQuarters.database.user.IUser;
import org.threeQuarters.database.user.UserAction;

public class UserExample {
    private static final IUser iUser = new UserAction();

    public static void main(String[] args) {
        DirectoryInitial.initial();

        String username = "wow";
        String password = "123";
        String rePassword = "123";
        String newAvatarPath = "C:/Users/12298/Desktop/picture/221515-22375080-杨佳宇轩.png";

//        try {
//            if (iUser.signUp(username, password, rePassword)) {
//                System.out.println("User sign up successfully");
//            } else {
//                System.out.println("User has existed");
//            }
//        } catch (IllegalArgumentException e) {
//            System.out.println(e.getMessage());
//        }

//        System.out.println(iUser.checkIfRemember());
//        System.out.println(iUser.login(username, password, true));
//        System.out.println(iUser.checkIfRemember());

//        try {
//            System.out.println(iUser.updateAvatar(username, newAvatarPath));
//        } catch (PersistenceException e) {
//            System.out.println("size of image is TOO large");
//        }

//        iUser.getAvatar(username);
    }
}
