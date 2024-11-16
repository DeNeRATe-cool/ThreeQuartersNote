package org.threeQuarters.controls;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import org.threeQuarters.util.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileTreeCell extends TreeCell<File>{


    // 总结
    //事件触发：该事件处理器会在鼠标释放时触发。
    //展开/收缩控制：如果当前树节点不是叶子节点，并且点击位置不在展开/收缩按钮区域内，则切换该节点的展开或收缩状态。
    //文件树视图处理：如果当前的 TreeView 是 FileTreeView，则调用 FileTreeView 中的 handleClicks 方法来处理点击事件。
    public FileTreeCell(){
        setOnMouseReleased(event->{
            TreeItem<File> treeItem = getTreeItem();
            if(treeItem != null &&
            !treeItem.isLeaf() &&
            (getDisclosureNode() == null || !getDisclosureNode().getBoundsInParent().contains(event.getX(),event.getY()))){
                treeItem.setExpanded(!treeItem.isExpanded());
            }

            if(getTreeView() instanceof FileTreeView)
                ((FileTreeView)getTreeView()).handleClicks(treeItem,event.getButton(),event.getClickCount());

        });
    }



    protected void updateItem(File file, boolean empty){
        super.updateItem(file, empty);


        FileTreeItem item = (FileTreeItem) getTreeItem();
        if(Utils.fileOpenAble(file) && Utils.isTextFile(file))
        {
//            System.out.println(file.getAbsolutePath());
            try {
                item.setFileContent(new String(Files.readAllBytes(Path.of(file.getAbsolutePath()))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String text = null;
        Node graphic = null;

        if(!empty && file != null)
        {
            text = file.getName();
            //设置图标：根据节点类型选择不同的图标（文件或者文件夹）。
            //设置图标大小：使用 1.2em 来调整图标大小。
            FontAwesomeIcon icon = getTreeItem().isLeaf()
                    ? fileIcon(text)
                    : FontAwesomeIcon.FOLDER_ALT;
            graphic = FontAwesomeIconFactory.get().createIcon(icon,"1.2em");

            //这段代码的目的是根据每个文件项的图标（文件或文件夹的类型）动态地为树形视图的项添加不同的样式类，使得不同类型的文件或文件夹可以有不同的显示样式：
            //
            //判断图标类型（文件夹、文本文件、图片等），并为其设置相应的样式类（"folder"、"file"、"markdown"、"image"）。
            //重置样式类，确保每个树项都有统一的基础样式（tree-cell），防止复用时遗留样式。
            //如果文件名是以 . 开头（即隐藏文件），则添加 hidden 样式类。
            //举个例子：
            //假设有一个文件 example.md，它是一个 Markdown 文件：
            //
            //icon 可能是 FILE_TEXT_ALT，所以 styleClass 被设置为 "markdown"。
            //树项的样式类集合会是 ["tree-cell", "markdown"]，这表示该文件项将显示为一个 Markdown 文件的样式。
            //如果文件名是 .gitignore（隐藏文件）：
            //
            //text.startsWith(".") 会返回 true，因此会为该文件项添加 "hidden" 样式类。
            //树项的样式类集合会是 ["tree-cell", "hidden"]，这表示该文件项会被显示为隐藏文件。

            String styleClass;
            switch (icon) {
                case FOLDER_ALT: styleClass = "folder"; break;
                case FILE_ALT: styleClass = "file"; break;
                case FILE_TEXT_ALT: styleClass = "markdown"; break;
                case FILE_IMAGE_ALT: styleClass = "image"; break;
                default: styleClass = null; break;
            }
            getStyleClass().setAll("tree-cell"); // reset because cell may be reused for other files
            if (styleClass != null)
                getStyleClass().add(styleClass);
            if (text.startsWith("."))
                getStyleClass().add("hidden");

        }
        setText(text);
        setGraphic(graphic);
    }

    private FontAwesomeIcon fileIcon(String name)
    {
        // 如果文件名标识为图片类型
        return Utils.isImage(name)
            ? FontAwesomeIcon.FILE_IMAGE_ALT
                :(isMarkdownFile(name))
                ?FontAwesomeIcon.FILE_TEXT_ALT
                : FontAwesomeIcon.FILE_ALT;
    }

    private static String lastMarkdownFileExtensions;
    private static String[] extensions;

    private static boolean isMarkdownFile(String name)
    {
//        String markdownFileExtensions = Options.getMarkdownFileExtensions();
//        if(markdownFileExtensions != lastMarkdownFileExtensions)
//        {
//            lastMarkdownFileExtensions = markdownFileExtensions;
//            extensions = markdownFileExtensions.trim().split("\\s*,\\s*");
//            for (int i = 0; i < extensions.length; i++)
//                extensions[i] = extensions[i].startsWith("*.") ? extensions[i].substring(2) : null;
//        }
//
//        int sepIndex = name.lastIndexOf('.');
//        if(sepIndex < 0)
//            return false;
//
//        String ext = name.substring(sepIndex+1).toLowerCase();
//        for(String e:extensions){
//            if(e != null && e.equals(ext))
//                return true;
//        }
        return false;
    }

}
