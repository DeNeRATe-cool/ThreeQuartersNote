package org.threeQuarters.FileMaster;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.threeQuarters.database.note.Note;
import org.threeQuarters.toolkit.SimpleInputDialog;

public class ModifyShareNoteDialog {

    Note note;

    Stage modifyStage;

    FileData file;

    public ModifyShareNoteDialog(Note note, FileData file) {
        file = file;
        this.note = note;

        modifyStage = new Stage();
        modifyStage.initModality(Modality.APPLICATION_MODAL);
        modifyStage.setTitle("Modify Note");

        // 创建主布局
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(15);

        // 设置对齐
        gridPane.setAlignment(Pos.CENTER);

//        HBox courseNameBox = new HBox();

        Label courseName = new Label("Course Name");
        TextField courseNameText = new TextField();
        courseNameText.setPromptText(note.getCourse());

        Button confirmButton = getConfirmButton(courseNameText);

        gridPane.add(courseName, 0, 0);
        gridPane.add(courseNameText, 1, 0);

        gridPane.add(confirmButton, 2, 0);

        Scene scene = new Scene(gridPane);
        modifyStage.setScene(scene);
        scene.getStylesheets().add(SimpleInputDialog.class.getResource("/loginFace.css").toExternalForm());

    }

    public void show()
    {
        modifyStage.show();
    }



    private Button getConfirmButton(TextField courseNameText){
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {

            if(!courseNameText.getText().isEmpty()){
                System.out.println(courseNameText.getText());
                note.setCourse(courseNameText.getText());
                modifyStage.close();
            }

        });

        return confirmButton;
    }

}
