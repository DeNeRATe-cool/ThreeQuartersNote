package org.threeQuarters;


import javafx.application.Application;
import javafx.stage.Stage;


public class ThreeQuartersApp extends Application {

    private MainWindow mainWindow;

    public void start(Stage primaryStage) throws Exception {

        AppConfig.loadConfig();

        // 主界面
        mainWindow = new MainWindow();

        primaryStage.setScene(mainWindow.getScene());


        primaryStage.setTitle("Quarters App");

        primaryStage.show();
    }

}
