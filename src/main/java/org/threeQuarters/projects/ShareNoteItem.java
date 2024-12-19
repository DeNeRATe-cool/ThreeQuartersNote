package org.threeQuarters.projects;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.threeQuarters.FileMaster.*;
import org.threeQuarters.database.note.Note;
import org.threeQuarters.util.Utils;

import java.io.IOException;

public class ShareNoteItem {
    private HBox hBox;
    private Label noteLabel;

    private Note note;

    public ShareNoteItem(Note note) {
        this.note = note;
        hBox = new HBox();
        noteLabel = new Label(note.getName());

        noteLabel.setId("noteLabel");

        Button button = getPullDownButton();

        hBox.getChildren().addAll(button, noteLabel);
        Utils.applyTooltip(hBox, note.toString());

    }

    public HBox getHBox() {
        return hBox;
    }

    private Button getPullDownButton()
    {
        Button button = new Button();
        Image img = new Image(getClass().getResource("/images/pull_downButton.png").toExternalForm());
        ImageView icon = new ImageView(img);
        icon.setFitHeight(14);
        icon.setFitWidth(14);
        button.setGraphic(icon);

        button.setOnAction(e->{
            ILocalFile fileData = new FileDataAdapter(note);
            try {
                myFileUtil.createFileWithContent(fileData.getAbsolutePath(),fileData.getContent());
                FileManager.getInstance().refresh();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return button;
    }

}
