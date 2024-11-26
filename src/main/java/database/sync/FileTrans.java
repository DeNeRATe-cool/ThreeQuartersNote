package database.sync;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileTrans {

    /**
     * transform .md file to SyncNote object
     * @param relativePath relative path to the root
     * @param noteTitle note title
     * @param content content of the .md file
     * @param username online username
     * @return object SyncNote
     */
    public static SyncNote md2SyncNote(String relativePath, String noteTitle, String content, String username) {
        if(!content.trim().startsWith("---")) {
            return new SyncNote(username, noteTitle, relativePath, new Date(), content.trim());
        }

        String title = content.split("---")[1];
        String text = content.split("---")[2].trim();

        Map<String, String> map = new HashMap<>();
        String[] lines = title.trim().split("\n");
        for(String line : lines) {
            String[] keyVal = line.split(":", 2);
            if(keyVal.length == 2) {
                map.put(keyVal[0].trim(), keyVal[1].trim());
            }
        }

        SyncNote note = new SyncNote(username, noteTitle, relativePath, new Date(), text);

        for(Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "course" -> note.setCourse(value);
                case "uuid" -> note.setUuid(value);
            }
        }

        return note;
    }
}
