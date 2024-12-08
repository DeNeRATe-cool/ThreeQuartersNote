package org.threeQuarters.projects;

import com.mysql.cj.log.Log;
import database.user.User;
import database.user.UserAction;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import org.hibernate.annotations.Check;
import org.threeQuarters.ThreeQuartersApp;
import org.threeQuarters.options.Options;
import org.threeQuarters.toolkit.SimpleInputDialog;
import org.threeQuarters.util.ImageUtils;
import org.threeQuarters.util.MessageBox;
import org.threeQuarters.util.Utils;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LoginDialog {

    private static LoginDialog loginDialog;

    static {
        loginDialog = new LoginDialog();
    }

    public static LoginDialog getLoginDialog() {
        return loginDialog;
    }

    private ImageView avatar;

    public void show(Stage stage) throws IOException {
        Stage loginStage = new Stage();
        loginStage.initModality(Modality.APPLICATION_MODAL);

        // 创建主布局
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(15);

        // 设置对齐
        gridPane.setAlignment(Pos.CENTER);



        // 用户名部分
        Label IDLabel = new Label("Username:");
//        ComboBox<String> idBox = new ComboBox<>();
//        idBox.setEditable(true);
//        idBox.setPromptText("Enter your username");
        TextField IDField = getIDField();

        //
        avatar = getAvatarCircularWithUserName(IDField.getText());

        Button setAvatar = getSetAvatarButton(loginStage);

//        Button usernameButton = new Button("Check");

        HBox usernameBox = new HBox(10, IDField);
//        HBox usernameBox = new HBox(10,idBox);
//        HBox passwordBox = new HBox(10, idBox);
        usernameBox.setAlignment(Pos.CENTER_LEFT);

        // 密码部分
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = getPasswordField();
//        Button passwordButton = new Button("Show");

        HBox passwordBox = new HBox(10, passwordField);
        passwordBox.setAlignment(Pos.CENTER_LEFT);

        // sign up link
        Label signupLabel = getClickableLabel();

        // remember me checkbox
        CheckBox rememberCheckBox = getRememberMeCheckBox();

        // 登录按钮
//        Button loginButton = new Button("Login");
//        loginButton.setPrefWidth(150);
        Button loginButton = getLoginButton(IDField,passwordField,rememberCheckBox, loginStage);

        // 布局组件

        gridPane.add(avatar, 1, 0);

        gridPane.add(setAvatar, 0, 0);

        gridPane.add(IDLabel, 0, 1);
        gridPane.add(usernameBox, 1, 1);


        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordBox, 1, 2);

        gridPane.add(signupLabel, 0, 3);
        gridPane.add(rememberCheckBox, 1, 3);

        gridPane.add(loginButton, 1, 4);

        Scene scene = new Scene(gridPane);
        loginStage.setScene(scene);

        scene.getStylesheets().add(SimpleInputDialog.class.getResource("/loginFace.css").toExternalForm());

        // 显示对话框
        loginStage.show();

//        loginButton.setOnAction(e -> {
//            Options.setUserID(IDField.getText());
////            Options.setUserID(idBox.getValue());
//            Options.setPassWord(passwordField.getText());
//
//            System.out.println("close login dialog");
//            loginStage.close();
//        });

    }


    public static CheckBox getRememberMeCheckBox() {
        CheckBox rememberMeCheckBox = new CheckBox("remember me");
        Utils.applyTooltip(rememberMeCheckBox,"although i have to say goodbye~");
        if(UserAction.getInstance().checkIfRemember()!=null)rememberMeCheckBox.setSelected(true);
        return rememberMeCheckBox;
    }

    public static ImageView getAvatarCircularWithUserName(String userName)
    {
        ImageView imageView = ImageUtils.createAvatarImageView(UserAction.getInstance().getAvatar(userName));
        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
        Circle clip = new Circle(imageView.getFitWidth() / 2, imageView.getFitHeight() / 2, radius);
        imageView.setClip(clip);
        return imageView;
    }

    public static ImageView makeAvatarCircular() {
        String userName = null;
        if (UserAction.getNowUser() != null)userName = UserAction.getNowUser().getUsername();

        ImageView imageView = ImageUtils.createAvatarImageView(UserAction.getInstance().getAvatar(userName));
        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
        Circle clip = new Circle(imageView.getFitWidth() / 2, imageView.getFitHeight() / 2, radius);
        imageView.setClip(clip);
        return imageView;
    }

    public static Label getClickableLabel()
    {
        // 创建一个标签
        Label clickableLabel = new Label("Click to Sign in");

        // 设置标签的样式，模拟磁力标签的外观
        clickableLabel.setStyle("-fx-text-fill: #0066cc; -fx-font-size: 16px; -fx-underline: true;");

        // 设置点击事件处理器
        clickableLabel.setOnMouseClicked((MouseEvent event) -> {
            // 在这里处理点击事件，比如打开一个链接或执行其他操作
            try {
                SignUpDialog.show(ThreeQuartersApp.getPrimaryStage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            // 例如，模拟打开一个网站链接（这里仅输出信息）
            // 在实际应用中，你可以使用 Desktop.getDesktop().browse(new URI("http://example.com"));
        });
        return clickableLabel;
    }

    public static Button getLoginButton(TextField IDField, PasswordField passwordField,CheckBox rememberCheck, Stage loginStage) {
        // 登录按钮
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(150);
        loginButton.setOnAction(e -> {
//            Options.setUserID(IDField.getText());
////            Options.setUserID(idBox.getValue());
//            Options.setPassWord(passwordField.getText());



            if(IDField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                new MessageBox("","","Please enter your username and password");
            }
            else
            {
                boolean loginsuccess = UserAction.getInstance().login(IDField.getText(), passwordField.getText(), rememberCheck.isSelected());
                if(loginsuccess) {
                    new MessageBox("login info", "login info", "you have logined");

                    loginStage.close();
                    Options.setRememberUserName(UserAction.getNowUser().getUsername());
                    Options.setUserName(UserAction.getNowUser().getUsername());
                }
                else
                {
                    new MessageBox("nono","nono","you have not login");
                }
            }
        });
        return loginButton;
    }

    public TextField getIDField() {
        TextField textField = new TextField();

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            avatar = getAvatarCircularWithUserName(textField.getText());
        });

        if(UserAction.getInstance().getNowUser() != null)textField.setText(UserAction.getNowUser().getUsername());
        else
        {
            if (!Options.getRememberUserName().isEmpty()) {
                List<String> ls = UserAction.getInstance().checkIfRemember();
                if(ls != null && ls.contains(Options.getRememberUserName())) {
                    textField.setText(Options.getRememberUserName());
                }
                else textField.setPromptText("Enter your username");
            } else {
                textField.setPromptText("Enter your username");
            }
        }
        return textField;
    }

    public static PasswordField getPasswordField() {
        PasswordField passwordField = new PasswordField();
        if(!Options.getRememberUserName().isEmpty()) {
            List<String> ls = UserAction.getInstance().checkIfRemember();
            if(ls!=null && ls.contains(Options.getRememberUserName())) {
                passwordField.setText(ls.get(ls.indexOf(Options.getRememberUserName())+1));
            }
            else passwordField.setPromptText("Enter your password");
        }
        else passwordField.setPromptText("Enter your password");
        if(UserAction.getInstance().getNowUser() != null)passwordField.setText(UserAction.getNowUser().getPassword());
        return passwordField;
    }


    public Button getSetAvatarButton(Stage loginStage) {

        Button setAvatarButton = new Button("Set Avatar");

        setAvatarButton.setOnAction(e -> {

            if(UserAction.getInstance().getNowUser() == null)
            {
                new MessageBox("","","Please Log in first");
                return;
            }

            // 创建一个 FileChooser 对象

            FileChooser fileChooser = new FileChooser();

            // 设置文件扩展名过滤器，只显示图片文件
            FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
            fileChooser.getExtensionFilters().add(imageFilter);

            // 打开文件选择对话框，并获取选择的文件
            File file = fileChooser.showOpenDialog(ThreeQuartersApp.getPrimaryStage());

            UserAction action = UserAction.getInstance();

            if(file != null) {
                action.updateAvatar(UserAction.getNowUser().getUsername(), file.getAbsolutePath());
            }
            avatar = makeAvatarCircular();
            loginStage.close();
            try {
                LoginDialog.getLoginDialog().show(ThreeQuartersApp.getPrimaryStage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        return setAvatarButton;
    }

}
