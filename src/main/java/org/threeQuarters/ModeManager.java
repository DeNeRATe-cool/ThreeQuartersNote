package org.threeQuarters;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import org.threeQuarters.options.Options;
import org.threeQuarters.util.Utils;

import java.io.IOException;

public class ModeManager {

    HBox hBox;

    ToggleButton openWebView;

    private static ModeManager instance = new ModeManager();

    private ModeManager() {
        hBox = new HBox();

        openWebView = new ToggleButton();
        openWebView.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.EYE));
        Utils.applyTooltip(openWebView,"Open WebView");

        hBox.getChildren().add(openWebView);
        setAction();
    }

    public HBox getHBox()
    {
        return hBox;
    }

    public static ModeManager getInstance() {
        return instance;
    }

    public void setOpenWebViewBoolean(Boolean openWebViewBoolean)
    {
        openWebView.setSelected(openWebViewBoolean);
    }

    public void setAction()
    {
        openWebView.selectedProperty().addListener((observable, oldValue, newValue)->{
            if(newValue){
                try {
                    // 判断是否正在编辑 markdown 文本
                    if(FileManager.getInstance().editingMarkDownFile())
                    {
                        Options.setIsWebViewOpened(true);
                    }
                    else openWebView.setSelected(false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                if(Options.getIsWebViewOpened())
                {
                    Options.setIsWebViewOpened(false);
                }
                // 取消显示 webview
            }
        });
    }

}
