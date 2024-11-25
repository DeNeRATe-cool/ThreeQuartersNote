package database;

import java.io.File;

public class DirectoryInitial {
    private static final String[] paths = {"./cache/json", "./cache/image", "./cache/video"};

    public static void initial() {
        for(String path : paths) {
            File directory = new File(path);
            if (!directory.exists()) {
                if (directory.mkdirs())
                    System.out.println("Directory created!");
                else {
                    System.out.println("Directory failed to create...");
                }
            }
        }
    }
}
