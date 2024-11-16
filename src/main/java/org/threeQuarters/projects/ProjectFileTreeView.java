package org.threeQuarters.projects;

import org.threeQuarters.controls.FileTreeCell;
import org.threeQuarters.controls.FileTreeItem;
import org.threeQuarters.controls.FileTreeView;
import org.threeQuarters.options.Options;

import java.io.File;
import java.io.IOException;

public class ProjectFileTreeView
 extends FileTreeView {

        private File rootDirectory;
        private FileTreeItem rootItem;


    public ProjectFileTreeView() throws IOException {
        // 获取文件根目录
        rootDirectory = new File(Options.getCurrentRootPath());
        // 设置根目录
        updateRootPath(rootDirectory);
        // 响应点击事件
        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                File selectedFile = newValue.getValue();
                try {
                    updateRootPath(selectedFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }


    public void updateRootPath(File rootDir) throws IOException {
        rootItem = new FileTreeItem(rootDir);
        setRoot(rootItem);
        rootItem.refresh();
        setCellFactory(treeView -> new FileTreeCell());

    }

}
