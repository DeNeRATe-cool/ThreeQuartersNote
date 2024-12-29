package org.threeQuarters.projects;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
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
import org.threeQuarters.FileMaster.FileManager;
import org.threeQuarters.options.Options;
import org.threeQuarters.toolkit.SimpleInputDialog;

import java.io.IOException;

public class ConfigureDialog {

    private static ConfigureDialog configureDialog;

    public static ConfigureDialog getConfigureDialog() {
        if(configureDialog == null) {
            configureDialog = new ConfigureDialog();
        }
        return configureDialog;
    }

    public void show(Stage stage) throws IOException {
        Stage configureStage = new Stage();
        configureStage.initModality(Modality.APPLICATION_MODAL);

        // 创建主布局
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(15);

        // 设置对齐
        gridPane.setAlignment(Pos.CENTER);

        // 用户名部分
        Label IDLabel = new Label("学工号:");
        TextField IDField = new TextField();
        IDField.setPromptText("输入学号");
        if(Options.getBuaaID()!=null)IDField.setText(Options.getBuaaID());

//        Button usernameButton = new Button("Check");

        HBox usernameBox = new HBox(10, IDField);
        usernameBox.setAlignment(Pos.CENTER_LEFT);

        // 密码部分
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        if(Options.getBuaaPassword()!=null)passwordField.setText(Options.getBuaaPassword());
//        Button passwordButton = new Button("Show");

        HBox passwordBox = new HBox(10, passwordField);
        passwordBox.setAlignment(Pos.CENTER_LEFT);

        // 姓名部分
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");

        HBox nameBox = new HBox(10, nameField);
        nameBox.setAlignment(Pos.CENTER_LEFT);

        // 配置学工号成功
        Label buaaLabel = new Label();
        buaaLabel.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.ERASER));

        Label rootFolderLabel = new Label("Root Folder:");
        TextField rootFolderField = new TextField();
//        rootFolderField.setPromptText("Enter root folder");
        rootFolderField.setEditable(false);
        rootFolderField.textProperty().bind(Options.currentRootPathProperty());
        Button rootFolderButton = FileManager.getInstance().getOpenFolderButton();
        rootFolderButton.getStylesheets().add(SimpleInputDialog.class.getResource("/loginFace.css").toExternalForm());

        HBox rootFolderBox = new HBox(10, rootFolderField, rootFolderButton);
        rootFolderBox.setAlignment(Pos.CENTER_LEFT);
        // 登录按钮
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(150);

        // 退出按钮
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(150);

        // 布局组件
        gridPane.add(IDLabel, 0, 0);
        gridPane.add(usernameBox, 1, 0);

        gridPane.add(nameLabel,0,1);
        gridPane.add(nameBox,1,1);

        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordBox, 1, 2);

        gridPane.add(rootFolderLabel, 0, 4);
        gridPane.add(rootFolderBox, 1, 4);

        gridPane.add(buaaLabel, 2, 3);
        gridPane.add(loginButton, 1, 3);

        gridPane.add(cancelButton, 1, 5);

        Scene scene = new Scene(gridPane);
        configureStage.setScene(scene);

        scene.getStylesheets().add(SimpleInputDialog.class.getResource("/loginFace.css").toExternalForm());

        // 显示对话框
        configureStage.show();

        setOnLoginButtonAction(loginButton,buaaLabel,IDField,passwordField);

        cancelButton.setOnAction(e -> {
            configureStage.close();
        });

    }

    public void setOnLoginButtonAction(Button loginButton,Label buaaLabel,TextField IDField,PasswordField passwordField)
    {

//        Crawler crawler = new Crawler();
        loginButton.setOnAction(e -> {
            buaaLabel.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.CLOUD_DOWNLOAD));
//            boolean accepted = crawler.userAccept(IDField.getText(), passwordField.getText());
//
//            if(accepted)
//            {
//                buaaLabel.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.LIGHTBULB_ALT));
//            }
//            else
//            {
//                buaaLabel.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.ERASER));
//            }
            Options.setBuaaID(IDField.getText());
            Options.setBuaaPassword(passwordField.getText());
//            Options.setUserName(nameField.getText());
//            configureStage.close();
        });
    }



}
