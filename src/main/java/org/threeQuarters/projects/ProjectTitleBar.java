package org.threeQuarters.projects;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import org.threeQuarters.ThreeQuartersApp;


public class ProjectTitleBar {

    BorderPane titleBar = new BorderPane();

    HBox defaultFunctionButtonsBox = new HBox();

    // 用于记录鼠标点击时的位置
    private double xOffset = 0;
    private double yOffset = 0;

    public ProjectTitleBar() {


        // 自定义标题栏
        defaultFunctionButtonsBox = new HBox();
//        // 渐变背景
//        defaultFunctionButtonsBox.setStyle("-fx-background-color: linear-gradient(to right, #feda75, #fa7e1e, #d62976, #962fbf, #4f5bd5);");

        // 按钮样式
        Button minimizeButton = createStyledButton("—");
        Button maximizeButton = createStyledButton("⬜");
        Button closeButton = createStyledButton("✖");
        closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #ff4b5c;"); // 自定义关闭按钮颜色

        // 添加事件
        minimizeButton.setOnAction(e -> ThreeQuartersApp.getPrimaryStage().setIconified(true));
        maximizeButton.setOnAction(e -> ThreeQuartersApp.getPrimaryStage().setMaximized(!ThreeQuartersApp.getPrimaryStage().isMaximized()));
        closeButton.setOnAction(e -> ThreeQuartersApp.getPrimaryStage().close());

        defaultFunctionButtonsBox.getChildren().addAll(new ProjectUserFace().getProjectUserFaceBox());
        defaultFunctionButtonsBox.getChildren().addAll(minimizeButton, maximizeButton, closeButton);

        titleBar = new BorderPane();
        titleBar.setRight(defaultFunctionButtonsBox);
        defaultFunctionButtonsBox.setPadding(new Insets(5));
        defaultFunctionButtonsBox.setSpacing(10);
        titleBar.setPadding(new Insets(5));

        // 设置自定义拖动事件
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            ThreeQuartersApp.getPrimaryStage().setX(event.getScreenX() - xOffset);
            ThreeQuartersApp.getPrimaryStage().setY(event.getScreenY() - yOffset);
        });


        titleBar.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                if (ThreeQuartersApp.getPrimaryStage().isMaximized()) {
                    ThreeQuartersApp.getPrimaryStage().setMaximized(false);
                } else {
                    ThreeQuartersApp.getPrimaryStage().setMaximized(true);
                }
            }
        });

        titleBar.setStyle("""
                -fx-background-color: linear-gradient(to right, #e0eafc, #cfdef3);
                -fx-padding: 0;
                -fx-alignment: center-left;
                -fx-border-color: #d3d3d3;
                -fx-border-width: 0 0 1 0;
                -fx-border-style: solid;
                """);

    }

    public BorderPane getTitleBox()
    {
        return titleBar;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: grey;" +
                        "-fx-font-size: 14px;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 3px;" +
                        "-fx-background-radius: 3px;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-background-color: #68D6F2;"+
                        "-fx-text-fill: black;" +
                        "-fx-font-size: 14px;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 3px;" +
                        "-fx-background-radius: 3px;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: grey;" +
                        "-fx-font-size: 14px;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 3px;" +
                        "-fx-background-radius: 3px;"
        ));
        return button;
    }

}
