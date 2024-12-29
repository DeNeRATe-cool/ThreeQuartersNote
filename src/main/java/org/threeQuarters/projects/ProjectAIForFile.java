package org.threeQuarters.projects;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.threeQuarters.FileMaster.FileManager;
import org.threeQuarters.ai.AIService;
import org.threeQuarters.ai.ChatView;
import org.threeQuarters.util.Utils;

import java.io.IOException;

public class ProjectAIForFile {

    private static ProjectAIForFile instance;

    private BorderPane borderPane;

    private ChatView chatView;

    private Button freshButton;
    private HBox uphbox;
    private String title;

    private ProjectAIForFile()
    {

        uphbox = new HBox();
        chatView = new ChatView();

        borderPane = new BorderPane();
        borderPane.setPrefWidth(400.0);

        // 设置浅色边框和背景颜色
        borderPane.setStyle("-fx-background-color: lightblue; " +  // 背景颜色
                "-fx-border-color: lightgray; " +  // 浅色边框颜色
                "-fx-border-width: 2; " +         // 边框宽度
                "-fx-border-radius: 15; " +       // 圆角半径
                "-fx-background-radius: 15;");    // 背景圆角半径

        freshButton = creatFlushButton();
//        borderPane.setTop(freshButton);
        uphbox.getChildren().add(freshButton);
        uphbox.getChildren().add(new Label("Ai Chatting"));

        // 设置子元素之间的间距
        uphbox.setSpacing(20);

        borderPane.setTop(uphbox);
        borderPane.setCenter(chatView);
    }

    private Button creatFlushButton()
    {
        Button flushButton = new Button();
        Image img = new Image(getClass().getResource("/images/flush.png").toExternalForm());
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(20);
        imageView.setFitWidth(16);
        flushButton.setGraphic(imageView);
        Utils.applyTooltip(flushButton,"刷新");
        setFlushAction(flushButton);
        return flushButton;
    }

    private void setFlushAction(Button flushButton) {
        flushButton.setOnAction(event -> {
            borderPane.setCenter(null);
            String newtitle = null;
            try {
                if(FileManager.getInstance().getEditingFileTab()!=null)
                {
                    AIService.setContent(FileManager.getInstance().getEditingFileTab().getTextContent());
                    AIService.updateAiServiceInstance();
                    newtitle = FileManager.getInstance().getEditingFileTab().getFileData().getName();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(newtitle != null && !newtitle.isEmpty())title = newtitle;
            else title = "Ai Chatting";
            chatView = new ChatView();
            uphbox.getChildren().clear();
            uphbox.getChildren().add(flushButton);
            uphbox.getChildren().add(new Label(title));
            borderPane.setTop(uphbox);
            borderPane.setCenter(chatView);

        });
    }

    public static ProjectAIForFile getInstance()
    {
        if(instance == null)instance = new ProjectAIForFile();
        return instance;
    }

    public BorderPane getBorderPane()
    {
        return borderPane;
    }

}
