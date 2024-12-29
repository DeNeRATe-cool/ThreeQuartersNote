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
import org.threeQuarters.database.note.NoteAction;
import org.threeQuarters.database.user.User;
import org.threeQuarters.database.user.UserAction;
import org.threeQuarters.toolkit.SimpleInputDialog;
import org.threeQuarters.util.MessageBox;
import org.threeQuarters.util.NoteManagerUtil;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class CreateShareNoteDialog {

    Note note;

    Stage creatStage;

    FileData fileData;

    public CreateShareNoteDialog(FileData fileData){

        this.fileData = fileData;
        creatStage = new Stage();
        creatStage.initModality(Modality.APPLICATION_MODAL);
        creatStage.setTitle("Create Note");

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


        Button confirmButton = getConfirmButton(courseNameText);


        gridPane.add(courseName, 0, 0);
        gridPane.add(courseNameText, 1, 0);

        gridPane.add(confirmButton, 2, 0);


        Scene scene = new Scene(gridPane);
        creatStage.setScene(scene);
        scene.getStylesheets().add(SimpleInputDialog.class.getResource("/loginFace.css").toExternalForm());

    }

    public void show()
    {
        creatStage.show();
    }

    private String getUserName()
    {
        User now = UserAction.getNowUser();
        if(now == null)return null;
        else return now.getUsername();
    }

    private Button getConfirmButton(TextField courseNameText){
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            String username = getUserName();
            if(username != null){
                if(!courseNameText.getText().isEmpty()){
                    String uuid = UUID.randomUUID().toString();
                    System.out.println(courseNameText.getText());
                    String content = null;
                    try {
                        content = FileManager.getInstance().getEditingFileTab().getTextContent();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    content = NoteManagerUtil.addPrefixWithUuidAndCoursename(uuid,courseNameText.getText(),content);
                    Note now = new Note(fileData.getName(),courseNameText.getText(), username, new Date(), content,uuid);

                    try {
                        FileManager.getInstance().getEditingFileTab().setPrefsContent(NoteManagerUtil.getPrefix(uuid,courseNameText.getText()));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    System.out.println(content);
                    try {
                        FileManager.getInstance().saveEditingFile();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    NoteAction.getInstance().insert(now);

                    creatStage.close();
                }
                else new MessageBox("","","input CourseName");
            }
            else
            {
                new MessageBox("","","Log In First");
            }
        });

        return confirmButton;
    }

}
