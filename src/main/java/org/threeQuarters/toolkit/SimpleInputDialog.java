package org.threeQuarters.toolkit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SimpleInputDialog {

    public static void show(Stage owner, String placeholder, OnConfirmListener listener) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setResizable(false);

        // 输入框
        TextField textField = new TextField();
        Label label = new Label();
        label.setText(placeholder);
        label.setStyle("-fx-text-fill: black;"+
                "-fx-font-size: 20");
        textField.setPromptText(placeholder);
//        .getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

//        textField.setStyle(
//                "-fx-background-color: #f9f9f9; " +
//                        "-fx-border-color: #cccccc; " +
//                        "-fx-border-radius: 5; " +
//                        "-fx-padding: 8; " +
//                        "-fx-font-size: 14;"
//        );

        // 确定按钮
        Button confirmButton = new Button("确定");
        confirmButton.setId("button2");
//        confirmButton.setStyle(
//                "-fx-background-color: #0078D7; " +
//                        "-fx-text-fill: white; " +
//                        "-fx-padding: 8 16; " +
//                        "-fx-background-radius: 5;"
//        );
        confirmButton.setOnAction(e -> {
            listener.onConfirm(textField.getText());
            dialog.close();
        });

        // 取消按钮
        Button cancelButton = new Button("取消");
        cancelButton.setId("button2");
//        cancelButton.setStyle(
//                "-fx-background-color: #f3f3f3; " +
//                        "-fx-padding: 8 16; " +
//                        "-fx-border-color: #cccccc; " +
//                        "-fx-border-radius: 5;"
//        );
        cancelButton.setOnAction(e -> dialog.close());

        // 布局
        HBox buttonBox = new HBox(10, confirmButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox dialogLayout = new VBox(15, label,textField, buttonBox);
        dialogLayout.setAlignment(Pos.CENTER);
        dialogLayout.setPadding(new Insets(20));
//        dialogLayout.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-border-color: #cccccc;");

        Scene scene = new Scene(dialogLayout, 300, 150);
        dialog.setScene(scene);


        scene.getStylesheets().add(SimpleInputDialog.class.getResource("/styles.css").toExternalForm());

        // 显示对话框
        dialog.show();
    }

    public interface OnConfirmListener {
        void onConfirm(String input);
    }
}
