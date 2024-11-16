package org.threeQuarters;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import org.threeQuarters.controls.FileData;

import java.io.*;

public class FileEditorTab extends Tab{

    FileData fileData;

    BooleanProperty saveProperty = new SimpleBooleanProperty(true);

    private TextArea textArea;


    public FileEditorTab(FileData fileData)
    {
        super(fileData.getName());
        setClosable(true);
        this.fileData = fileData;
        this.textArea = new TextArea();
        textArea.setEditable(true);
        textArea.setText(fileData.getContent());

        setContent(textArea);
        setTextAreaChangedAction();
        updateState();
    }

    private void setTextAreaChangedAction()
    {
        this.textArea.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                saveProperty.set(false);
                updateState();
            }
        });
    }

    public void showTabLook()
    {
//        setText(fileData.getName());
        if(saveProperty.get())
        {
            setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.STAR));
        }
        else
        {
            setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.STAR_ALT));
        }
    }

    public void saveFile() throws IOException {
        File file = new File(fileData.getAbsolutePath());
        if(file != null)
        {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bytes = textArea.getText().getBytes();
            fileOutputStream.write(bytes);
//            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            System.out.println(textArea.getText());
//            writer.write(textArea.getText());
            saveProperty.set(true);
            updateState();
        }
    }

    // 更新编辑器状态
    public void updateState()
    {
        showTabLook();
    }

}
