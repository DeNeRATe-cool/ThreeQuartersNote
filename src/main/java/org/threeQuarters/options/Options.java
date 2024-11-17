package org.threeQuarters.options;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import org.threeQuarters.util.PrefsBooleanProperty;
import org.threeQuarters.util.PrefsStringProperty;

import javax.swing.text.StyledEditorKit;

public class Options {

//    这段代码定义了一个用于存储和管理 Markdown 文件扩展名的属性，提供了以下功能：
//
//获取扩展名：getMarkdownFileExtensions() 用于获取当前的 Markdown 文件扩展名。
//设置扩展名：setMarkdownFileExtensions() 用于设置新的 Markdown 文件扩展名。
//绑定属性：markdownFileExtensionsProperty() 返回一个 StringProperty 对象，允许在 JavaFX 中进行绑定或监听属性的变化。
//通过这些方法，程序可以灵活地管理与 Markdown 文件扩展名相关的设置，并可以将其与用户界面（如设置界面）或其他程序部分进行交互。
// 'markdownFileExtensions' property


    private static final PrefsBooleanProperty testbool = new PrefsBooleanProperty(false);
    private static boolean getTestbool(){return testbool.get();}
    public static void setTestbool(boolean value){testbool.set(value);}
    public static BooleanProperty getTestboolProperty(){return testbool;}


    private static final PrefsBooleanProperty isWebViewOpened = new PrefsBooleanProperty(false);
    public static boolean getIsWebViewOpened() {return isWebViewOpened.get();}
    public static void setIsWebViewOpened(boolean value) {
        Options.isWebViewOpened.set(value);
    }
    public static BooleanProperty getIsWebViewOpenedProperty() {return isWebViewOpened;}

    //在这段代码中，markdownFileExtensions 是用来保存 Markdown 文件扩展名的一个全局配置项。这个设置会在整个程序中共享，它并不为每一个 .md 文件单独设置一个扩展名。相反，它是用来管理你希望程序默认使用的扩展名。
    //
    //例如：
    //
    //如果你希望程序识别 .md 或 .markdown 扩展名作为 Markdown 文件的标准扩展名，markdownFileExtensions 会存储这个信息（例如：.md,.markdown）。
    //当你创建新的 Markdown 文件时，程序会根据这个全局设置来判断该文件的类型。

    private static final PrefsStringProperty markdownFileExtensions = new PrefsStringProperty();
    public static String getMarkdownFileExtensions() { return markdownFileExtensions.get(); }
    public static void setMarkdownFileExtensions(String markdownFileExtensions) { Options.markdownFileExtensions.set(markdownFileExtensions); }
    public static StringProperty markdownFileExtensionsProperty() { return markdownFileExtensions; }

    // 当前根目录
    private static final PrefsStringProperty currentRootPath = new PrefsStringProperty();
    public static String getCurrentRootPath() {return currentRootPath.get();}
    public static void setCurrentRootPath(String currentRootPath){Options.currentRootPath.set(currentRootPath);}
    public static StringProperty currentRootPathProperty() { return currentRootPath; }
}
