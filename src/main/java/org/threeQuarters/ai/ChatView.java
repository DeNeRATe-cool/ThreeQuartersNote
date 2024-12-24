package org.threeQuarters.ai;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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