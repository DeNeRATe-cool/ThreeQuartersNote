package org.threeQuarters.projects;

import org.threeQuarters.FileEditorTab;
import org.threeQuarters.FileManager;
import org.threeQuarters.controls.FileData;
import org.threeQuarters.controls.FileTreeCell;
import org.threeQuarters.controls.FileTreeItem;
import org.threeQuarters.controls.FileTreeView;
import org.threeQuarters.options.Options;
import org.threeQuarters.util.Utils;

import java.io.File;
import java.io.IOException;

// 负责文件的展示和响应

public class ProjectFileTreeView {

        private File rootDirectory;
        private FileTreeItem rootItem;
        private FileTreeView fileTreeView;

    public ProjectFileTreeView() throws IOException {

        fileTreeView = new FileTreeView();
        fileOnClick();
        // 获取文件根目录
        rootDirectory = new File(Options.getCurrentRootPath());
        // 设置根目录
        updateRootPath(rootDirectory);
        // 响应点击事件
//        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                File selectedFile = newValue.getValue();
//                try {
//                    updateRootPath(selectedFile);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });

    }

    public FileTreeView getFileTreeView() {
        return fileTreeView;
    }

    public void updateRootPath(File rootDir) throws IOException {
        rootItem = new FileTreeItem(rootDir);
        fileTreeView.setRoot(rootItem);
        rootItem.refresh();
        fileTreeView.setCellFactory(treeView -> new FileTreeCell());
    }



    public void fileOnClick(){
        // 响应文件的点击事件
        fileTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                File selectedFile = newValue.getValue();
                if(Utils.fileOpenAble(selectedFile) && Utils.isTextFile(selectedFile)){
                    String fileName = selectedFile.getName();
                    try {
                        FileManager.getInstance().OpenFileInTab(new FileData(selectedFile));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
//                fileTreeView.getSelectionModel().clearSelection();
//                FileTreeItem fileTreeItem = (FileTreeItem)newValue;
//                // 获取文件内容
//                String fileContent = fileTreeItem.getFileContent(); // 获取 userData

//                textArea.setText(fileContent);
                // 更新 MainApp 中的 TextArea 或其他组件来显示文件内容
//                updateTextArea(fileName, fileContent);
            }
        });
    }

}
