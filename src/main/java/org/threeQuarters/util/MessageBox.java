package org.threeQuarters.util;

import javafx.scene.control.Alert;

public class MessageBox {

    public MessageBox(String title,String header,String content) {
            // 创建并配置消息框
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);

            // 使用自定义 CSS 样式
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/messagebox_style.css").toExternalForm());

            // 显示消息框
            alert.showAndWait();
    }

}
