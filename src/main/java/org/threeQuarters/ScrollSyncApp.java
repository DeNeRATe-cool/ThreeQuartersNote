package org.threeQuarters;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ScrollSyncApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建 TextArea 和 WebView
        TextArea textArea = new TextArea();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // 将 TextArea 内容作为 Markdown 渲染为 HTML
        textArea.textProperty().addListener((obs, oldText, newText) -> {
            String markdownContent = newText; // 获取 TextArea 中的 Markdown 内容
            String htmlContent = convertMarkdownToHtml(markdownContent); // 转换 Markdown 到 HTML
            webEngine.loadContent(htmlContent); // 在 WebView 中显示 HTML 内容
        });

        // 监听 TextArea 的滚动事件
        textArea.setOnScroll(event -> syncScroll(textArea, webView));

        // 监听 WebView 的滚动事件
        webView.setOnScroll(event -> syncScroll(webView, textArea));

        // 布局设置
        BorderPane root = new BorderPane();
        root.setTop(textArea);
        root.setCenter(webView);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Scroll Sync Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 将 Markdown 转换为 HTML 的简单示例
    private String convertMarkdownToHtml(String markdown) {
        // 使用一个简化的转换方法，实际中可以使用像 commonmark 这样的库
        return "<html><body>" + markdown.replace("\n", "<br>") + "</body></html>";
    }

    // 同步滚动条位置的方法
    private void syncScroll(Object source, Object target) {
        if (source instanceof TextArea && target instanceof WebView) {
            TextArea sourceTextArea = (TextArea) source;
            WebView targetWebView = (WebView) target;

            // 获取 TextArea 的滚动位置并同步到 WebView
            double scrollPosition = sourceTextArea.getScrollTop();
            double scrollHeight = sourceTextArea.getHeight();
            targetWebView.getEngine().executeScript("window.scrollTo(0, " + (scrollPosition / scrollHeight * 100) + ");");
        } else if (source instanceof WebView && target instanceof TextArea) {
            WebView sourceWebView = (WebView) source;
            TextArea targetTextArea = (TextArea) target;

            // 获取 WebView 的滚动位置并同步到 TextArea
            double scrollPosition = (double) sourceWebView.getEngine().executeScript("return document.documentElement.scrollTop;");
            double scrollHeight = targetTextArea.getHeight();
            targetTextArea.setScrollTop(scrollPosition / 100 * scrollHeight);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
