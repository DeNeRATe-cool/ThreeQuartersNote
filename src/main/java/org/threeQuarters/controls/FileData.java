package org.threeQuarters.controls;

import org.threeQuarters.util.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileData {

    private String content;
    File file;
    public FileData(File file) throws IOException {
        this.file = file;
        if(Utils.fileOpenAble(file) && Utils.isTextFile(file))
        {
            this.content = (new String(Files.readAllBytes(Path.of(file.getAbsolutePath()))));
        }
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public String getName()
    {
        return file.getName();
    }

}
