package org.threeQuarters;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.threeQuarters.controls.FileTreeCell;
import org.threeQuarters.controls.FileTreeItem;
import org.threeQuarters.controls.FileTreeView;
import org.threeQuarters.options.Options;

import java.io.File;
import java.io.IOException;

public class FileExplorer extends Application {

    public String content = "";

    public void setContent(String content)
    {
        this.content = content;
    }



    @Override
    public void start(Stage primaryStage) throws IOException {
        // 初始化配置
        AppConfig.loadConfig();


        // 文件树

        FileTreeView fileTreeView = new FileTreeView();

        File rootDirectory = new File(Options.getCurrentRootPath());
        FileTreeItem rootItem = new FileTreeItem(rootDirectory);

        fileTreeView.setRoot(rootItem);

        rootItem.refresh();

        fileTreeView.setCellFactory(treeView -> new FileTreeCell());

        // TextArea

        TextArea textArea = new TextArea();
        textArea.setEditable(false);



        // 文件打开
        // 创建一个按钮，点击时打开文件对话框
        Button btnOpenFile = new Button("打开文件");

        // 创建 DirectoryChooser 实例
        DirectoryChooser directoryChooser = new DirectoryChooser();

//        // 设置初始目录，如果需要的话
//        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));


//        btnOpenFile.setOnAction(e -> {
//            // 打开文件夹选择对话框
//            File selectedDirectory = directoryChooser.showDialog(primaryStage);
//
//            // 如果选择了文件夹
//            if (selectedDirectory != null) {
//                Options.setCurrentRootPath(selectedDirectory.getAbsolutePath());
//            }
//        });
//
//
//        // 响应文件的点击事件
//        fileTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                File selectedFile = newValue.getValue();
//                String fileName = selectedFile.getName();
//
//                FileTreeItem fileTreeItem = (FileTreeItem)newValue;
//                // 获取文件内容
//                String fileContent = fileTreeItem.getFileContent(); // 获取 userData
//
//                textArea.setText(fileContent);
//                // 更新 MainApp 中的 TextArea 或其他组件来显示文件内容
////                updateTextArea(fileName, fileContent);
//            }
//        });


        // 创建布局并将文件树视图放置在中间
        BorderPane rootLayout = new BorderPane();
        BorderPane fileLayout = new BorderPane();
        fileLayout.setCenter(fileTreeView);
        fileLayout.setTop(btnOpenFile);
        rootLayout.setLeft(fileLayout);
        rootLayout.setCenter(textArea);

        // 创建并显示场景
        Scene scene = new Scene(rootLayout, 1200, 800);

        primaryStage.setTitle("File Explorer");
        primaryStage.setScene(scene);
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
