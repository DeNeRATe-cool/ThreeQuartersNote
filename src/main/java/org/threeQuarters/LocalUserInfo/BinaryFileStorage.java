package org.threeQuarters.LocalUserInfo;

import javafx.scene.image.Image;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinaryFileStorage {
    private static final String FILE_PATH = "localUser.bin";

    // 保存对象到文件
    public static void saveToFile(ArrayList<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从文件中读取对象
    public static ArrayList<User> readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void main(String[] args) {

        UserInfo.getInstance().addUser(new User("22374271","buAA"));
        UserInfo.getInstance().addUser(new User("22374272","buAA"));

        saveToFile(UserInfo.getInstance().getUsers());

        List<User> loadedUsers = readFromFile();
        loadedUsers.forEach(System.out::println);
    }
}