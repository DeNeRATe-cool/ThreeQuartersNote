package org.threeQuarters.FileMaster;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import database.note.Note;
import database.note.NoteAction;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.robot.Robot;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.threeQuarters.projects.ProjectLeftMenu;
import org.threeQuarters.projects.ProjectShareNote;
import org.threeQuarters.util.MessageBox;
import org.threeQuarters.util.NoteManagerUtil;
import org.threeQuarters.util.TextAreaEnhancer;
import org.threeQuarters.util.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FileEditorTab extends Tab{

    private ILocalFile fileData;

    // 文件是否已经保存
    BooleanProperty saveProperty = new SimpleBooleanProperty(true);

    private TextArea textArea;
    private String prefsContent;
    private String textcontent;


    private WebView webView;
    private Parser parser;
    private HtmlRenderer renderer;
    Robot robot;
    boolean robotOpened = false;


    public FileEditorTab(ILocalFile fileData)
    {
        super(fileData.getName());
        robot = new Robot();
        setClosable(true);
        this.fileData = fileData;
        this.textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setEditable(true);

        if(NoteManagerUtil.validateStringStart(fileData.getContent()))
        {
            prefsContent = NoteManagerUtil.getUUIDFromContent(fileData.getContent());
            textcontent = NoteManagerUtil.removeUUIDFromContent(fileData.getContent());

        }else{
            prefsContent = "";
            textcontent = fileData.getContent();
        }


        textArea.setText(textcontent);


        // markdown 渲染器
        webView = new WebView();
        webView.setContextMenuEnabled(false);


        parser = Parser.builder().build();
        renderer = HtmlRenderer.builder().build();

        TextAreaEnhancer.enhanceTextArea(textArea);

//        textArea.getStyleClass().add("text-area");
        setContent(textArea);
        setTextAreaChangedAction();
        updateMarkDownShow();
        updateState();

        // 监听 TextArea 的滚动事件
        textArea.setOnScroll(event -> syncScroll(textArea, webView));

        // 监听 WebView 的滚动事件
        webView.setOnScroll(event -> syncScroll(webView, textArea));

    }

    public String getTextContent()
    {
        return textcontent;
    }

    public String getPrefsContent()
    {
        return prefsContent;
    }
    public void setPrefsContent(String prefsContent)
    {
        this.prefsContent = prefsContent;
    }

    public ILocalFile getFileData() {
        return fileData;
    }

    public WebView getWebView() {
        return webView;
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
//            double scrollPosition = (double) sourceWebView.getEngine().executeScript("return document.documentElement.scrollTop;");
//            double scrollHeight = targetTextArea.getHeight();
//            targetTextArea.setScrollTop(scrollPosition / 100 * scrollHeight);
        }
    }



    private void setTextAreaChangedAction()
    {
        // 添加 Tab 键监听
        this.textArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                int caretPosition = textArea.getCaretPosition();
                textArea.insertText(caretPosition, "    "); // 插入 4 个空格
                event.consume(); // 阻止默认 Tab 行为
            }

            // 及时 markdown 渲染
            translatorForTextAreaToWebView(textArea.getText(),textArea.getCaretPosition());
            updateMarkDownShow();
        });

        this.textArea.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                saveProperty.set(false);
                textcontent = textArea.getText();
                updateState();
                updateMarkDownShow();
                // 获取 WebView 的 WebEngine

            }
        });

        webView.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                webView.getStyleClass().add("focused-webview");
                textArea.requestFocus();
            } else {
                webView.getStyleClass().remove("focused-webview");
            }
        });

    }


    public static String translatorForTextAreaToWebView(String text,int pos)
    {
        if(text == null || text.equals(""))return "";
        text = Utils.getNearLines(text,20,pos);
        return text.replaceAll("\n","\n\n");
    }


    public void updateMarkDownShow()
    {
        if(!Utils.isMarkdownFile(new File(fileData.getAbsolutePath())))return;
        com.vladsch.flexmark.util.ast.Document document = parser.parse(translatorForTextAreaToWebView(textArea.getText(), textArea.getCaretPosition()));
        WebEngine webEngine = webView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                // 执行 JavaScript，滚动页面到底部
//                webEngine.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                // 获取 TextArea 的滚动位置并同步到 WebView

                String script = String.format(
                        "var scrollHeight = document.documentElement.scrollHeight;" +
                                "var windowHeight = window.innerHeight;" +
                                "var scrollToY = (scrollHeight - windowHeight) * %f ;" +
                                "window.scrollTo(0, scrollToY);", Utils.getLineRatio(textArea.getText(),textArea.getCaretPosition()));

//                webEngine.executeScript("window.scrollTo(0, " + (scrollPosition / scrollHeight *ThreeQuartersApp.getPrimaryStage().getHeight()*2) + ");");
                webEngine.executeScript(script);
            }

        });
        String htmlContent = renderer.render(document);
        webView.getEngine().loadContent(htmlContent);
    }

    public void showTabLook()
    {
//        setText(fileData.getName());
        if(saveProperty.get())
        {
            setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.STAR));
        }
        else
        {
            setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.STAR_ALT));
        }
    }

    public void saveFile() throws IOException {
        File file = new File(fileData.getAbsolutePath());

        if(file != null)
        {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bytes = (this.prefsContent + textArea.getText()).getBytes();
            fileOutputStream.write(bytes);
//            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//            writer.write(textArea.getText());
            fileOutputStream.close();
            saveProperty.set(true);
            updateState();
        }
    }

    public void pushOnDataBase()
    {
        System.out.println(this.fileData.getContent());
        if(!Objects.equals(this.prefsContent, ""))
        {
            new MessageBox("","","try modify the existed note");
            String nowUuid = NoteManagerUtil.extractUuid(this.prefsContent);
            List<Note> allNotes = NoteAction.getInstance().queryAll();
            Note operatingNote = null;
            for(Note note : allNotes)
            {
                if(note.getUuid().equals(nowUuid))
                {
                    operatingNote = note;
                }
            }
            if(operatingNote == null)
            {
                new MessageBox("","","cannot find the note");
                return;
            }
//            new ModifyShareNoteDialog(operatingNote, (FileData) this.fileData).show();

            operatingNote.setContent(prefsContent + textcontent);
            operatingNote.setUploadTime(new Date());
            System.out.println("motify\n"+operatingNote.getContent());
            NoteAction.getInstance().update(operatingNote);
        }
        else {
            new CreateShareNoteDialog((FileData) this.fileData).show();
        }
//        else
//        {
//
//        }
        if(ProjectLeftMenu.getInstance().isShareResourcesButtonSelected())
        {
            try {
                ProjectLeftMenu.getInstance().getLeftMenuPane().setCenter(ProjectShareNote.newInstance().getBorderPane());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 更新编辑器状态
    public void updateState()
    {
        showTabLook();
    }

}
