package org.threeQuarters;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import org.threeQuarters.controls.FileTreeCell;
import org.threeQuarters.controls.FileTreeItem;
import org.threeQuarters.controls.FileTreeView;
import org.threeQuarters.options.Options;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainWindow {

    private final Scene scene;

    // 布局容器
    BorderPane rootLayout;
    BorderPane fileLayout;
    BorderPane mainLayout;


    // manager
    FileManager fileManager;
    ModeManager modeManager;


    public MainWindow() throws IOException {

//        // 文件树
//
//        FileTreeView fileTreeView = new FileTreeView();
//
//        // 获取文件根目录
//        File rootDirectory = new File(Options.getCurrentRootPath());
//        FileTreeItem rootItem = new FileTreeItem(rootDirectory);
//
//        // 设置根目录
//        fileTreeView.setRoot(rootItem);
//
//        rootItem.refresh();
//
//        fileTreeView.setCellFactory(treeView -> new FileTreeCell());

        fileManager = FileManager.getInstance();
        modeManager = ModeManager.getInstance();

        // TextArea

        TextArea textArea = new TextArea();
        textArea.setEditable(true);

        // 文件打开
        // 创建一个按钮，点击时打开文件对话框
//        Button btnOpenFile = new Button("Open Folder");


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
        rootLayout = new BorderPane();
        fileLayout = new BorderPane();

        fileLayout.setCenter(fileManager.getFileLeftPane());
        rootLayout.setLeft(fileLayout);

        mainLayout = new BorderPane();
        mainLayout.setPrefSize(800,600);
        mainLayout.setTop(modeManager.getHBox());
        mainLayout.setCenter(fileManager.getOpenedFilesTabPane());
        mainLayout.setRight(fileManager.getMDWebView());
        rootLayout.setCenter(mainLayout);


        scene = new Scene(rootLayout);
        // 创建响应事件

        setAction();

    }


    public BorderPane getMainLayout() {
        return mainLayout;
    }

    Scene getScene(){return scene;}


    public void setAction(){
        setKeyBoardAction();



        Options.getIsWebViewOpenedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == Boolean.TRUE)
            {
                try {
                    mainLayout.setRight(FileManager.getInstance().getMDWebView());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                mainLayout.setRight(null);
            }
        });
    }

    public void setKeyBoardAction()
    {
        scene.setOnKeyPressed(event -> {
            if(event.isControlDown() && event.getCode() == KeyCode.S)
            {
                try {
                    FileManager.getInstance().saveEditingFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
