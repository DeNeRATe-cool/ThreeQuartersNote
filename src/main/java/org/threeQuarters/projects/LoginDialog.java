package org.threeQuarters.projects;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import org.threeQuarters.FileManager;
import org.threeQuarters.options.Options;
import org.threeQuarters.toolkit.SimpleInputDialog;

import java.io.IOException;

public class LoginDialog {

    public static void show(Stage stage) throws IOException {
        Stage loginStage = new Stage();
        loginStage.initModality(Modality.APPLICATION_MODAL);

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

        HBox passwordBox = new HBox(10, passwordField);
        passwordBox.setAlignment(Pos.CENTER_LEFT);

        // 姓名部分
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");

        HBox nameBox = new HBox(10, nameField);
        nameBox.setAlignment(Pos.CENTER_LEFT);

        // 登录按钮
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(150);

        // 布局组件
        gridPane.add(IDLabel, 0, 0);
        gridPane.add(usernameBox, 1, 0);

        gridPane.add(nameLabel,0,1);
        gridPane.add(nameBox,1,1);

        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordBox, 1, 2);


        gridPane.add(loginButton, 1, 3);

        Scene scene = new Scene(gridPane);
        loginStage.setScene(scene);

        scene.getStylesheets().add(SimpleInputDialog.class.getResource("/loginFace.css").toExternalForm());

        // 显示对话框
        loginStage.show();

        loginButton.setOnAction(e -> {
            Options.setUserID(IDField.getText());
//            Options.setUserID(idBox.getValue());
            Options.setPassWord(passwordField.getText());
            Options.setUserName(nameField.getText());
            System.out.println(nameField.getText());
            System.out.println("close login dialog");
            loginStage.close();
        });

    }


    private void setButtonAction(Button loginButton, Button cancelButton,String IDtextField,String passwordField)
    {

    }


}
