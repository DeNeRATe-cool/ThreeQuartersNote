package org.threeQuarters.projects;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.threeQuarters.options.Options;
import org.threeQuarters.util.MessageBox;
import org.threeQuarters.scrapper.IVideoCrawlable;
import org.threeQuarters.scrapper.VideoExecutor;

import java.util.List;

public class ProjectVideoDownloader {

    private static ProjectVideoDownloader instance;

    private BorderPane borderPane;

    private VBox vBox;

    private static List<String> courseList;

    private static IVideoCrawlable crawler;

    private Thread loginThread;

    private Label label;
    private Button goButton;
    private Button goSelectCourseButton;
    private Button goSelectTeacherButton;
    private Button goSelectTimeTableButton;
    private Button restartButton;
    private Button downloadButton;

    private String videoName;

    private ProgressBar progressBar;

    private Thread courseSelectorThread;
    private Thread teacherSelectorThread;
    private Thread timeTableSelectorThread;
    private Thread videoDownloaderThread;
    private Thread videoToAiMdThread;

    private static String courseName;
    private static String teacherName;
    private static String timeTableName;

    private ComboBox comboBox;

    private ProjectVideoDownloader(){
        borderPane = new BorderPane();
        borderPane.setPrefWidth(200.0);

        // 设置浅色边框和背景颜色
        borderPane.setStyle("-fx-background-color: lightblue; " +  // 背景颜色
                "-fx-border-color: lightgray; " +  // 浅色边框颜色
                "-fx-border-width: 2; " +         // 边框宽度
                "-fx-border-radius: 15; " +       // 圆角半径
                "-fx-background-radius: 15;");    // 背景圆角半径

        vBox = creatVBox();
        borderPane.setCenter(vBox);
        progressBar = new ProgressBar(ProgressBar.INDETERMINATE_PROGRESS);
        getLoginThread();
        goSelectCourseButton = creatGoSelectCourseButton();
        goSelectTeacherButton = creatGoSelectTeacherButton();
        goSelectTimeTableButton = creatGoSelectTimeTableButton();
        restartButton = creatRestartButton();
        downloadButton = creatDownLoadButton();

        setPro0Face();
    }

    private void restart()
    {
        if(crawler != null)
        {
            crawler.quit();
            crawler = null;
        }
        setPro0Face();
    }

    private Button creatRestartButton(){
        Button button = new Button("Restart");
        button.setId("button2");
        button.setOnAction((ActionEvent event) -> {
            restart();
        });
        return button;
    }

    private VBox creatVBox(){
        VBox vbox = new VBox();
        // 设置垂直居中对齐
        vbox.setAlignment(Pos.CENTER);

        // 设置子元素之间的间距
        vbox.setSpacing(20);
        return vbox;
    }

    private Button creatDownLoadButton(){
        Button button = new Button();

        Image img = new Image(getClass().getResource("/images/pull_downButton.png").toExternalForm());
        javafx.scene.image.ImageView icon = new ImageView(img);
        icon.setFitHeight(20);
        icon.setFitWidth(16);
        button.setGraphic(icon);

        button.setOnAction((ActionEvent event) -> {
            creatVideoDownloadThread();
            videoDownloaderThread.start();
//            creatVideoToAiMdThread();
//            videoToAiMdThread.start();
        });
        return button;
    }

    private Button creatGoSelectTimeTableButton(){
        Button button = new Button("选择课程时间>>");
        button.setId("button2");
        button.setOnAction((ActionEvent event) -> {
            creatTimeTableSelectorThread();
            timeTableSelectorThread.start();

        });
        return button;
    }

    private Button creatGoSelectTeacherButton(){
        Button button = new Button("选择任课教师>>");
        button.setId("button2");
        button.setOnAction((ActionEvent event) -> {
            creatTeacherSelectorThread();
            teacherSelectorThread.start();
        });
        return button;
    }


    private void getLoginThread()
    {
        loginThread = new Thread(()->{
            try {
                crawler = new VideoExecutor();

                crawler.initial(Options.getBuaaID(), Options.getBuaaPassword(), "");

                crawler.login();
                System.out.println("get course list");
                courseList = crawler.getCourseList();
                System.out.println(courseList);

                Platform.runLater(()->{
                    if(courseList==null || courseList.isEmpty()){
                        new MessageBox("","","登录异常");
                    }
                    else
                    {
                        new MessageBox("", "", "欢迎来到 3/4 notes " + Options.getBuaaID());

                        vBox.getChildren().add(goSelectCourseButton);
                    }
                    goButton.setDisable(false);
                    vBox.getChildren().remove(progressBar);
                    vBox.getChildren().remove(goButton);
                    vBox.getChildren().remove(label);
                });
//            crawler.downloadCourseVideo();
            } finally {

//                crawler.quit();
            }
        });
    }

    private Button creatGoSelectCourseButton(){
        Button button = new Button("选择课程>>");
        button.setId("button2");
        button.setOnAction((ActionEvent event) -> {
            creatCourseSelectorThread();
            courseSelectorThread.start();
        });
        return button;
    }

    private void creatVideoToAiMdThread()
    {
        videoToAiMdThread = new Thread(()->{
            ProjectGenerateAINote projectGenerateAINote = new ProjectGenerateAINote();
            projectGenerateAINote.executeVideoAInote(courseName,videoName);
            Platform.runLater(()->{
                vBox.getChildren().clear();
                vBox.getChildren().add(new Label("正在生成智能笔记"));
                vBox.getChildren().add(new Label("请耐心等待！"));
                vBox.getChildren().add(progressBar);
            });
        });
    }

    private void creatVideoDownloadThread(){
        videoDownloaderThread = new Thread(()->{
            Platform.runLater(()->{
//                comboBox.setDisable(true);
                vBox.getChildren().add(new Label("downloading. . ."));
                vBox.getChildren().remove(downloadButton);
                vBox.getChildren().add(progressBar);
                vBox.getChildren().remove(restartButton);
            });
            crawler.gotoCourseTime(timeTableName);
            videoName = crawler.downloadCourseVideo();
            crawler.quit();

            Platform.runLater(()->{
                new MessageBox("","","正在生成智能笔记\n"+courseName+" \n/ "+teacherName+" \n/ "+timeTableName+"\n"+"请耐心等待哦");
            });
            ProjectGenerateAINote projectGenerateAINote = new ProjectGenerateAINote();
            System.out.println("*courseName"+courseName);
            System.out.println("*videoName"+videoName);
            projectGenerateAINote.executeVideoAInote(courseName,videoName);
            Platform.runLater(this::restart);
        });
    }

    private void creatCourseSelectorThread()
    {
        courseSelectorThread = new Thread(()->{
            Platform.runLater(()->{
                vBox.getChildren().clear();
                vBox.getChildren().add(new Label("选择课程"));
                ComboBox<String> comboBox = new ComboBox<>();
                if(crawler == null)Thread.currentThread().interrupt();
                if(courseList == null || courseList.isEmpty()){Thread.currentThread().interrupt();}
                comboBox.getItems().addAll(courseList);
                vBox.getChildren().add(comboBox);
                comboBox.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (newValue != null && !newValue.isEmpty()) {
                        if(!vBox.getChildren().contains(goSelectTeacherButton)) vBox.getChildren().add(goSelectTeacherButton);
                    }
                    courseName = newValue;
                    System.out.println(newValue);
                });
                vBox.getChildren().add(restartButton);
            });
        });
    }

    private void creatTeacherSelectorThread()
    {
        teacherSelectorThread = new Thread(()->{
            if(crawler == null)Thread.currentThread().interrupt();
            crawler.searchCourse(courseName);
            List<String> teacherList = crawler.getTeachers();
            Platform.runLater(()->{
                vBox.getChildren().clear();
                vBox.getChildren().add(new Label("选择任课老师"));
                vBox.getChildren().add(new Label(courseName));
                ComboBox<String> comboBox = new ComboBox<>();
                comboBox.getItems().addAll(teacherList);
                vBox.getChildren().add(comboBox);
                comboBox.valueProperty().addListener((observable, oldValue, newValue) ->{
                    if(newValue!=null && !newValue.isEmpty() && !vBox.getChildren().contains(goSelectTimeTableButton)){
                        vBox.getChildren().add(goSelectTimeTableButton);
                    }
                    teacherName = newValue;
                });
                vBox.getChildren().add(restartButton);
            });
        });
    }

    private void creatTimeTableSelectorThread()
    {
        timeTableSelectorThread = new Thread(()->{
            if(crawler == null)Thread.currentThread().interrupt();
            crawler.gotoTargetTeacherCourse(teacherName);
            List<String> timeTableList = crawler.getCourseTimeTable();
            Platform.runLater(()->{
                vBox.getChildren().clear();
                vBox.getChildren().add(new Label("选择课程时间"));
                vBox.getChildren().add(new Label(courseName));
                vBox.getChildren().add(new Label(teacherName));
                ComboBox<String> comboBox = new ComboBox<>();
                comboBox.getItems().addAll(timeTableList);
                vBox.getChildren().add(comboBox);
                comboBox.valueProperty().addListener((observable, oldValue, newValue) ->{
                    if(newValue!=null && !newValue.isEmpty() && !vBox.getChildren().contains(downloadButton)){
                        vBox.getChildren().add(downloadButton);
                    }
                    timeTableName = newValue;
                });
                vBox.getChildren().add(restartButton);
            });
        });
    }

    private void tryLogin()
    {
        if(Options.getBuaaID() == null || Options.getBuaaID().isEmpty()
        || Options.getBuaaPassword() == null || Options.getBuaaPassword().isEmpty())
        {
            new MessageBox("","","请先配置学工号信息");
            return;
        }
        new MessageBox("","","登陆中，请稍后");
        vBox.getChildren().add(progressBar);
        getLoginThread();
        loginThread.start();
    }


    private Button getGoButton()
    {
        goButton = new Button("Go");
        goButton.setId("button2");


        goButton.setOnAction(e -> {
            goButton.setDisable(true);
            tryLogin();
        });
        return goButton;
    }

    public BorderPane getBorderPane(){
        return borderPane;
    }

    public static ProjectVideoDownloader getInstance(){
        if(instance == null){
            instance = new ProjectVideoDownloader();
        }
        return instance;
    }

    private void setPro0Face()
    {
        vBox.getChildren().clear();
        label = new Label("点击登录");
        goButton = getGoButton();
        vBox.getChildren().add(new Label("课程录像智能总结"));
        vBox.getChildren().add(label);
        vBox.getChildren().add(goButton);
        vBox.getChildren().add(restartButton);
    }

}
