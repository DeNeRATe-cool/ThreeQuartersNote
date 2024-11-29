package database.user;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class JsonUtils {
    /**
     * create json file if not exist
     * otherwise, initial the json file
     * {username: "", password: ""}
     * @param directory cache/json directory
     * @param filename file name
     */
    public static void createJsonFile(String directory, String filename) {
        File directoryFile = new File(directory);
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }

        File jsonFile = new File(directory, filename);
        if(jsonFile.exists()) {
            return;
        }

        JSONObject initialJsonObject = new JSONObject(Map.of("username", "", "password", ""));
        try(FileWriter fileWriter = new FileWriter(jsonFile)) {
            fileWriter.write(initialJsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write content to the json file
     * @param directory directory
     * @param filename file name
     * @param jsonObject to be written json object
     */
    public static void writeJsonFile(String directory, String filename, JSONObject jsonObject) {
        File jsonFile = new File(directory, filename);

        try(FileWriter fileWriter = new FileWriter(jsonFile)) {
            fileWriter.write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * read json file in given path
     * as Map<String, Object> class
     * @param directory cache/json for example
     * @param filename file name
     * @return Map<String, Object> content
     */
    public static Map<String, Object> readJsonFile(String directory, String filename) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(directory, filename)));
            JSONObject jsonObject = new JSONObject(content);
            return jsonObject.toMap();
        } catch (IOException e) {
            e.printStackTrace();
            return Map.of();
        }
    }
}
