package org.threeQuarters;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.threeQuarters.controls.FileData;
import org.threeQuarters.util.Utils;

import java.io.*;

public class FileEditorTab extends Tab{

    private FileData fileData;

    // 文件是否已经保存
    BooleanProperty saveProperty = new SimpleBooleanProperty(true);

    private TextArea textArea;

    private WebView webView;
    private Parser parser;
    private HtmlRenderer renderer;

    public FileEditorTab(FileData fileData)
    {
        super(fileData.getName());
        setClosable(true);
        this.fileData = fileData;
        this.textArea = new TextArea();
        textArea.setEditable(true);
        textArea.setText(fileData.getContent());
        // 设置 WebView 为可编辑
        String editableContent = "<html><body>" +
                "<h2 style='text-align: center;'>这是一个可编辑的区域</h2>" +
                "<div contenteditable='true' style='width: 80%; margin: auto; padding: 20px; border: 2px solid #ccc; border-radius: 10px;'>" +
                "<p>您可以在这里编辑文本，输入内容，或者删除。</p>" +
                "<p>例如：输入一些内容或删除它。</p>" +
                "</div>" +
                "</body></html>";




        // markdown 渲染器
        webView = new WebView();
        webView.setContextMenuEnabled(false);

        // 设置 WebView 内容
        webView.getEngine().loadContent(editableContent);

        parser = Parser.builder().build();
        renderer = HtmlRenderer.builder().build();


//        textArea.getStyleClass().add("text-area");
        setContent(textArea);
        setTextAreaChangedAction();
        updateMarkDownShow();
        updateState();
    }

    public FileData getFileData() {
        return fileData;
    }

    public WebView getWebView() {
        return webView;
    }

    private void setTextAreaChangedAction()
    {
        this.textArea.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                saveProperty.set(false);
                updateState();
                updateMarkDownShow();
                // 获取 WebView 的 WebEngine
                WebEngine webEngine = webView.getEngine();

                // 执行 JavaScript，滚动到页面顶部
                webEngine.executeScript("window.scrollTo(0, 0);");
                System.out.println("window.scrollTo(0, 0);");
            }
        });

        webView.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("WebView is focused");
                webView.getStyleClass().add("focused-webview");
                textArea.requestFocus();
            } else {
                System.out.println("WebView is not focused");
                webView.getStyleClass().remove("focused-webview");
            }
        });




    }

    public void updateMarkDownShow()
    {
        if(!Utils.isMarkdownFile(new File(fileData.getAbsolutePath())))return;
        com.vladsch.flexmark.util.ast.Document document = parser.parse(textArea.getText());
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
            byte[] bytes = textArea.getText().getBytes();
            fileOutputStream.write(bytes);
//            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//            writer.write(textArea.getText());
            saveProperty.set(true);
            updateState();
        }
    }

    // 更新编辑器状态
    public void updateState()
    {
        showTabLook();
    }

}
