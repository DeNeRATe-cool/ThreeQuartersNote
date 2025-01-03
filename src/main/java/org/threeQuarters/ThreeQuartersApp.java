package org.threeQuarters;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ThreeQuartersApp extends Application {

    private MainWindow mainWindow;

    private static Stage primaryStage;

    public void start(Stage primaryStage) throws Exception {

        AppConfig.loadConfig();
        this.primaryStage = primaryStage;
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // 主界面
        mainWindow = MainWindow.getMainWindow();

        primaryStage.setScene(mainWindow.getScene());
        mainWindow.getScene().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setTitle("Quarters App");

        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
