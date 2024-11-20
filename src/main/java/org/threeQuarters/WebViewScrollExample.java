package org.threeQuarters;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class WebViewScrollExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建 WebView
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // 加载一个网页
        webEngine.load("https://www.luogu.com.cn");

        // 控制滚动条：让页面滚动到页面底部
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                // 执行 JavaScript，滚动页面到底部
                webEngine.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(webView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("WebView Scroll Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
