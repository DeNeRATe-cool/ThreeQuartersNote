package org.threeQuarters.projects;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.threeQuarters.FileManager;
import org.threeQuarters.util.Utils;

import java.io.IOException;

public class ProjectsFilesButtons {

    private HBox buttons;

    Button saveFileButton;
    Button newFileButton;
    Button newFolderButton;
    Button deleteFileButton;

    public ProjectsFilesButtons() {
        buttons = new HBox();
//        buttons.setSpacing(10);

        saveFileButton = new Button();
        Utils.applyTooltip(saveFileButton, "Save File");
        saveFileButton.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.SAVE));

        newFileButton = new Button();
        Utils.applyTooltip(newFileButton, "New File");
        newFileButton.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.FILE_ALT));

        newFolderButton = new Button();
        Utils.applyTooltip(newFolderButton, "New Folder");
        newFolderButton.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.FOLDER));

        deleteFileButton = new Button();
        Utils.applyTooltip(deleteFileButton, "Delete File");

        Image configimg = new Image(getClass().getResource("/images/delete.png").toExternalForm());
        ImageView configIcon = new ImageView(configimg);
        configIcon.setFitHeight(20);
        configIcon.setFitWidth(16);
        deleteFileButton.setGraphic(configIcon);

        setButtonAction();

        buttons.getChildren().addAll(newFileButton, newFolderButton,saveFileButton,deleteFileButton);

    }

    public HBox getButtons() {
        return buttons;
    }

    private void setButtonAction() {
        newFileButton.setOnAction(e -> {
            try {
                FileManager.getInstance().createNewFile();
            } catch (IOException er) {
                throw new RuntimeException(er);
            }
        });
        saveFileButton.setOnAction(e -> {
            try {
                FileManager.getInstance().saveEditingFile();
            } catch (IOException er) {
                throw new RuntimeException(er);
            }
        });
        newFolderButton.setOnAction(e -> {
            try{
                FileManager.getInstance().createNewFolder();
            }catch (IOException er){
                throw new RuntimeException(er);
            }
        });
        deleteFileButton.setOnAction(e -> {
            try {
                FileManager.getInstance().delFile();
            } catch (IOException er) {
                throw new RuntimeException(er);
            }
        });
    }

}
