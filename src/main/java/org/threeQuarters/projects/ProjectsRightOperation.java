package org.threeQuarters.projects;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.threeQuarters.ModeManager;
import org.threeQuarters.util.Utils;

public class ProjectsRightOperation {

    private BorderPane rightPane;

    private VBox vbox;

    private static ProjectsRightOperation instance;

    private ToggleButton webViewButton;

    private Button shareButton;

    private ProjectsRightOperation() {
        webViewButton = ModeManager.getInstance().getOpenWebView();

        shareButton = creatShareButton();

        vbox = new VBox();
        putButtonsInVBox();

        rightPane = new BorderPane();

        rightPane.setCenter(vbox);



    }

    private void putButtonsInVBox()
    {
        vbox.getChildren().add(webViewButton);
        vbox.getChildren().add(shareButton);
    }

    private Button creatShareButton()
    {
        Button shareButton = new Button();
        Image shareImg = new Image(getClass().getResource("/images/shareButton.png").toExternalForm());
        ImageView shareIcon = new ImageView(shareImg);
        shareIcon.setFitHeight(20);
        shareIcon.setFitWidth(16);
        shareButton.setGraphic(shareIcon);
        Utils.applyTooltip(shareButton,"Share it On Community");
        setShareButtonAction();

        return shareButton;
    }

    public void setShareButtonAction()
    {

    }

    public BorderPane getRightPane() {
        return rightPane;
    }

    public static ProjectsRightOperation getInstance() {
        if (instance == null) {
            instance = new ProjectsRightOperation();
        }
        return instance;
    }

}
