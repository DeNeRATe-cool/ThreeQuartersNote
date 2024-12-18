package org.threeQuarters.projects;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.application.Platform;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.threeQuarters.FileMaster.FileManager;
import org.threeQuarters.ThreeQuartersApp;
import org.threeQuarters.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProjectLeftMenu {

    private static ProjectLeftMenu instance = new ProjectLeftMenu();

    private ArrayList<ToggleButton> leftButtons = new ArrayList<>();

    private BorderPane leftPane;
    private BorderPane toolPane;
    //
    ToggleButton projectButton;
    ToggleButton shareResourcesButton;
    ToggleButton videoDownloadButton;
    ToggleButton pptDownloadButton;
    Button configButton;

    private static final Lock lock = new ReentrantLock();

    private ProjectLeftMenu()
    {
        leftPane = new BorderPane();
        toolPane = new BorderPane();
        VBox toolBox = new VBox();
        leftPane.setLeft(toolPane);

        toolPane.setTop(toolBox);

        VBox dialogPane = new VBox();

        toolPane.setBottom(dialogPane);

        // 文件树预览
        projectButton = new ToggleButton();
        Utils.applyTooltip(projectButton,"Project");
        projectButton.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.FOLDER_OPEN_ALT));
        projectButton.setSelected(true);
        try {
            leftPane.setCenter(FileManager.getInstance().getFileLeftPane());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        toolBox.getChildren().add(projectButton);

        shareResourcesButton = getShareResourcesButton();
        toolBox.getChildren().add(shareResourcesButton);

        // 配置按钮
        configButton = new Button();
        Utils.applyTooltip(configButton, "Configure");
        // 加载图片资源
        Image configimg = new Image(getClass().getResource("/images/configure.png").toExternalForm());
        ImageView configIcon = new ImageView(configimg);
        configIcon.setFitHeight(20);
        configIcon.setFitWidth(16);
        configButton.setGraphic(configIcon);

        dialogPane.getChildren().add(configButton);

        videoDownloadButton = getVideoDownloadButton();
        toolBox.getChildren().add(videoDownloadButton);

        pptDownloadButton = creatPPTDownloadButton();
        toolBox.getChildren().add(pptDownloadButton);

        // 管理只选择一个button
        leftButtons.add(projectButton);
        leftButtons.add(shareResourcesButton);
        leftButtons.add(videoDownloadButton);
        leftButtons.add(pptDownloadButton);

        setButtonAction();
    }

    public ToggleButton getVideoDownloadButton() {
        ToggleButton toggleButton = new ToggleButton();
        Image img = new Image(getClass().getResource("/images/video_button.png").toExternalForm());
        ImageView icon = new ImageView(img);
        icon.setFitHeight(20);
        icon.setFitWidth(16);
        toggleButton.setGraphic(icon);
        return toggleButton;
    }

    private ToggleButton creatPPTDownloadButton() {
        ToggleButton toggleButton = new ToggleButton();
        Image img = new Image(getClass().getResource("/images/ppt_download_button.png").toExternalForm());
        ImageView icon = new ImageView(img);
        icon.setFitHeight(20);
        icon.setFitWidth(16);
        toggleButton.setGraphic(icon);
        return toggleButton;
    }

    public ToggleButton getShareResourcesButton() {
        ToggleButton toggleButton = new ToggleButton();
        Image img = new Image(getClass().getResource("/images/shareToggle.png").toExternalForm());
        ImageView icon = new ImageView(img);
        icon.setFitHeight(20);
        icon.setFitWidth(16);
        toggleButton.setGraphic(icon);
        return toggleButton;
    }

    public boolean isShareResourcesButtonSelected() {
        return shareResourcesButton.isSelected();
    }

    public void setShareResourcesButtonAction(ToggleButton shareResourcesButton) {
        shareResourcesButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                closeAllToggle(shareResourcesButton);
                try {
                    leftPane.setCenter(ProjectShareNote.newInstance().getBorderPane());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                leftPane.setCenter(null);
            }
        });
    }

    public void setVideoDownloadButtonAction(ToggleButton videoDownloadButton) {
        videoDownloadButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                closeAllToggle(videoDownloadButton);
                leftPane.setCenter(ProjectVideoDownloader.getInstance().getBorderPane());
            }
            else leftPane.setCenter(null);
        });
    }

    public BorderPane getLeftMenuPane()
    {
        return leftPane;
    }

    private void closeAllToggle(ToggleButton toggle)
    {
        for(ToggleButton toggleButton : leftButtons)
        {
            if(toggleButton != toggle) toggleButton.setSelected(false);
        }
    }

    private void setButtonAction()
    {
        projectButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                closeAllToggle(projectButton);
                try {
                    leftPane.setCenter(FileManager.getInstance().getFileLeftPane());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                leftPane.setCenter(null);
            }
        });

        setShareResourcesButtonAction(shareResourcesButton);
        setVideoDownloadButtonAction(videoDownloadButton);
        setPptDownloadButtonAction(pptDownloadButton);

        configButton.setOnAction(e -> {
            try {
                ConfigureDialog.getConfigureDialog().show(ThreeQuartersApp.getPrimaryStage());
//                ConfigureDialog.show(ThreeQuartersApp.getPrimaryStage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    private void setPptDownloadButtonAction(ToggleButton pptDownloadButton)
    {
        pptDownloadButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                closeAllToggle(pptDownloadButton);
                leftPane.setCenter(ProjectPPTDownloader.getInstance().getBorderPane());
            }
            else leftPane.setCenter(null);
        });
    }

    public static ProjectLeftMenu getInstance()
    {
        return instance;
    }

}
