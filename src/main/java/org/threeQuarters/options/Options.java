package org.threeQuarters.options;

import database.user.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import org.asynchttpclient.util.StringBuilderPool;
import org.threeQuarters.util.PrefsBooleanProperty;
import org.threeQuarters.util.PrefsStringProperty;

import javax.swing.text.StyledEditorKit;
import java.util.prefs.Preferences;

public class Options {

//    这段代码定义了一个用于存储和管理 Markdown 文件扩展名的属性，提供了以下功能：
//
//获取扩展名：getMarkdownFileExtensions() 用于获取当前的 Markdown 文件扩展名。
//设置扩展名：setMarkdownFileExtensions() 用于设置新的 Markdown 文件扩展名。
//绑定属性：markdownFileExtensionsProperty() 返回一个 StringProperty 对象，允许在 JavaFX 中进行绑定或监听属性的变化。
//通过这些方法，程序可以灵活地管理与 Markdown 文件扩展名相关的设置，并可以将其与用户界面（如设置界面）或其他程序部分进行交互。
// 'markdownFileExtensions' property

    private static final PrefsStringProperty buaaID = new PrefsStringProperty();
    public static String getBuaaID(){return buaaID.get();}
    public static void setBuaaID(String value){buaaID.set(value);}
    public static PrefsStringProperty getBuaaIDProperty(){return buaaID;}

    public static final PrefsStringProperty buaaPassword = new PrefsStringProperty();
    public static String getBuaaPassword(){return buaaPassword.get();}
    public static void setBuaaPassword(String value){buaaPassword.set(value);}
    public static PrefsStringProperty getBuaaPasswordProperty(){return buaaPassword;}

    private static final PrefsStringProperty userID = new PrefsStringProperty();
    public static String getUserID(){return userID.get();}
    public static void setUserID(String ID){userID.set(ID);}
    public static PrefsStringProperty getuserIDProperty(){return userID;}

    public static final PrefsStringProperty userName = new PrefsStringProperty();
    public static String getUserName(){return userName.get();}
    public static void setUserName(String ID){userName.set(ID);}
    public static PrefsStringProperty getUserNameProperty(){return userName;}

    public static final PrefsStringProperty passWord = new PrefsStringProperty();
    public static String getPassWord(){return passWord.get();}
    public static void setPassWord(String ID){passWord.set(ID);}
    public static PrefsStringProperty getpassWordProperty(){return passWord;}


    private static final PrefsBooleanProperty testbool = new PrefsBooleanProperty(false);
    private static boolean getTestbool(){return testbool.get();}
    public static void setTestbool(boolean value){testbool.set(value);}
    public static BooleanProperty getTestboolProperty(){return testbool;}


    private static final PrefsBooleanProperty isWebViewOpened = new PrefsBooleanProperty(true);
    public static boolean getIsWebViewOpened() {return isWebViewOpened.get();}
    public static void setIsWebViewOpened(boolean value) {
        Options.isWebViewOpened.set(value);
    }
    public static PrefsBooleanProperty getIsWebViewOpenedProperty() {return isWebViewOpened;}

    //在这段代码中，markdownFileExtensions 是用来保存 Markdown 文件扩展名的一个全局配置项。这个设置会在整个程序中共享，它并不为每一个 .md 文件单独设置一个扩展名。相反，它是用来管理你希望程序默认使用的扩展名。
    //
    //例如：
    //
    //如果你希望程序识别 .md 或 .markdown 扩展名作为 Markdown 文件的标准扩展名，markdownFileExtensions 会存储这个信息（例如：.md,.markdown）。
    //当你创建新的 Markdown 文件时，程序会根据这个全局设置来判断该文件的类型。

    private static final PrefsStringProperty markdownFileExtensions = new PrefsStringProperty();
    public static String getMarkdownFileExtensions() { return markdownFileExtensions.get(); }
    public static void setMarkdownFileExtensions(String markdownFileExtensions) { Options.markdownFileExtensions.set(markdownFileExtensions); }
    public static PrefsStringProperty markdownFileExtensionsProperty() { return markdownFileExtensions; }

    // 当前根目录
    private static final PrefsStringProperty currentRootPath = new PrefsStringProperty();
    public static String getCurrentRootPath() {return currentRootPath.get();}
    public static void setCurrentRootPath(String currentRootPath){Options.currentRootPath.set(currentRootPath);}
    public static PrefsStringProperty currentRootPathProperty() { return currentRootPath; }

    private static final PrefsStringProperty rememberUserName = new PrefsStringProperty();
    public static String getRememberUserName() {return rememberUserName.get();}
    public static void setRememberUserName(String rememberUserName) {Options.rememberUserName.set(rememberUserName);}
    public static PrefsStringProperty rememberUserNameProperty() { return rememberUserName; }


    static{
        Preferences prefs = Preferences.userNodeForPackage(Options.class);
//        buaaID.init(prefs, "buaaID", "defaultID");
//        userName.init(prefs,"userName", "");
        rememberUserName.init(prefs,"rememberUserName","");
        currentRootPath.init(prefs,"currentRootPath", System.getProperty("user.dir"));
    }


}
