package database.sync;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileTrans {

    /**
     * transform .md file to SyncNote object
     * @param relativePath relative path to the root
     * @param Content content of the .md file
     * @param username online username
     * @return object SyncNote
     */
    public static SyncNote md2SyncNote(String relativePath, String Content, String username) {
        String title = Content.split("---")[1];
        String text = Content.split("---")[2].trim();

        Map<String, String> map = new HashMap<>();
        String[] lines = title.trim().split("\n");
        for(String line : lines) {
            String[] keyVal = line.split(":", 2);
            if(keyVal.length == 2) {
                map.put(keyVal[0].trim(), keyVal[1].trim());
            }
        }

        SyncNote note = new SyncNote(username, "未命名笔记", relativePath, new Date(), text);

        for(Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "name" -> note.setName(value);
                case "course" -> note.setCourse(value);
                case "uuid" -> note.setUuid(value);
            }
        }

        return note;
    }
}
