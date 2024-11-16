package org.threeQuarters;

import javafx.scene.control.TextArea;
import org.threeQuarters.controls.FileData;

public class FileEditor {

    FileData fileData;

    private TextArea textArea;

    public FileEditor(FileData fileData)
    {
        this.fileData = fileData;
        this.textArea = new TextArea();
        textArea.setEditable(true);
        textArea.setText(fileData.getContent());
    }

}
