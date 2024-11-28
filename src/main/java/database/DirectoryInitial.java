package database;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DirectoryInitial {
    public static final String absolutePath = Paths.get("").toAbsolutePath().toString();
    public static final Map<String, String> paths = new HashMap<>(Map.of("json", Paths.get(absolutePath, "cache/json").toString(),
                                                                        "image", Paths.get(absolutePath, "cache/image").toString(),
                                                                        "video", Paths.get(absolutePath, "cache/video").toString(),
                                                                        "PPT", Paths.get(absolutePath, "cache/PPT").toString()));

    public static void initial() {
        for(String path : paths.values()) {
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
