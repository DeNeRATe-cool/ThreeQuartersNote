package org.example;

import Database.ISync;
import Database.SyncAction;

import java.nio.file.Paths;

public class SyncExample {

    public static void main(String[] args) {
        ISync syncManager = new SyncAction();
        String rootPath = Paths.get(Paths.get("").toAbsolutePath().toString(), "MyNoteDir").toString();
        String targetPath = "C:\\Users\\12298\\Desktop\\temp\\notes\\sync";
        String username = "庄耿雄";

        syncManager.backUp(rootPath, username);
        syncManager.sync(targetPath, username);
    }
}
