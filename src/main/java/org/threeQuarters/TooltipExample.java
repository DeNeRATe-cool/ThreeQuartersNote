package org.threeQuarters;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TooltipExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建一个按钮
        Button button = new Button("Hover over me");

        // 创建 Tooltip
        Tooltip tooltip = new Tooltip("This is a tooltip");

        // 设置 Tooltip 的样式
        tooltip.setStyle("-fx-background-color: #333333; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");

        // 设置显示和隐藏的延迟时间
        tooltip.setShowDelay(Duration.millis(200)); // 200 毫秒后显示
        tooltip.setHideDelay(Duration.millis(200)); // 鼠标移开 200 毫秒后隐藏

        // 将 Tooltip 添加到按钮
        button.setTooltip(tooltip);

        // 创建布局
        StackPane root = new StackPane(button);
        Scene scene = new Scene(root, 300, 200);

        // 设置舞台
        primaryStage.setTitle("Tooltip Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
