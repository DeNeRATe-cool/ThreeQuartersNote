package org.threeQuarters.ai;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatViewTestMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        ChatView chatView = new ChatView();
        Scene scene = new Scene(chatView, 500, 700);
        
        primaryStage.setTitle("AI聊天");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
} 