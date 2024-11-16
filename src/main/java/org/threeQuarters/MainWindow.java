package org.threeQuarters;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import org.threeQuarters.controls.FileTreeCell;
import org.threeQuarters.controls.FileTreeItem;
import org.threeQuarters.controls.FileTreeView;
import org.threeQuarters.options.Options;

import java.io.File;
import java.io.IOException;

public class MainWindow {

    private final Scene scene;

    public MainWindow() throws IOException {

        // 文件树

        FileTreeView fileTreeView = new FileTreeView();

        // 获取文件根目录
        File rootDirectory = new File(Options.getCurrentRootPath());
        FileTreeItem rootItem = new FileTreeItem(rootDirectory);

        // 设置根目录
        fileTreeView.setRoot(rootItem);

        rootItem.refresh();

        fileTreeView.setCellFactory(treeView -> new FileTreeCell());

        // TextArea

        TextArea textArea = new TextArea();
        textArea.setEditable(true);

        // 文件打开
        // 创建一个按钮，点击时打开文件对话框
        Button btnOpenFile = new Button("打开文件");

        // 创建 DirectoryChooser 实例
        DirectoryChooser directoryChooser = new DirectoryChooser();

//        File selectedDirectory

//        // 设置初始目录，如果需要的话
//        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
//
//
//        btnOpenFile.setOnAction(e -> {
//            // 打开文件夹选择对话框
//            File selectedDirectory = directoryChooser.showDialog(primaryStage);
//
//            // 如果选择了文件夹
//            if (selectedDirectory != null) {
//                Options.setCurrentRootPath(selectedDirectory.getAbsolutePath());
//            }
//        });





        // 创建布局并将文件树视图放置在中间
        BorderPane rootLayout = new BorderPane();
        BorderPane fileLayout = new BorderPane();
        fileLayout.setCenter(fileTreeView);
        fileLayout.setTop(btnOpenFile);
        rootLayout.setLeft(fileLayout);
        rootLayout.setCenter(textArea);


        scene = new Scene(rootLayout);

    }

    Scene getScene(){return scene;}

}
