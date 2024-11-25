package org.example;

import database.DirectoryInitial;
import database.user.IUser;
import database.user.UserAction;

public class UserExample {
    private static final IUser iUser = new UserAction();

    public static void main(String[] args) {
        DirectoryInitial.initial();

        String username = "admin";
        String password = "abc";
        String rePassword = "abc";
        String newAvatarPath = "C:/Users/12298/Desktop/picture/正装照 - 副本.jpg";

        try {
            if (iUser.signUp(username, password, rePassword)) {
                System.out.println("User sign up successfully");
            } else {
                System.out.println("User has existed");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(iUser.checkIfRemember());
        System.out.println(iUser.login(username, password, true));

        System.out.println(iUser.updateAvatar(username, newAvatarPath));

        iUser.getAvatar(username);
    }
}
