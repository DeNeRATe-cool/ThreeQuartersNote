package database.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.List;

public class FileUtils {

    /**
     * file walk all .md file in the folder path
     * and get all file in SyncNote class
     * @param folderPath root folder path
     * @param username online username
     * @return list of SyncNote all in folder and subfolder
     */
    public static List<SyncNote> walkMdFile(String folderPath, String username) {
        List<SyncNote> notes = new ArrayList<>();
        Path basePath = Paths.get(folderPath);

        try(Stream<Path> paths = Files.walk(basePath)) {
            paths.filter(path -> path.toString().endsWith(".md"))
                    .forEach(path -> {
                        Path relativePath = basePath.relativize(path);
                        String content = readFileContent(path);

                        notes.add(FileTrans.md2SyncNote(relativePath.toString(), content, username));
                    });
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
        return notes;
    }

    /**
     * get content in the file of the path
     * @param path absolute path of file
     * @return string of content
     */
    public static String readFileContent(Path path) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("file read error: " + path + " - " + e.getMessage());
        }
        return content.toString();
    }
}
