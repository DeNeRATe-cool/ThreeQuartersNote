package org.threeQuarters;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import org.threeQuarters.controls.FileData;
import org.threeQuarters.controls.FileTreeView;
import org.threeQuarters.options.Options;
import org.threeQuarters.projects.ProjectFileTreeView;
import org.threeQuarters.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    // 文件标签栏
    private TabPane openedFilesTabPane;

    private Map<String,FileEditorTab> openTabs;

    private static FileManager instance;

    private BooleanProperty openWebView;

    static {
        try {
            instance = new FileManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ProjectFileTreeView projectFileTreeView;

    private FileTreeView fileTreeView;
    // 打开文件夹按钮
    private Button openFolderButton;

    // 文件左侧面板
    private BorderPane fileLeftPane;

    // 创建 DirectoryChooser 实例
    DirectoryChooser directoryChooser;

    private FileManager() throws IOException {
        initialized();
    }

    public static synchronized FileManager getInstance() throws IOException {
        if(instance == null){
            instance = new FileManager();
        }
        return instance;
    }


    public void initialized() throws IOException {
        // 标签栏
        openedFilesTabPane = new TabPane();

        // 已经打开的文件
        openTabs = new HashMap<>();
        directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        openFolderButton = new Button();
        Utils.applyTooltip(openFolderButton, "Open Folder");
        openFolderButton.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.FOLDER_OPEN_ALT));
//        projectFileTreeView = new ProjectFileTreeView();
        setOpenFolderButtonAction();
        projectFileTreeView = new ProjectFileTreeView();

        fileLeftPane = new BorderPane();
        fileLeftPane.setCenter(projectFileTreeView.getFileTreeView());
        fileLeftPane.setTop(openFolderButton);

        // 是否打开WebView面板
        openWebView = new SimpleBooleanProperty(false);

        // 响应 标签切换事件
        setTabChangedAction();
    }

    // 响应标签切换事件
    public void setTabChangedAction()
    {
        openedFilesTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ModeManager.getInstance().setOpenWebViewBoolean(false);
        });
    }

    private void setOpenFolderButtonAction() throws IOException {
        openFolderButton.setOnAction(event -> {
            File selectedFile = directoryChooser.showDialog(fileLeftPane.getScene().getWindow());

            if(selectedFile != null && selectedFile.isDirectory()){
                Options.setCurrentRootPath(selectedFile.getAbsolutePath());
                try {
                    updateProjectFileTreeView();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public TabPane getOpenedFilesTabPane() {
        return openedFilesTabPane;
    }

    // 更新文件树的显示
    private void updateProjectFileTreeView() throws IOException {
        projectFileTreeView = new ProjectFileTreeView();
        fileLeftPane.setCenter(projectFileTreeView.getFileTreeView());
    }

    public BorderPane getFileLeftPane()
    {
        return fileLeftPane;
    }

    // 打开一个可编辑的文件
    public void OpenFileInTab(FileData fileData) throws IOException
    {
        String filePath = fileData.getAbsolutePath();
        if(openTabs.containsKey(filePath))
        {
            openedFilesTabPane.getSelectionModel().select(openTabs.get(filePath));
            return;
        }

        FileEditorTab fileEditorTab = new FileEditorTab(fileData);
        fileEditorTab.setClosable(true);

        openedFilesTabPane.getTabs().add(fileEditorTab);
        openedFilesTabPane.getSelectionModel().select(fileEditorTab);

        openTabs.put(filePath,fileEditorTab);

        fileEditorTab.setOnClosed(e->openTabs.remove(filePath));

    }

    // 保存文件
    public void saveEditingFile()
    {
        Tab nowSelectedTab = openedFilesTabPane.getSelectionModel().getSelectedItem();
        if(nowSelectedTab instanceof FileEditorTab fileEditorTab)
        {
            try {
                fileEditorTab.saveFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 当前是否正在编辑 markdown 类型文件
    public boolean editingMarkDownFile()
    {
        Tab nowSelectedTab = openedFilesTabPane.getSelectionModel().getSelectedItem();
        if(nowSelectedTab instanceof FileEditorTab fileEditorTab)
        {
            return Utils.isMarkdownFile(new File(fileEditorTab.getFileData().getAbsolutePath()));
        }
        return false;
    }

    // 获取当前 markdown 文件的 webview 控件
    public WebView getMDWebView()
    {
        Tab nowSelectedTab = openedFilesTabPane.getSelectionModel().getSelectedItem();
        if(nowSelectedTab instanceof FileEditorTab fileEditorTab)
        {
            return fileEditorTab.getWebView();
        }
        return null;
    }

}
