package org.threeQuarters.projects;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.threeQuarters.ThreeQuartersApp;
import org.threeQuarters.options.Options;
import org.threeQuarters.util.Utils;

import java.io.IOException;

public class ProjectUserFace {

    private BorderPane borderPane;

    private HBox hBox;

    Label label;
    Button button;

    public ProjectUserFace()
    {
        borderPane = new BorderPane();
//        Options.setUserName("Visitor Mode");
        label = new Label(Options.getUserName());
        label.textProperty().bind(Options.getUserNameProperty());
        hBox = new HBox();


        button = new Button();
        button.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.USER));

        hBox.getChildren().add(button);
        hBox.getChildren().add(label);
        setHoverAnimation();

        borderPane.setRight(hBox);
        button.setOpacity(0);
        button.setPrefHeight(hBox.getHeight());
        button.setPrefWidth(button.getPrefHeight());
        button.alignmentProperty().set(label.getAlignment());
        Utils.applyTooltip(button,"Log in");
        button.setId("UserButton");
        label.setId("UserLabel");
        hBox.setId("UserHBox");

//        hBox.setStyle("-fx-border-color: black; " + // 边框颜色
//        "-fx-border-width: 2px; " +  // 边框宽度
//        "-fx-border-radius: 5px; " + // 边框圆角
//        "-fx-padding: 10px;");       // 内边距

        setButtonAction();

    }

    private void setButtonAction()
    {
        button.setOnAction(e -> {
            try {
                LoginDialog.show(ThreeQuartersApp.getPrimaryStage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }


    private void setHoverAnimation()
    {
        // 创建淡入动画
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), button);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // 创建淡出动画
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), button);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        // 添加鼠标事件监听器
        hBox.setOnMouseEntered(event -> fadeIn.play());
        hBox.setOnMouseExited(event -> fadeOut.play());
    }



    public HBox getProjectUserFaceBox()
    {
        return hBox;
    }


}
