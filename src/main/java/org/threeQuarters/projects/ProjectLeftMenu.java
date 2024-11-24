package org.threeQuarters.projects;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.threeQuarters.FileManager;
import org.threeQuarters.ThreeQuartersApp;
import org.threeQuarters.util.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectLeftMenu {

    private static ProjectLeftMenu instance = new ProjectLeftMenu();

    private ArrayList<ToggleButton> leftButtons = new ArrayList<>();

    private BorderPane leftPane;
    private BorderPane toolPane;
    //
    ToggleButton projectButton;
    Button configButton;

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

        // 管理只选择一个button
        leftButtons.add(projectButton);

        setButtonAction();
    }

    public BorderPane getLeftMenuPane()
    {
        return leftPane;
    }

    private void setButtonAction()
    {
        projectButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
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

            configButton.setOnAction(e -> {
            try {
                ConfigureDialog.show(ThreeQuartersApp.getPrimaryStage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    public static ProjectLeftMenu getInstance()
    {
        return instance;
    }

}
