package org.threeQuarters.projects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.threeQuarters.database.user.UserAction;
import org.threeQuarters.toolkit.SimpleInputDialog;
import org.threeQuarters.util.MessageBox;

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
        Label IDLabel = new Label("用户名");
//        ComboBox<String> idBox = new ComboBox<>();
//        idBox.setEditable(true);
//        idBox.setPromptText("Enter your username");
        TextField IDField = new TextField();
        IDField.setPromptText("请输入用户名");

//        Button usernameButton = new Button("Check");

        HBox usernameBox = new HBox(10, IDField);
//        HBox usernameBox = new HBox(10,idBox);
//        HBox passwordBox = new HBox(10, idBox);
        usernameBox.setAlignment(Pos.CENTER_LEFT);

        // 密码部分
        Label passwordLabel = new Label("密码");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码");
//        Button passwordButton = new Button("Show");

        // 密码确认部分
        Label repasswordLabel = new Label("确认密码");
        PasswordField repasswordField = new PasswordField();
        repasswordField.setPromptText("再次输入密码");

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
        Button signUpButton = new Button("注册");
        signUpButton.setOnAction(e -> {

            if(IDField.getText().isEmpty() || passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
                new MessageBox("","","请正确输入注册信息");
            }
            else
            {
                boolean success = UserAction.getInstance().signUp(IDField.getText(), passwordField.getText(), confirmPasswordField.getText());
                if(success)
                {
                    new MessageBox("","","注册成功\n欢迎来到 3/4 notes "+IDField.getText());
                    signUpStage.close();
                }
                else
                {
                    new MessageBox("","","注册失败");
                }
            }
        });

        return signUpButton;
    }

}
