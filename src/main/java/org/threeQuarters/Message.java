package org.threeQuarters;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Message {
    private static final String BUNDLE_NAME = "org.org.threeQuarters.Messages";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private Message(){
    }

    public static String get(String key){
        return RESOURCE_BUNDLE.getString(key);
    }
    public static String get(String key,Object... args){
        return MessageFormat.format(get(key), args);
    }
}
