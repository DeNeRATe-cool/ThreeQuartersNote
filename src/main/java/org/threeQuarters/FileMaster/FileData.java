package org.threeQuarters.FileMaster;

import org.threeQuarters.util.NoteManagerUtil;
import org.threeQuarters.util.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileData implements ILocalFile{

    private Boolean isShared;
    private String content;
    File file;
    public FileData(File file) throws IOException {
        this.file = file;
        isShared = false;
        if(Utils.fileOpenAble(file) && Utils.isTextFile(file))
        {
            this.content = (new String(Files.readAllBytes(Path.of(file.getAbsolutePath()))));
        }
    }

    public FileData(boolean Shared,String content,String path) throws IOException {
        this.file = myFileUtil.createFileWithContent(path,content);
        this.isShared = Shared;
        FileManager.getInstance().refresh();
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean isShared() {
        return isShared;
    }

    @Override
    public void setShared(boolean shared) {
        this.isShared = shared;
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public String getName()
    {
        return file.getName();
    }

}
