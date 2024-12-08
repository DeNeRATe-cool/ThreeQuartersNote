package org.threeQuarters.projects;

import database.user.UserAction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.threeQuarters.toolkit.SimpleInputDialog;
import org.threeQuarters.util.ImageUtils;
import org.threeQuarters.util.MessageBox;
import org.threeQuarters.util.Utils;

import java.io.IOException;

public class SignUpDialog {

    public static void show(Stage stage) throws IOException {
        Stage signUpStage = new Stage();
        signUpStage.initModality(Modality.APPLICATION_MODAL);

        // 创建主布局
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(15);

        // 设置对齐
        gridPane.setAlignment(Pos.CENTER);


        // 用户名部分
        Label IDLabel = new Label("Username:");
//        ComboBox<String> idBox = new ComboBox<>();
//        idBox.setEditable(true);
//        idBox.setPromptText("Enter your username");
        TextField IDField = new TextField();
        IDField.setPromptText("Enter your username");

//        Button usernameButton = new Button("Check");

        HBox usernameBox = new HBox(10, IDField);
//        HBox usernameBox = new HBox(10,idBox);
//        HBox passwordBox = new HBox(10, idBox);
        usernameBox.setAlignment(Pos.CENTER_LEFT);

        // 密码部分
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
//        Button passwordButton = new Button("Show");

        // 密码确认部分
        Label repasswordLabel = new Label("Confirm Password:");
        PasswordField repasswordField = new PasswordField();
        repasswordField.setPromptText("Enter your password Again");

        HBox passwordBox = new HBox(10, passwordField);
        passwordBox.setAlignment(Pos.CENTER_LEFT);


        // 注册按钮
        Button signUpButton = getSignUpButton(IDField,passwordField,repasswordField,signUpStage);

        // 布局组件


        gridPane.add(IDLabel, 0, 0);
        gridPane.add(usernameBox, 1, 0);


        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordBox, 1, 1);

        gridPane.add(repasswordLabel, 0, 2);
        gridPane.add(repasswordField, 1, 2);

        gridPane.add(signUpButton, 1, 3);


        Scene scene = new Scene(gridPane);
        signUpStage.setScene(scene);

        scene.getStylesheets().add(SimpleInputDialog.class.getResource("/loginFace.css").toExternalForm());

        // 显示对话框
        signUpStage.show();


    }

    public static Button getSignUpButton(TextField IDField, PasswordField passwordField, PasswordField confirmPasswordField, Stage signUpStage) {
        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(e -> {

            if(IDField.getText().isEmpty() || passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
                new MessageBox("","","Please input Username \nPassword and Confirm Password!!");
            }
            else
            {
                boolean success = UserAction.getInstance().signUp(IDField.getText(), passwordField.getText(), confirmPasswordField.getText());
                if(success)
                {
                    new MessageBox("","","Sign Up Successful!!\nWelcome to 3/4,"+IDField.getText());
                    signUpStage.close();
                }
                else
                {
                    new MessageBox("","","Sign Up Failed!!");
                }
            }
        });

        return signUpButton;
    }

}
