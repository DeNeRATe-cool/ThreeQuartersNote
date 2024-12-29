package database;

import org.threeQuarters.database.DirectoryInitial;
import org.threeQuarters.database.sync.FileUtils;
import org.threeQuarters.database.sync.ISync;
import org.threeQuarters.database.sync.SyncAction;
import org.threeQuarters.database.sync.SyncNote;

import java.nio.file.Paths;
import java.util.List;

public class SyncTest {

    private static final ISync sync = new SyncAction();

    public static void walkMdFileTest(String rootPath, String username) {
        List<SyncNote> syncNote =  FileUtils.walkMdFile(rootPath, username);
        System.out.println("------");
        System.out.println("walk test: note info");
        syncNote.forEach(System.out::println);
        System.out.println("------");
    }

    public static void backUpTest(String rootPath, String username) {
        sync.backUp(rootPath, username);
    }

    public static void syncTest(String targetPath, String username) {
        sync.sync(targetPath, username);
    }

    public static void main(String[] args) {
        DirectoryInitial.initial();

        String rootPath = Paths.get(Paths.get("").toAbsolutePath().toString(), "MyNoteDir").toString();
        String targetPath = "C:\\Users\\12298\\Desktop\\temp\\Dir";
        String username = "杨佳宇轩";

        walkMdFileTest(rootPath, username);
//        backUpTest(rootPath, username);
//        syncTest(targetPath, username);

    }
}
