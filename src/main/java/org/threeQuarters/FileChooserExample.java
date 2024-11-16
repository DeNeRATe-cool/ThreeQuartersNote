package org.threeQuarters;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

// 一个测试样例

public class FileChooserExample extends Application {

    public void start(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File Chooser Example");

        // 设置初始目录
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // 设置文件类型筛选器
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Button openFileButton = new Button("Open File");
        openFileButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                System.out.println("Opened file: " + selectedFile.getAbsolutePath());
            } else {
                System.out.println("No file selected");
            }
        });

        Button saveFileButton = new Button("Save File");
        saveFileButton.setOnAction(e -> {
            File fileToSave = fileChooser.showSaveDialog(primaryStage);
            if (fileToSave != null) {
                System.out.println("File saved at: " + fileToSave.getAbsolutePath());
            } else {
                System.out.println("No file saved");
            }
        });

        VBox root = new VBox(10, openFileButton, saveFileButton);
        Scene scene = new Scene(root, 400, 200);

        primaryStage.setTitle("FileChooser Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}