package org.threeQuarters;

import database.note.*;
import org.threeQuarters.options.Options;

import java.util.prefs.Preferences;

public class AppConfig {


    // 初始化资源（如加载配置文件）
    public static void loadConfig() {
        // 模拟加载配置文件
//        System.out.println("加载配置文件: " + CONFIG_PATH);
        // 此处可以添加读取配置文件的代码
        Options.setUserName("Log In");

    }


}
