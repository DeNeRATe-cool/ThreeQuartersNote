package org.threeQuarters.projects;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.threeQuarters.options.Options;
import org.threeQuarters.util.MessageBox;
import scrapper.IPPTCrawlable;
import scrapper.NoneResourcesException;
import scrapper.PPTExecutor;

import java.util.List;

public class ProjectPPTDownloader {

    private static ProjectPPTDownloader instance;

    private BorderPane borderPane;

    private VBox vBox;

    private static List<String> courseList;

    private static IPPTCrawlable crawler;

    private Thread loginThread;

    private Label label;
    private Button goButton;
    private Button goSelectCourseButton;
    private Button goSelectTimeTableButton;
    private Button restartButton;
    private Button downloadButton;

    private ProgressBar progressBar;

    private Thread courseSelectorThread;
    private Thread timeTableSelectorThread;
    private Thread pptDownloaderThread;

    private static String courseName;
    private static String timeTableName;

    private ComboBox comboBox;

    private ProjectPPTDownloader(){
        borderPane = new BorderPane();
        borderPane.setPrefWidth(200.0);
        vBox = creatVBox();
        borderPane.setCenter(vBox);
        progressBar = new ProgressBar(ProgressBar.INDETERMINATE_PROGRESS);
        goSelectCourseButton = creatGoSelectCourseButton();
        goSelectTimeTableButton = creatGoSelectTimeTableButton();
        restartButton = creatRestartButton();
        downloadButton = creatDownLoadButton();

        restart();
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
            creatPPTDownloadThread();
            pptDownloaderThread.start();
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


    private void getLoginThread()
    {
        loginThread = new Thread(()->{
            try {
                crawler = new PPTExecutor();

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

    private void creatPPTDownloadThread(){
        pptDownloaderThread = new Thread(()->{
            Platform.runLater(()->{
                vBox.getChildren().add(new Label("downloading. . ."));
                vBox.getChildren().remove(downloadButton);
                vBox.getChildren().add(progressBar);
                vBox.getChildren().remove(restartButton);
            });
            try {
                crawler.downloadPPT(timeTableName);
            } catch (NoneResourcesException e) {
                Platform.runLater(()->{
                    new MessageBox("", "", "这节课没有ppt资源");
                    restart();
                });
                throw new RuntimeException(e);
            }
            crawler.quit();
            Platform.runLater(()->{
                new MessageBox("","","正在生成智能笔记\n"+courseName+" \n/ "+timeTableName+"\n"+"请耐心等待哦");
                restart();
            });
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
                        if(!vBox.getChildren().contains(goSelectTimeTableButton)) vBox.getChildren().add(goSelectTimeTableButton);
                    }
                    courseName = newValue;
                    System.out.println(newValue);
                });
                vBox.getChildren().add(restartButton);
            });
        });
    }

    private void creatTimeTableSelectorThread()
    {
        timeTableSelectorThread = new Thread(()->{
            if(crawler == null)Thread.currentThread().interrupt();
            crawler.gotoTargetCourse(courseName);
            List<String> timeTableList = crawler.getTimeTable();
            Platform.runLater(()->{
                vBox.getChildren().clear();
                vBox.getChildren().add(new Label("选择课程时间"));
                vBox.getChildren().add(new Label(courseName));
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

    public static ProjectPPTDownloader getInstance(){
        if(instance == null){
            instance = new ProjectPPTDownloader();
        }
        return instance;
    }

    private void setPro0Face()
    {
        vBox.getChildren().clear();
        label = new Label("点击登录");
        goButton = getGoButton();
        vBox.getChildren().add(label);
        vBox.getChildren().add(goButton);
        vBox.getChildren().add(restartButton);
    }

}
