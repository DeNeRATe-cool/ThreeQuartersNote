package org.threeQuarters.projects;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.threeQuarters.FileMaster.FileManager;
import org.threeQuarters.addons.LeftPaneAddon;
import org.threeQuarters.util.PrefsStringProperty;

import java.io.File;
import java.util.List;

public class ProjectFileAiToMd implements LeftPaneAddon {

    private static ProjectFileAiToMd instance;

    private VBox dragTarget;
    private VBox vBox;
    private ProgressBar progressBar;
    private Button goButton;
    private PrefsStringProperty absolutePath = new PrefsStringProperty();

    private ProjectFileAiToMd(){
        borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: lightblue; " +  // 背景颜色
                "-fx-border-color: lightgray; " +  // 浅色边框颜色
                "-fx-border-width: 2; " +         // 边框宽度
                "-fx-border-radius: 15; " +       // 圆角半径
                "-fx-background-radius: 15;");    // 背景圆角半径

        vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        progressBar = new ProgressBar(ProgressBar.INDETERMINATE_PROGRESS);
        dragTarget = getDragTarget();
        goButton = new Button();
        goButton.setId("button2");
        goButton.setText("点击生成");
        setGoButtonAction();
        set0Face();
        borderPane.setCenter(vBox);
    }

    private void setGoButtonAction()
    {
        goButton.setOnAction(e->{
            if(!absolutePath.isEmpty().get())
            {
                loadingProcess(absolutePath.get());
            }
        });
    }

    public void writeInPath(String path)
    {
        System.out.println("write in "+path);
        absolutePath.set(path);
        dragTarget.getChildren().clear();
        StringBuilder sb = new StringBuilder(path);
        for(int i = 20;i < sb.length();i+=20)
        {
            sb.insert(i,"\n");
        }
        dragTarget.getChildren().add(new Label(sb.toString()));
    }

    public VBox getDragTarget()
    {
        // 创建一个矩形作为拖拽区域
        VBox dragTarget = new VBox();
        dragTarget.setStyle(
//                "-fx-background-color: #f3f3f3;" + // 背景色
                        "-fx-border-color: #4CAF50;" +     // 边框颜色
                        "-fx-border-width: 2px;" +          // 边框宽度
                        "-fx-border-radius: 10px;" +        // 圆角
                        "-fx-padding: 20px;" +              // 内边距
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0.5, 0, 0);" // 阴影效果
        );

        // 设置 "拖入" 事件
        dragTarget.setOnDragOver(event -> {
            // 检查拖入的是否为文件
            if (event.getGestureSource() != dragTarget && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY); // 接受复制操作
            }
            event.consume();
        });

        dragTarget.setOnDragDropped(event -> {
            boolean success = false;
            if (event.getDragboard().hasFiles()) {
                List<File> files = event.getDragboard().getFiles();

                // 遍历文件并根据类型响应
                for (File file : files) {
                    String fileName = file.getName();
                    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    System.out.println("File dropped: " + file.getAbsolutePath());

                    // 根据文件类型做出不同的响应
                    switch (fileType) {
                        case "txt":
                            System.out.println("This is a text file!");
                            break;
                        case "jpg":
                        case "png":
                            System.out.println("This is an image file!");
                            break;
                        case "ppt","pptx","pdf","doc","docx":
                            System.out.println("This is a good file");
                            writeInPath(file.getAbsolutePath());
                            break;
                        default:
                            System.out.println("Unknown file type!");
                            break;
                    }
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        return dragTarget;
    }

    public void loadingProcess(String path)
    {
            Thread thread = new Thread(new GenerateMdFromFileWorker(path));
        new Thread(()->{
            Platform.runLater(()->{
                vBox.getChildren().clear();
                vBox.getChildren().add(new Label("正在生成，请稍后"));
                vBox.getChildren().add(progressBar);
            });
        }).start();
        thread.start();
    }

    public void set0Face()
    {
        Platform.runLater(()->{
            vBox.getChildren().clear();
            HBox hbox = new HBox();
            hbox.getChildren().add(new Label("打开文件资源管理器"));
            hbox.getChildren().add(FileManager.getOpenDirectoryChooserButton());
            vBox.getChildren().add(hbox);
            vBox.getChildren().add(new Label("或者 将word ppt pdf文件拖拽此处"));
            vBox.getChildren().add(dragTarget);
            vBox.getChildren().add(goButton);
        });
    }

    public static ProjectFileAiToMd getInstance()
    {
        if(instance == null)instance = new ProjectFileAiToMd();
        return instance;
    }

    private BorderPane borderPane;


    public BorderPane getBorderPane()
    {
        return borderPane;
    }
}