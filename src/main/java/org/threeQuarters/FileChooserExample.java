package org.threeQuarters;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

// 一个测试样例

public class FileChooserExample extends Application {

    public void start(Stage primaryStage) {
        // 创建一个按钮
        Button button = new Button("Hover over me!");

        // 创建一个 Tooltip 并设置文本
        Tooltip tooltip = new Tooltip("This is a tooltip on hover");
//        Tooltip.install(button, tooltip);
        button.setTooltip(tooltip);

        // 布局
        StackPane root = new StackPane(button);
        Scene scene = new Scene(root, 300, 200);

//        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Tooltip Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}