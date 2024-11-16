package org.threeQuarters.util;

import javafx.beans.property.SimpleObjectProperty;

import java.util.prefs.Preferences;

public class PrefsStringsProperty extends SimpleObjectProperty<String[]> {

    // Preferences 类是 Java 提供的一种用于持久化存储应用程序设置的工具，属于 java.util.prefs 包。它主要用于存储和读取用户或系统的配置项（如应用程序设置、用户偏好等），这些配置项可以在程序重新启动后保持不变。Preferences 类通过键值对的方式存储数据，支持数据的简单存取，并且在不同操作系统上能够提供一致的行为。

    private Preferences prefs;
    private String key;

    public PrefsStringsProperty(){
        set(new String[0]);
    }

    public PrefsStringsProperty(Preferences prefs,String key){init(prefs,key);}

    public void init(Preferences prefs,String key)
    {
        this.key = key;
        setPreferences(prefs);
        addListener((ob,o,n)-> {
            Utils.putPrefsStrings(this.prefs, this.key, get());
        });

    }

    public void setPreferences(Preferences prefs) {
        this.prefs = prefs;

        set(Utils.getPrefsStrings(prefs, key));
    }

//总结
//PrefsStringsProperty 类的作用是：
//
//管理一个字符串数组属性，并将其与 Preferences 结合，允许该属性值在程序运行时被存储到本地配置中。
//通过 init() 方法初始化该属性，使其能够从 Preferences 加载初始值，并且在属性值变化时自动将新的值保存回 Preferences。
//提供一个 JavaFX 的属性类型（SimpleObjectProperty<String[]>），支持属性绑定和监听，使得 UI 或其他代码能够方便地与这个属性进行交互。

}
