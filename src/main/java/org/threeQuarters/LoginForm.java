package org.threeQuarters;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginForm extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建标题
        Label title = new Label("Login");
        title.setFont(new Font("Arial", 24));

        // 创建用户名输入框
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        // 创建密码输入框
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // 创建登录按钮
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #3B5998; -fx-text-fill: white; -fx-font-size: 14px;");
        loginButton.setOnAction(e -> {
            // 模拟登录事件
            System.out.println("Username: " + usernameField.getText());
            System.out.println("Password: " + passwordField.getText());
        });

        // 创建布局容器
        VBox root = new VBox(10); // 设置组件之间的间距
        root.setPadding(new Insets(20)); // 设置内边距
        root.setAlignment(Pos.CENTER); // 使组件居中对齐
        root.getChildren().addAll(title, usernameField, passwordField, loginButton);

        // 创建场景
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}