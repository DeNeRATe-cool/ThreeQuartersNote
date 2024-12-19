package org.threeQuarters.FileMaster;

import org.threeQuarters.database.note.Note;
import org.threeQuarters.options.Options;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDataAdapter implements java.io.Serializable ,ILocalFile{

    private boolean isShared;
    private Note note;

    public  FileDataAdapter(Note note)
    {
        this.note = note;
    }


    public FileData getFileData()
    {
        try {
            return new FileData(isShared,getContent(),getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return note.getName();
    }

    @Override
    public String getAbsolutePath() {
        Path dir = Paths.get(Options.getCurrentRootPath());
        Path file = dir.resolve(note.getName());
        return file.toString();
    }

    @Override
    public String getContent() {
        return note.getContent();
    }

    @Override
    public void setContent(String content) {
        note.setContent(content);
    }

    @Override
    public boolean isShared() {
        return true;
    }

    @Override
    public void setShared(boolean shared) {
        isShared = shared;
    }
}
