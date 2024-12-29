package org.threeQuarters.projects;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.threeQuarters.FileMaster.FileEditorTab;
import org.threeQuarters.FileMaster.FileManager;
import org.threeQuarters.ModeManager;
import org.threeQuarters.database.sync.SyncAction;
import org.threeQuarters.database.user.UserAction;
import org.threeQuarters.options.Options;
import org.threeQuarters.util.MessageBox;
import org.threeQuarters.util.Utils;

import java.io.IOException;

public class ProjectsRightOperation {

    private BorderPane rightPane;

    private VBox vbox;

    private static ProjectsRightOperation instance;

    private ToggleButton webViewButton;

    private Button shareButton;

    private Button backupButton;
    private Button syncButton;

    private ProjectsRightOperation() {
        webViewButton = ModeManager.getInstance().getOpenWebView();

        shareButton = creatShareButton();

        backupButton = creatBackupButton();

        syncButton = creatSyncButton();

        vbox = new VBox();
        putButtonsInVBox();

        rightPane = new BorderPane();

        rightPane.setCenter(vbox);



    }

    private void putButtonsInVBox()
    {
        vbox.getChildren().add(webViewButton);
        vbox.getChildren().add(shareButton);
        vbox.getChildren().add(backupButton);
        vbox.getChildren().add(syncButton);
    }

    private Button creatBackupButton()
    {
        Button backupButton = new Button();
        Image img = new Image(getClass().getResource("/images/backup_button.png").toExternalForm());
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(20);
        imageView.setFitWidth(16);
        backupButton.setGraphic(imageView);
        Utils.applyTooltip(backupButton,"备份（备份当前根目录）\n所有文件到数据库");
        setBackUpAction(backupButton);
        return backupButton;
    }


    private Button creatSyncButton()
    {
        Button syncButton = new Button();
        Image img = new Image(getClass().getResource("/images/pull.png").toExternalForm());
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(20);
        imageView.setFitWidth(16);
        syncButton.setGraphic(imageView);
        Utils.applyTooltip(syncButton,"同步（同步数据库\n所有文件到根目录）");
        setSyncAction(syncButton);
        return syncButton;
    }

    private void setSyncAction(Button syncButton)
    {
        syncButton.setOnAction(e->{
            SyncAction action = new SyncAction();

            if(UserAction.getNowUser() == null)
            {
                new MessageBox("","","请先登录");
            }
            else
            {
                System.out.printf("try sync");
                action.sync(Options.getCurrentRootPath(),UserAction.getNowUser().getUsername());
                Platform.runLater(()->{
                    new MessageBox("","","成功将数据同步到本地");
                });
            }
        });

    }

    private void setBackUpAction(Button backupButton)
    {
        backupButton.setOnAction(event -> {
            SyncAction action = new SyncAction();

            if(UserAction.getNowUser() == null)
            {
                new MessageBox("","","请先登录");
            }
            else
            {
//                System.out.println("try back up");
                action.backUp(Options.getCurrentRootPath(),UserAction.getNowUser().getUsername());
                Platform.runLater(()->{
                    new MessageBox("","","成功将本地数据备份到数据库");
                });
            }
        });

    }

    private Button creatShareButton()
    {
        Button shareButton = new Button();
        Image shareImg = new Image(getClass().getResource("/images/shareButton.png").toExternalForm());
        ImageView shareIcon = new ImageView(shareImg);
        shareIcon.setFitHeight(20);
        shareIcon.setFitWidth(16);
        shareButton.setGraphic(shareIcon);
        Utils.applyTooltip(shareButton,"推送到共享资源库");
        setShareButtonAction(shareButton);

        return shareButton;
    }

    public void setShareButtonAction(Button shareButton)
    {
        shareButton.setOnAction(e -> {
            try {
                FileEditorTab tab = FileManager.getInstance().getEditingFileTab();
                if(tab != null)tab.pushOnDataBase();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public BorderPane getRightPane() {
        return rightPane;
    }

    public static ProjectsRightOperation getInstance() {
        if (instance == null) {
            instance = new ProjectsRightOperation();
        }
        return instance;
    }

}
