package org.threeQuarters.projects;

import database.note.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.threeQuarters.util.Utils;

import java.util.List;

import java.io.IOException;

public class ProjectShareNote {

    private static ProjectShareNote instance;

    ObservableList<Note> noteData;

//    ObservableList<String> data = FXCollections.observableArrayList(
//            "Apple", "Banana", "Cherry", "Date", "Elderberry",
//            "Fig", "Grape", "Honeydew", "Kiwi", "Lemon", "Mango","离散","离别是悄悄地"
//    );

    // 底板
    private BorderPane borderPane;
    // 搜索框
    private TextField searchField;
    // 预览表
    private ListView<String> resultListView;

    private ListView<HBox> allNotesListView;

    private VBox vBox;

    private HBox searchHBox;
    private Button searchButton;

    public static ProjectShareNote getInstance() throws IOException {
        if (instance == null) {
            instance = new ProjectShareNote();
        }
        return instance;
    }

    private ProjectShareNote() throws IOException {
        searchHBox = new HBox();
        searchButton = createSearchButton();

//        data = FXCollections.observableArrayList();
//        noteToString = FXCollections.observableArrayList();
        noteData = FXCollections.observableArrayList();

        loadShareNoteList();
        borderPane = new BorderPane();
        searchField = new TextField();
        searchField.setPromptText("Search..."); // 提示文本
        searchField.setPrefWidth(200);

        resultListView = new ListView<>();
        resultListView.setItems(getNoteNameList());
        resultListView.setVisible(false);

        allNotesListView = new ListView<>();
        allNotesListView.setItems(getAllNoteItem());
        allNotesListView.setVisible(true);

        setSearchFieldAction(searchField);
        setResultListViewAction(resultListView);

//        resultListView.setVisible(false);

        vBox = new VBox(searchField,resultListView);
        hideListView();

        searchHBox.getChildren().addAll(vBox, searchButton);

        borderPane.setTop(searchHBox);

    }

    private ObservableList<HBox> getAllNoteItem()
    {
        ObservableList<HBox> items = FXCollections.observableArrayList();
        for(Note note : noteData)
        {
//            items.add(noteItemFactory(note.simpleToString(),note.toString()));
            items.add(new ShareNoteItem(note).getHBox());
        }
        return items;
    }

    private HBox noteItemFactory(String noteName,String tooltips)
    {
        HBox hBox = new HBox();
        Label noteLabel = new Label(noteName);

        noteLabel.setId("noteLabel");

        // 下拉按钮
        Button button = getPullDownButton();

        hBox.getChildren().add(button);
        hBox.getChildren().add(noteLabel);
        Utils.applyTooltip(hBox,tooltips);
        return hBox;
    }

    private Button getPullDownButton()
    {
        Button button = new Button();
        Image img = new Image(getClass().getResource("/images/pull_downButton.png").toExternalForm());
        ImageView icon = new ImageView(img);
        icon.setFitHeight(14);
        icon.setFitWidth(14);
        button.setGraphic(icon);

        NoteAction noteManager = NoteAction.getInstance();

        return button;
    }

    ObservableList<String> getNoteNameList()
    {
        ObservableList<String> noteNames = FXCollections.observableArrayList();
        for(Note note : noteData)
        {
            noteNames.add(note.simpleToString());
        }
        return noteNames;
    }

    private Button createSearchButton() {
        Button searchButton = new Button();
        Image img = new Image(getClass().getResource("/images/searchButton.png").toExternalForm());
        ImageView icon = new ImageView(img);
        icon.setFitHeight(20);
        icon.setFitWidth(16);
        searchButton.setGraphic(icon);
        return searchButton;
    }

    public void loadShareNoteList(){
        NoteAction noteManager = NoteAction.getInstance();

        List<Note>allNote =  noteManager.queryAll();
        for(Note note : allNote)
        {
            noteData.add(note);
        }
//        for(Note note : allNote){
//            data.add(note.simpleToString());
//            noteToString.add(note.toString());
//        }
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    private void setResultListViewAction(ListView<String> listView) {
        // 响应 ListView 点击事件
//        resultListView.setOnMouseClicked(event -> {
//            // 获取选中项
//            String selectedItem = resultListView.getSelectionModel().getSelectedItem();
//            if (selectedItem != null) {
//                // 将选中项填充到搜索框
//                System.out.println(selectedItem);
//                searchField.setText(selectedItem);
//            }
//        });
        listView.setOnMouseClicked(event -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                searchField.setText(selectedItem); // 填充搜索框
            }
        });

        listView.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue&&!searchField.focusedProperty().getValue()) {
                hideListView();
            }
        });
    }

    public void hideListView()
    {
        resultListView.setVisible(false);
        vBox.getChildren().remove(resultListView);
        borderPane.setCenter(allNotesListView);
    }

    public void showListView()
    {
        resultListView.setVisible(true);
        vBox.getChildren().add(resultListView);
        borderPane.setCenter(null);
    }

    public boolean isClickOnSearchPane(Event event)
    {
        return event.getTarget() == searchField || event.getTarget() == resultListView;
    }

    private void setSearchFieldAction(TextField searchField) throws IOException {
        // 搜索逻辑：监听输入变化
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                resultListView.setItems(getNoteNameList()); // 显示所有数据

            } else {
                ObservableList<String> filteredData = FXCollections.observableArrayList();
                ObservableList<String> noteNames = getNoteNameList();
                for (String item : noteNames) {
                    if (item.toLowerCase().contains(newValue.toLowerCase())) {
                        filteredData.add(item); // 添加匹配的内容
                    }
                }
                resultListView.setItems(filteredData); // 更新结果列表
            }
        });

        searchField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue&&!resultListView.focusedProperty().getValue()) {
                hideListView();
            }
            else
            {
                showListView();
            }
        });

    }

}
