package org.threeQuarters;

import javafx.application.Application;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;

public class InputDialogExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建输入对话框
        TextInputDialog inputDialog = new TextInputDialog("Default Value"); // 设置默认值
        inputDialog.setTitle("输入框");
        inputDialog.setHeaderText("请输入文件名");
        inputDialog.setContentText("文件名：");

        // 显示对话框并等待用户输入
        Optional<String> result = inputDialog.showAndWait();

        // 获取用户输入的结果
        result.ifPresent(input -> System.out.println("用户输入的内容：" + input));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
