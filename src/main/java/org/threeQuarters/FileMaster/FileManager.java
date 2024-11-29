package org.threeQuarters.FileMaster;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import org.threeQuarters.MainWindow;
import org.threeQuarters.ModeManager;
import org.threeQuarters.ThreeQuartersApp;
import org.threeQuarters.options.Options;
import org.threeQuarters.projects.ProjectFileTreeView;
import org.threeQuarters.projects.ProjectsFilesButtons;
import org.threeQuarters.toolkit.SimpleInputDialog;
import org.threeQuarters.util.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    // 文件标签栏
    private TabPane openedFilesTabPane;

    private Map<String, FileEditorTab> openTabs;

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

    public Button getOpenFolderButton() {
        return openFolderButton;
    }

    public void initialized() throws IOException {
        // 标签栏
        openedFilesTabPane = new TabPane();

        // 已经打开的文件
        openTabs = new HashMap<>();
        directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        // 打开文件夹按钮
        openFolderButton = new Button();
        Utils.applyTooltip(openFolderButton, "Open Folder");
        openFolderButton.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.FOLDER_OPEN_ALT));
        //


//        projectFileTreeView = new ProjectFileTreeView();
        setOpenFolderButtonAction();
        projectFileTreeView = new ProjectFileTreeView();

        fileLeftPane = new BorderPane();
        fileLeftPane.setTop(new ProjectsFilesButtons().getButtons());
        fileLeftPane.setCenter(projectFileTreeView.getFileTreeView());
//        fileLeftPane.setTop(openFolderButton);

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
            ModeManager.getInstance().setOpenWebViewBoolean(true);
        });
    }

    private void setOpenFolderButtonAction() throws IOException {
        openFolderButton.setOnAction(event -> {
//            File selectedFile = directoryChooser.showDialog(fileLeftPane.getScene().getWindow());
            File selectedFile = null;
            try {
                selectedFile = directoryChooser.showDialog(MainWindow.getMainWindow().getScene().getWindow());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
    // 创建一个新的文件树
    private void updateProjectFileTreeView() throws IOException {
        projectFileTreeView = new ProjectFileTreeView();
        fileLeftPane.setCenter(projectFileTreeView.getFileTreeView());
    }

    public BorderPane getFileLeftPane()
    {
        return fileLeftPane;
    }

    // 打开一个可编辑的文件
    public void OpenFileInTab(ILocalFile fileData) throws IOException
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
        Options.setIsWebViewOpened(false);
        Options.setIsWebViewOpened(true);

    }

    // 删除指定文件对应的 Tab
    public void closeFileTab(ILocalFile fileData) throws IOException {
        String filePath = fileData.getAbsolutePath();

        // 检查是否已经打开该文件
        if (openTabs.containsKey(filePath)) {
            FileEditorTab fileEditorTab = openTabs.get(filePath);

            // 从 TabPane 中移除该 Tab
            openedFilesTabPane.getTabs().remove(fileEditorTab);

            // 更新 openTabs，移除对应的 Tab
            openTabs.remove(filePath);

            // 这里你可以选择进一步处理，比如关闭文件等
//            fileEditorTab.close();
        }
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

    public void refresh()
    {
        try {
            projectFileTreeView.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createNewFolder() throws IOException {
        SimpleInputDialog inputDialog = new SimpleInputDialog();
        inputDialog.show(ThreeQuartersApp.getPrimaryStage(),"new Folder", input -> {
            File directFolder = projectFileTreeView.getDirectFolder();
            if(directFolder != null)
            {
                Path filePath = Paths.get(directFolder.getAbsolutePath(),input);

                if (!Files.exists(directFolder.getAbsoluteFile().toPath())) {
                    try {
                        Files.createDirectories(directFolder.getAbsoluteFile().toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                // 创建文件
                if (!Files.exists(filePath)) {
                    try {
                        Files.createDirectory(filePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        projectFileTreeView.refresh();
    }

    public void createNewFile()
    {
        SimpleInputDialog inputDialog = new SimpleInputDialog();
        inputDialog.show(ThreeQuartersApp.getPrimaryStage(),"new File",input -> {
            File directFolder = projectFileTreeView.getDirectFolder();
            if(directFolder != null)
            {
                if(!Utils.isMarkdownFile(input))input += ".md";
                Path filePath = Paths.get(directFolder.getAbsolutePath(),input);

                // 检查文件夹是否存在，如果不存在则创建
                try {
                    // 如果文件夹不存在，创建文件夹
                    if (!Files.exists(directFolder.getAbsoluteFile().toPath())) {
                        Files.createDirectories(directFolder.getAbsoluteFile().toPath());
                    }

                    // 创建文件
                    if (!Files.exists(filePath)) {
                        Files.createFile(filePath);
                    } else {
                    }

                } catch (IOException e) {
                    System.err.println("文件或文件夹创建失败: " + e.getMessage());
                }
            }
            projectFileTreeView.refresh();
        });
    }

    public void delFile() throws IOException {
        if(projectFileTreeView.getFileTreeView().getSelectionModel().getSelectedItem() == null)return;
        File selectedFile = projectFileTreeView.getFileTreeView().getSelectionModel().getSelectedItem().getValue();
        if(selectedFile != null)
        {
            if(selectedFile.getAbsolutePath().equals(Options.getCurrentRootPath()))return;
            if(selectedFile.isDirectory())
            {
                openTabs.forEach((k,v)->{
                    if(Utils.isFileInDirectory(new File(v.getFileData().getAbsolutePath()),selectedFile))
                    {
                        try {
                            closeFileTab(v.getFileData());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
            else if(selectedFile.isFile())
            {
                if(openTabs.containsKey(selectedFile.getName()))
                {
                    System.out.println(selectedFile.getName());
                    try {
                        closeFileTab(openTabs.get(selectedFile.getName()).getFileData());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            Utils.deleteFile(selectedFile);
            projectFileTreeView.refresh();
        }
    }

}
