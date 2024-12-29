package org.threeQuarters.util;

import javafx.beans.property.SimpleBooleanProperty;

import java.util.prefs.Preferences;

//什么是 SimpleBooleanProperty？
//SimpleBooleanProperty 是 JavaFX 中的一个工具类，用来存储和管理一个布尔值（true 或 false）。而且，它不仅仅是存储数据这么简单，它还支持以下功能：
//
//自动更新（绑定）：你可以将它和其他属性连接起来，当其他属性发生变化时，SimpleBooleanProperty 也会自动更新。
//事件监听：你可以让程序监听它的变化，当它的值改变时，程序就会自动做出反应。
//简单的比喻：
//想象你有一个开关，你可以控制它是开（true）还是关（false）。这个开关代表了一个布尔值，SimpleBooleanProperty 就是用来存储这个开关的状态的。
//
//如果开关是开，SimpleBooleanProperty 存储的是 true。
//如果开关是关，SimpleBooleanProperty 存储的是 false。
//当你改变开关的状态时，SimpleBooleanProperty 会自动知道这个变化，并可以通知程序其他部分。
//
//代码中的作用：
//假设你有一个按钮或复选框，你需要记录这个按钮的状态（比如“是否被选中”）。你就可以使用 SimpleBooleanProperty 来保存这个状态。当按钮的状态发生变化时，SimpleBooleanProperty 会自动记录新的状态。

public class PrefsBooleanProperty
extends SimpleBooleanProperty {
    private Preferences prefs;
    private String key;
    private boolean def;

    public PrefsBooleanProperty() {
    }

    public PrefsBooleanProperty(boolean initialValue) {
        super(initialValue);
    }

    public PrefsBooleanProperty(Preferences prefs, String key, boolean def) {
        init(prefs, key, def);
    }

    public void init(Preferences prefs, String key, boolean def) {
        this.key = key;
        this.def = def;

        setPreferences(prefs);
        addListener((ob, o, n) -> {
            Utils.putPrefsBoolean(this.prefs, this.key, get(), this.def);
        });
    }

    public void setPreferences(Preferences prefs) {
        this.prefs = prefs;

        set(prefs.getBoolean(key, def));
    }

}
