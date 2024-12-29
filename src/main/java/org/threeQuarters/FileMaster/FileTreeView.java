package org.threeQuarters.FileMaster;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileTreeView extends TreeView<File>{

    // need a hard reference to avoid GC
//    private final BooleanBinding windowFocusedProperty;

    public FileTreeView() {
        setCellFactory(treeView -> new FileTreeCell());

//        enableDragAndDrop();
//        windowFocusedProperty = Bindings.selectBoolean(sceneProperty(), "window", "focused");
        // use runLater() for adding listener to avoid unnecessary refresh after initial creation
//        Platform.runLater(() -> {
//            windowFocusedProperty.addListener((observer, oldFocused, newFocused) -> {
//                if (newFocused)
//                    Platform.runLater(() -> refreshFiles());
//            });
//        });

        // refresh tree if markdown file extensions were changed in options dialog
//        Options.markdownFileExtensionsProperty().addListener(new WeakInvalidationListener(e -> {
//            refresh();
//        }));
    }

//    private void enableDragAndDrop(){
//        // 拖动开始
//        this.setOnDragDetected(event -> {
//            TreeItem<File> selectedItem = this.getSelectionModel().getSelectedItem();
//            if (selectedItem != null) {
//                Dragboard dragboard = this.startDragAndDrop(TransferMode.ANY);
//                ClipboardContent content = new ClipboardContent();
//                content.putString(selectedItem.getValue().getAbsolutePath());
//                dragboard.setContent(content);
////                event.consume();
//            }
//        });
//
//        this.setOnDragOver(event->{
//            if(event.getGestureSource() instanceof FileTreeView && event.getDragboard().hasString()){
//                event.acceptTransferModes(TransferMode.MOVE);
//            }
//            event.consume();
//        });
//
//        // 拖动进入文件夹时，改变目标文件夹的视觉效果
//        setOnDragEntered(event -> {
////            if (!isLeaf()) {
//                // 改变视觉效果，例如：背景颜色变为绿色，表示可以放下
//                setStyle("-fx-background-color: lightgreen;");
////            }
//            event.consume();
//        });
//
//        // 拖动退出文件夹时，恢复目标文件夹的默认视觉效果
//        setOnDragExited(event -> {
////            if (!isLeaf()) {
//                // 恢复默认样式
//                setStyle("-fx-background-color: transparent;");
////            }
//            event.consume();
//        });
//
//        this.setOnDragDropped(event -> {
//            Dragboard dragboard = event.getDragboard();
//            boolean success = false;
//            if (dragboard.hasString()) {
//                String draggedFile = dragboard.getString();
//                Path source = Paths.get(draggedFile);
//
//                System.out.println(event.getSource().getClass());
//                TreeView<File> targetView = (TreeView<File>) event.getSource();
//                TreeItem<File> targetItem = targetView.getSelectionModel().getSelectedItem();
//                File targetDir = targetItem.getValue();
//                System.out.println("targetDir: " + targetDir.getAbsolutePath());
//                System.out.println("source: " + source);
//
//                if (targetDir.isDirectory()) {
//                    Path target = Paths.get(targetDir.getAbsolutePath(), source.getFileName().toString());
//                    try{
//                        Files.move(source,target, StandardCopyOption.REPLACE_EXISTING);
//                        success = true;
//                    }catch(IOException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//            event.setDropCompleted(success);
//            event.consume();
//        });
//    }

    protected void handleClicks(TreeItem<File> item, MouseButton button, int clickCount) {
    }

    public void refreshFiles() throws IOException {
        if (getRoot() instanceof FileTreeItem)
            ((FileTreeItem)getRoot()).refresh();
    }

    public List<File> getExpandedDirectories() {
        if (getRoot() == null)
            return Collections.emptyList();

        return items2files(findItems(item -> item.isExpanded()));
    }

    public void setExpandedDirectories(List<File> expandedDirectories) {
        if (getRoot() == null)
            return;

        HashSet<File> expandedDirectoriesSet = new HashSet<>(expandedDirectories);
        expandDirectories(getRoot(), expandedDirectoriesSet);
    }

    private void expandDirectories(TreeItem<File> item, HashSet<File> expandedDirectoriesSet) {
        getLoadedChildren(item).forEach(child -> {
            if (!child.isLeaf()) {
                if (expandedDirectoriesSet.contains(child.getValue()))
                    child.setExpanded(true);

                expandDirectories(child, expandedDirectoriesSet);
            }
        });
    }

    public List<TreeItem<File>> findItems(Predicate<TreeItem<File>> predicate) {
        if (getRoot() == null)
            return Collections.emptyList();

        ArrayList<TreeItem<File>> items = new ArrayList<>();
        findItemsRecur(predicate, getRoot(), items);
        return items;
    }

    private void findItemsRecur(Predicate<TreeItem<File>> predicate, TreeItem<File> item, List<TreeItem<File>> items) {
        if (predicate.test(item))
            items.add(item);

        getLoadedChildren(item).forEach(child -> {
            findItemsRecur(predicate, child, items);
        });
    }

    private List<File> items2files(List<TreeItem<File>> items) {
        return items.stream()
                .map(item -> item.getValue())
                .collect(Collectors.toList());
    }

    private ObservableList<TreeItem<File>> getLoadedChildren(TreeItem<File> item) {
        return (item instanceof FileTreeItem && !item.isExpanded())
                ? ((FileTreeItem)item).getLoadedChildren()
                : item.getChildren();
    }
}
