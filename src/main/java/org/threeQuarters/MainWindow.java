package org.threeQuarters;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import org.threeQuarters.options.Options;

import java.io.File;
import java.io.IOException;

public class MainWindow {

    private final Scene scene;

    // 布局容器
    BorderPane rootLayout;
    BorderPane fileLayout;
    BorderPane mainLayout;

//    textArea 和 Webview 实时渲染框
    HBox editorAndWebViewPane;


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

        // 放置编辑器和 webview 渲染框
        editorAndWebViewPane = new HBox();
        editorAndWebViewPane.getChildren().add(fileManager.getOpenedFilesTabPane());

//        editorAndWebViewPane.add(fileManager.getMDWebView(),0,1);
        mainLayout.setCenter(editorAndWebViewPane);

        HBox.setHgrow(fileManager.getOpenedFilesTabPane(),Priority.ALWAYS);

//        mainLayout.setCenter(fileManager.getOpenedFilesTabPane());
//        mainLayout.setRight(fileManager.getMDWebView());
//        rootLayout.setCenter(editorAndWebViewPane);

//        borderPane.setStyle("-fx-border-color: black; " + // 边框颜色
//                "-fx-border-width: 2px; " +  // 边框宽度
//                "-fx-border-radius: 5px; " + // 边框圆角
//                "-fx-padding: 10px;");       // 内边距
//        mainLayout.setStyle("-fx-border-color: black; " + // 边框颜色
//                "-fx-border-width: 2px; " +  // 边框宽度
//                "-fx-border-radius: 5px; " + // 边框圆角
//                "-fx-padding: 10px;");       // 内边距

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
                editorAndWebViewPane.getChildren().clear();
                editorAndWebViewPane.getChildren().add(fileManager.getOpenedFilesTabPane());
                fileManager.getOpenedFilesTabPane().setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                editorAndWebViewPane.getChildren().add(fileManager.getMDWebView());
                fileManager.getMDWebView().setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            }
            else
            {
                editorAndWebViewPane.getChildren().clear();
                editorAndWebViewPane.getChildren().add(fileManager.getOpenedFilesTabPane());
                fileManager.getOpenedFilesTabPane().setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
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
            if(event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.N)
            {
                try{
                    FileManager.getInstance().createNewFolder();
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
            }
            else if(event.isControlDown() && event.getCode() == KeyCode.N)
            {
                try {
                    FileManager.getInstance().createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(event.getCode() == KeyCode.DELETE)
            {
                try {
                    FileManager.getInstance().delFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
