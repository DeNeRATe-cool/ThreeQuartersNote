package org.threeQuarters.FileMaster;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.threeQuarters.util.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class FileTreeItem
        extends TreeItem<File> {
    private static final Comparator<File> FILE_COMPARATOR = (f1, f2) -> {
        int result = Boolean.compare(f1.isDirectory(), f2.isDirectory());
        if (result != 0)
            return -result;
        return f1.getName().compareToIgnoreCase(f2.getName());
    };
    private static final Comparator<TreeItem<File>> ITEM_COMPARATOR = (i1, i2) -> FILE_COMPARATOR.compare(i1.getValue(), i2.getValue());

    private final FilenameFilter filter;

    private boolean leaf;
    private boolean leafInitialized;
    private boolean childrenInitialized;
    private boolean expandedListenerAdded;

    private FileData fileData;

    private String content;

    public void setFileContent(String content) {
        this.fileData.setContent(content);
    }

    public String getFileContent() {
        return fileData.getContent();
    }

    // 第一个构造方法：接收一个 File 对象，调用第二个构造方法并传入 null 作为过滤器。
    //第二个构造方法：接收一个 File 对象和一个 FilenameFilter，用于创建一个 FileTreeItem，并初始化 filter。

    public FileTreeItem(File file) throws IOException {
        this(file, new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                // 只接受扩展名为 .md 的文件
                return name.endsWith(".md") || new File(dir, name).isDirectory();
            }
        });
        fileData = new FileData(file);
    }

    public FileTreeItem(File file, FilenameFilter filter) throws IOException {
        super(file);
        this.filter = filter;
        fileData = new FileData(file);
    }


    //isLeaf()：判断当前节点是否是叶子节点（文件）。如果是文件返回 true，否则返回 false。
    //expandedProperty().addListener(...)：如果是文件夹（即非叶子节点），给它添加一个展开监听器。每当文件夹被展开时，调用 refresh() 方法来更新文件夹的内容（加载子文件夹和文件）。

    @Override
    public boolean isLeaf() {
        if (!leafInitialized) {
            leafInitialized = true;
            leaf = getValue().isFile();

            // add expanded listener only to non-leafs (to safe memory)
            if (!leaf && !expandedListenerAdded) {
                expandedListenerAdded = true;
                expandedProperty().addListener((observable, oldExpanded, newExpanded) -> {
                    if (newExpanded) {
                        try {
                            refresh();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
        return leaf;
    }

    //getChildren()：返回当前节点的所有子节点（即该文件夹中的所有文件和文件夹）。
    //首先检查 childrenInitialized，如果未初始化，读取当前文件夹的内容（通过 listFiles(filter) 方法）。然后排序文件列表，并创建对应的 FileTreeItem。
    //调用 super.getChildren().setAll(children) 更新父类 TreeItem 的子节点。

    @Override
    public ObservableList<TreeItem<File>> getChildren() {
        if (!childrenInitialized) {
            childrenInitialized = true;

            File f = getValue();
            if (f.isDirectory()) {
                File[] files = f.listFiles(filter);
                if (files != null) {
                    Arrays.sort(files, FILE_COMPARATOR);
                    ArrayList<TreeItem<File>> children = new ArrayList<>();
                    for (File file : files) {
//                        if(!Utils.isMarkdownFile(file))continue;
                        try {
                            children.add(new FileTreeItem(file, filter));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    super.getChildren().setAll(children);
                }
            }
        }
        return super.getChildren();
    }
    // getLoadedChildren()：返回已加载的子节点（即已经加载到树中的子文件夹和文件）。它只是简单地返回父类的 getChildren()。
    public ObservableList<TreeItem<File>> getLoadedChildren() {
        return super.getChildren();
    }

    //refresh()：用于更新当前节点（文件夹）中的子节点。当文件夹的内容发生变化时（文件被添加或删除），这个方法会检查并更新子节点。
    //如果文件夹的内容变化，refresh() 会：
    //清除当前的子节点。
    //重新加载文件夹中的文件。
    //比较哪些文件被添加，哪些文件被删除，然后更新子节点。

    public void refresh() throws IOException {
        if (leafInitialized) {
            // check whether file has changed from directory to normal file or vice versa
            boolean oldLeaf = leaf;
            leafInitialized = false;
            boolean newLeaf = isLeaf();
            if (newLeaf != oldLeaf) {
                childrenInitialized = false;
                super.getChildren().clear();
                return;
            }
        }

        if (!childrenInitialized || isLeaf())
            return;

        // get current files
        ObservableList<TreeItem<File>> children = super.getChildren();
        File f = getValue();
        File[] newFiles = f.isDirectory() ? f.listFiles(filter) : null;
        if (newFiles == null || newFiles.length == 0) {
            children.clear();
            return;
        }

        // determine added and removed files
        HashSet<File> addedFiles = new HashSet<>(Arrays.asList(newFiles));
        ArrayList<TreeItem<File>> removedFiles = new ArrayList<>();
        for (TreeItem<File> item : children) {
            if (!addedFiles.remove(item.getValue()))
                removedFiles.add(item);
        }

        // remove files
        if (!removedFiles.isEmpty())
            children.removeAll(removedFiles);

        // add files
        // Object Utils;
        for (File file : addedFiles)
            Utils.addSorted(children, new FileTreeItem(file, filter), ITEM_COMPARATOR);

        // refresh loaded children
        for (TreeItem<File> item : children) {
            if (item instanceof FileTreeItem)
                ((FileTreeItem)item).refresh();
        }
    }
}
