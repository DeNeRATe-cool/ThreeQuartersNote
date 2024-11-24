package org.threeQuarters.LocalUserInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class UserInfo implements Serializable {

    ArrayList<User>users;

    private static UserInfo instance;

    private UserInfo()
    {
        users = new ArrayList<>();
    }


    public static UserInfo getInstance() {
        if (instance == null) {
            instance = new UserInfo();
        }
        return instance;
    }

    public static ArrayList<User> getUsers() {
        return instance.users;
    }

    public static void addUser(User user) {
        instance.users.add(user);
    }

    public static void removeUser(User user) {
        instance.users.remove(user);
    }

}
