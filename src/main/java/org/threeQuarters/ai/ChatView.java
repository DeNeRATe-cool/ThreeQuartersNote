package org.threeQuarters.ai;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import org.threeQuarters.util.Utils;

public class ChatView extends VBox {
    private VBox messageContainer;
    private TextField inputField;
    private ScrollPane scrollPane;


    public ChatView() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setStyle("-fx-background-color: #FFFFFF;");


        // 消息容器
        messageContainer = new VBox(10);
        messageContainer.setPadding(new Insets(10));

        // 滚动面板
        scrollPane = new ScrollPane(messageContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #FFFFFF; -fx-border-color: #FFFFFF;");

        // 输入区域
        HBox inputArea = new HBox(10);
        inputArea.setAlignment(Pos.CENTER);

        inputField = new TextField();
        inputField.setPromptText("输入消息...");
        inputField.setPrefHeight(40);
        inputField.setStyle(
                "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-color: #E8E8E8;" +
                        "-fx-background-color: #F8F8F8;");

        Button sendButton = new Button("发送");
        sendButton.setStyle(
                "-fx-background-color: #0095F6;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-min-width: 100;");

        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputArea.getChildren().addAll(inputField, sendButton);

        this.getChildren().addAll(scrollPane, inputArea);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // 发送消息的事件处理
        sendButton.setOnAction(e -> sendMessage());
        inputField.setOnAction(e -> sendMessage());

        AIService.clear();
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            addUserMessage(message);
            inputField.clear();

            // 创建新线程处理AI响应
            new Thread(() -> {
                Text text = new Text();
                Platform.runLater(() -> addAIMessage(text));
                AIService.chat(message, content -> Platform.runLater(() -> updateAIMessage(text, content)));
            }).start();
        }
    }

    private void addUserMessage(String message) {
        HBox messageBox = new HBox(10);
        messageBox.setAlignment(Pos.CENTER_RIGHT);

        Text text = new Text(message);
        text.setWrappingWidth(300);

        VBox textBox = new VBox(text);
        textBox.setStyle(
                "-fx-background-color: #0095F6;" +
                        "-fx-padding: 10;" +
                        "-fx-background-radius: 15;");
        text.setFill(Color.WHITE);

        messageBox.getChildren().add(textBox);
        messageContainer.getChildren().add(messageBox);
        scrollToBottom();
    }

    private void addAIMessage(Text text) {

        WebView webView = new WebView();
        webView.setContextMenuEnabled(false);

        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        // 创建 ScrollPane，放入文本
        ScrollPane sbscrollPane = new ScrollPane(webView);
//        sbscrollPane.setContent(webView);

        // 初始时隐藏滚动条
        sbscrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sbscrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // 监听鼠标进入事件，显示滚动条
        webView.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
            System.out.println("enter");
            sbscrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // 显示垂直滚动条
            sbscrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // 显示水平滚动条
            sbscrollPane.setVisible(true);
        });

        // 监听鼠标离开事件，隐藏滚动条
        webView.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
            System.out.println("left");
            sbscrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // 隐藏垂直滚动条
            sbscrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // 隐藏水平滚动条
            sbscrollPane.setVisible(false);
        });

        text.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                com.vladsch.flexmark.util.ast.Document document = parser.parse(text.getText());

                String htmlContent = renderer.render(document);
                webView.getEngine().loadContent(htmlContent);
                webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        Platform.runLater(() -> {
                            Integer height = (Integer) webView.getEngine().executeScript("document.body.scrollHeight");
                            Integer width = (Integer) webView.getEngine().executeScript("document.body.scrollWidth");

                            if (height != null && width != null) {
                                webView.setPrefHeight(20+20*Utils.countLines(text.getText()));
                                webView.setPrefWidth(width);
                            }
                        });
                    }
                });
                sbscrollPane.setVvalue(1.0);


//                WebEngine webEngine = webView.getEngine();
//
//                webEngine.loadContent(text.getText());

                // 监听页面加载完成
//                webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
//                    if (newState == Worker.State.SUCCEEDED) {
//                        Platform.runLater(() -> {
//                            // 使用 JavaScript 获取内容的完整高度
//                            Integer contentHeight = (Integer) webEngine.executeScript("document.body.scrollHeight");
//                            System.out.println(contentHeight);
////                            // 设置 WebView 高度
////                            if (contentHeight != null) {
////                                webView.setPrefHeight(contentHeight);
////                                System.out.println("WebView 高度设置为: " + contentHeight);
////                            }
//                        });
//                    }
//                });

            }
        });
//        com.vladsch.flexmark.util.ast.Document document = parser.parse(text.getText());

//        messageContainer.getChildren().add(new HBox(webView));

        HBox messageBox = new HBox(10);
        messageBox.setAlignment(Pos.CENTER_LEFT);

        text.setWrappingWidth(300);

        VBox textBox = new VBox(text);
        textBox.setStyle(
                "-fx-background-color: #F0F0F0;" +
                        "-fx-padding: 10;" +
                        "-fx-background-radius: 15;");

        messageBox.getChildren().add(textBox);
        messageContainer.getChildren().add(messageBox);
        scrollToBottom();
    }

    private void updateAIMessage(Text text, String content) {
        text.setText(content);
        scrollToBottom();
    }

    private void scrollToBottom() {
        scrollPane.setVvalue(1.0);
    }
}