package org.threeQuarters.controls;

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
