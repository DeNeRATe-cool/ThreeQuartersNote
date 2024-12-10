package org.threeQuarters;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import scrapper.IVideoCrawlable;
import scrapper.VideoExecutor;

public class TestThread extends Application {

    static String number = "22374271"; // e.g. 22375080
    static String password = "buAA101199@.@";
    static String name = "杨佳宇轩";

    static String course = "计算机硬件基础（软件专业）";
    static String teacher = "牛建伟";
    static String timeTable = "第3周星期2第3,4节";

    @Override
    public void start(Stage primaryStage) {
        // 创建按钮
        Button btn = new Button("Click Me!");

        Thread thread = new Thread(()->{
            try {
                IVideoCrawlable crawler = new VideoExecutor();

                crawler.initial(number, password, name);

                crawler.login();
                System.out.println(crawler.getCourseList());
                crawler.searchCourse(course);
                System.out.println(crawler.getTeachers());
                crawler.gotoTargetTeacherCourse(teacher);
                System.out.println(crawler.getCourseTimeTable());
                crawler.gotoCourseTime(timeTable);

//            crawler.downloadCourseVideo();
            } finally {
//                crawler.quit();
            }
        });

        thread.setDaemon(true);
        // 为按钮设置事件处理器
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // 在按钮点击时打印信息
                System.out.println("Button was clicked!");
                thread.start();
            }
        });

        // 创建布局并将按钮添加到布局中
        StackPane root = new StackPane();
        root.getChildren().add(btn);

        // 创建场景并将其设置到舞台
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("JavaFX Button Click Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
