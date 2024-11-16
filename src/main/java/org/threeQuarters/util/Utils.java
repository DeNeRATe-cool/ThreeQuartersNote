package org.threeQuarters.util;


import java.io.File;
import java.util.*;
import java.util.prefs.Preferences;

public class Utils {


    // File Related

    private static final int MAX_FILE_SIZE = 500000;

    public static boolean fileOpenAble(File file)
    {
        return file != null && file.isFile() && file.length() < MAX_FILE_SIZE && file.canRead();
    }

    // 定义可在记事本中正常显示的文件扩展名集合
    private static final Set<String> TEXT_FILE_EXTENSIONS = new HashSet<>(Arrays.asList(
            "txt", "md", "c", "cpp", "java", "py", "html", "xml", "json", "css", "js", "csv", "log"
    ));

    // 判断文件是否为纯文本文件
    public static boolean isTextFile(String fileName) {
        // 获取文件扩展名
        String extension = getFileExtension(fileName);
        // 判断扩展名是否在集合中
        return TEXT_FILE_EXTENSIONS.contains(extension.toLowerCase());
    }

    // 判断 File 对象是否为纯文本文件
    public static boolean isTextFile(File file) {
        // 检查文件是否存在并且是一个文件
        if (file == null || !file.isFile()) {
            return false;
        }

        // 获取文件名的扩展名
        String extension = getFileExtension(file);
        // 判断扩展名是否在集合中
        return TEXT_FILE_EXTENSIONS.contains(extension.toLowerCase());
    }

    // 获取文件扩展名
    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    // 获取文件扩展名
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public static boolean safeEquals(Object o1, Object o2) {
        if(o1 == o2)return true;
        if(o1 == null || o2 == null)return false;
        return o1.equals(o2);
    }

    public static <T> void addSorted(List<T> list, T element, Comparator<T> c) {
        int index = Collections.binarySearch(list, element, c);
        list.add((index < 0) ? ((-index)-1) : index, element);
    }

    // 根据文件名判断是不是一个图片文件
    public static boolean isImage(String filename)
    {
        int sepIndex = filename.lastIndexOf('.');
        if(sepIndex < 0 || (filename.length() - sepIndex - 1) != 3)
        {
            return false;
        }

        String ext = filename.substring(sepIndex + 1).toLowerCase();
        return ext.equals("png") || ext.equals("gif") || ext.equals("jpg") || ext.equals("svg");
    }

    // 该方法用于将一个字符串值 value 存储到 Preferences 中，键为 key。
    //如果 value 和默认值 def 不相等，且 value 不为 null，则将 value 存储到 Preferences 中；否则，删除 Preferences 中与该 key 相关的值。

    public static void putPrefs(Preferences prefs, String key, String value, String def) {
        if (value != def && value != null && !value.equals(def))
            prefs.put(key, value);
        else
            prefs.remove(key);
    }

    //该方法用于将一个整数 value 存储到 Preferences 中，键为 key。
    //如果 value 不等于默认值 def，则将 value 存储到 Preferences 中；否则，删除与该 key 相关的值。

    public static void putPrefsInt(Preferences prefs, String key, int value, int def) {
        if (value != def)
            prefs.putInt(key, value);
        else
            prefs.remove(key);
    }

    // 该方法用于将一个双精度浮点数 value 存储到 Preferences 中，键为 key。
    //如果 value 不等于默认值 def，则将 value 存储到 Preferences 中；否则，删除与该 key 相关的值。

    public static void putPrefsDouble(Preferences prefs, String key, double value, double def) {
        if (value != def)
            prefs.putDouble(key, value);
        else
            prefs.remove(key);
    }

    //该方法用于将一个布尔值 value 存储到 Preferences 中，键为 key。
    //如果 value 不等于默认值 def，则将 value 存储到 Preferences 中；否则，删除与该 key 相关的值。

    public static void putPrefsBoolean(Preferences prefs, String key, boolean value, boolean def) {
        if (value != def)
            prefs.putBoolean(key, value);
        else
            prefs.remove(key);
    }

    //这两个方法主要用于将一个字符串数组存储到 Preferences 中，并在需要时重新读取它。它们通过给每个字符串项添加一个后缀（(i + 1)）的方式，确保每个字符串都有唯一的键。这样做有几个好处：
    //
    //可以动态地管理和更新存储的字符串数组。
    //可以在数组发生变化时（如长度改变时）清除旧的数据。
    //这种方法适合用于存储和管理一些简单的、可以按顺序读取的数据，如用户偏好设置、历史记录、配置项等。
    public static void putPrefsStrings(Preferences prefs, String key, String[] strings) {
        for (int i = 0; i < strings.length; i++)
            prefs.put(key + (i + 1), strings[i]);

        for (int i = strings.length; prefs.get(key + (i + 1), null) != null; i++)
            prefs.remove(key + (i + 1));
    }

    public static String[] getPrefsStrings(Preferences prefs, String key) {
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            String s = prefs.get(key + (i + 1), null);
            if (s == null)
                break;
            arr.add(s);
        }
        return arr.toArray(new String[arr.size()]);
    }


    public static <T extends Enum<T>> void putPrefsEnum(Preferences prefs, String key, T value, T def) {
        if (value != def)
            prefs.put(key, value.name());
        else
            prefs.remove(key);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T getPrefsEnum(Preferences prefs, String key, T def) {
        String s = prefs.get(key, null);
        if (s == null)
            return def;
        try {
            return (T) Enum.valueOf(def.getClass(), s);
        } catch (IllegalArgumentException ex) {
            return def;
        }
    }

}
