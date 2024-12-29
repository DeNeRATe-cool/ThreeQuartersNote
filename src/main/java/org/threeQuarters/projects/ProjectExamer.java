package org.threeQuarters.projects;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.threeQuarters.FileMaster.FileManager;
import org.threeQuarters.addons.LeftPaneAddon;
import org.threeQuarters.ai.AIExam;
import org.threeQuarters.ai.Question;
import org.threeQuarters.util.MessageBox;
import org.threeQuarters.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectExamer implements LeftPaneAddon {

    BorderPane borderPane;
    VBox mainlayout;
    private static ProjectExamer instance;
    private Button goButton;
    private static ArrayList<ExamBox> boxes;
    private Button flushButton;
    private HBox topLayout;
    private Button checkButton;
    private Button newFileButton;

    private ProjectExamer()
    {
        borderPane = new BorderPane();

        borderPane.setPrefWidth(400.0);

        // 设置浅色边框和背景颜色
        borderPane.setStyle("-fx-background-color: lightblue; " +  // 背景颜色
                "-fx-border-color: lightgray; " +  // 浅色边框颜色
                "-fx-border-width: 2; " +         // 边框宽度
                "-fx-border-radius: 15; " +       // 圆角半径
                "-fx-background-radius: 15;");    // 背景圆角半径

        mainlayout = new VBox();
        topLayout = new HBox();

        // 设置垂直居中对齐
        mainlayout.setAlignment(Pos.CENTER);

        // 设置子元素之间的间距
        mainlayout.setSpacing(20);
        borderPane.setCenter(mainlayout);
        goButton = new Button();
        goButton.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.LIGHTBULB_ALT));
        setGoButtonAction();

        checkButton = new Button();
        checkButton.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.CHECK));
        setCheckButtonAction();
        Utils.applyTooltip(checkButton,"批改");

        flushButton = new Button();
        flushButton.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.FLASH));
        flushButton.setOnAction((ActionEvent event) -> {
            set0Face();
        });
        Utils.applyTooltip(flushButton, "点击刷新");

        newFileButton = new Button();
        newFileButton.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.PENCIL));
        newFileButton.setOnAction(e->{
            if(boxes != null && !boxes.isEmpty())
            {
                creatMdFile();
            }
            else
            {
                new MessageBox("","","题库为空");
            }
        });
        Utils.applyTooltip(newFileButton,"保存为md");

        topLayout.getChildren().add(flushButton);
        topLayout.getChildren().add(checkButton);
        topLayout.getChildren().add(newFileButton);
        borderPane.setTop(topLayout);
        boxes = new ArrayList<>();
        set0Face();
    }

    public void creatMdFile()
    {
        StringBuilder sb = new StringBuilder();
        for(ExamBox box : boxes)
        {
            sb.append(box.getQuestion().toString()+"\n");
        }
        try {
            FileManager.getInstance().createNewFile(new String(sb));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void setCheckButtonAction()
    {
        checkButton.setOnAction(e->{
            if(boxes == null || boxes.isEmpty())
            {
                new MessageBox("","","题库为空");
            }
            else {
                boolean fl = true;
                for(ExamBox box:boxes)
                {
                    if(!box.getBox().isSelectBoolean())
                    {
                        fl = false;
                        break;
                    }
                }
                if(fl == false)
                {
                    new MessageBox("","","还有题目没有完成");
                }
                else
                {
                    int num = boxes.size();
                    int cnt = 0;
                    for(ExamBox box:boxes)
                    {
                        if (box.isCorrectBoolean()) {
                            cnt++;
                        }
                        box.setEnableBoolean(false);
                    }
                    VBox vb = new VBox();
                    ScrollPane scrollPane = new ScrollPane(vb);
                    vb.setSpacing(15);
                    vb.getChildren().add(new Label("Solved:"+String.valueOf(cnt)+"/"+String.valueOf(num)));
                    for(ExamBox box:boxes)
                    {
                        vb.getChildren().add(box.getBox());
                    }
                    mainlayout.getChildren().clear();
                    mainlayout.getChildren().add(scrollPane);
                }
            }
        });
    }

    private void setGoButtonAction()
    {
        goButton.setOnAction(e->{
            Thread generatingThread = new Thread(() -> {
                Platform.runLater(()->{
                    mainlayout.getChildren().clear();
                    mainlayout.getChildren().add(new Label("题目生成中..."));
                    mainlayout.getChildren().add(new ProgressBar(ProgressBar.INDETERMINATE_PROGRESS));
                });

                    List<Question> qls = AIExam.getQuestionAccordingEditingFile();
                Platform.runLater(()->{
                    boxes.clear();
                    for(Question q : qls)
                    {
                        boxes.add(new ExamBox(q));
                    }
                    mainlayout.getChildren().clear();
                    VBox vb = new VBox();
                    vb.setSpacing(15);
                    ScrollPane scrollPane = new ScrollPane(vb);
                    for(ExamBox box:boxes)
                    {
                        vb.getChildren().add(box.getBox());
                    }
                    mainlayout.getChildren().add(scrollPane);
                });
            });
            generatingThread.start();
        });
    }

    private void set0Face()
    {
        boxes.clear();
        try {
            if(FileManager.getInstance().getEditingFileTab()==null)
            {
                mainlayout.getChildren().clear();
                mainlayout.getChildren().add(new Label("没有正在编辑的笔记\n没法获取出题来源哦"));
            }
            else
            {
                mainlayout.getChildren().clear();
                mainlayout.getChildren().add(new Label("点击开始做题吧"));
                mainlayout.getChildren().add(goButton);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ProjectExamer getInstance()
    {
        if(instance == null)instance =  new ProjectExamer();
        return instance;
    }

    @Override
    public BorderPane getBorderPane() {
        if(boxes == null || boxes.isEmpty())set0Face();
        return borderPane;
    }

}
