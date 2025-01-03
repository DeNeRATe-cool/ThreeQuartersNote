package org.threeQuarters.example;

import org.threeQuarters.database.DirectoryInitial;
import org.threeQuarters.database.sync.ISync;
import org.threeQuarters.database.sync.SyncAction;

import java.nio.file.Paths;

public class SyncExample {

    public static void main(String[] args) {
        DirectoryInitial.initial();

        ISync syncManager = new SyncAction();
        String rootPath = Paths.get(Paths.get("").toAbsolutePath().toString(), "MyNoteDir").toString();
        String targetPath = "C:\\Users\\12298\\Desktop\\temp\\notes\\sync";
        String username = "杨佳宇轩";

        syncManager.backUp(rootPath, username);
        syncManager.sync(targetPath, username);
    }
}
