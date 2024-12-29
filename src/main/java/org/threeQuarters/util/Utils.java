package org.threeQuarters.util;


import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {


    // File Related

    private static final int MAX_FILE_SIZE = 500000;

    public static boolean fileOpenAble(File file)
    {
        return file != null && file.isFile() && file.length() < MAX_FILE_SIZE && file.canRead();
    }

    // Markdown 文件的扩展名列表
    private static final List<String> MARKDOWN_EXTENSIONS = Arrays.asList(".md", ".markdown", ".mkd", ".mkdn");

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
        return ext.equals("png") || ext.equals("gif") || ext.equals("jpg") || ext.equals("images");
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

    public static boolean isMarkdownFile(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        }

        // 获取文件名并转换为小写字母
        String fileName = file.getName().toLowerCase();
        // 检查文件名的后缀是否在 Markdown 扩展名列表中
        return isMarkdownFile(fileName);
    }

    public static boolean isMarkdownFile(String fileName)
    {
        for (String ext : MARKDOWN_EXTENSIONS) {
            if (fileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    public static void applyTooltip(Node node, String tooltipText) {
        Tooltip tooltip = new Tooltip(tooltipText);

        // 设置 Tooltip 的样式
        tooltip.setStyle("-fx-background-color: #333333; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px;");

        // 设置显示和隐藏的延迟时间
        tooltip.setShowDelay(Duration.millis(200)); // 200 毫秒后显示
        tooltip.setHideDelay(Duration.millis(200)); // 鼠标移开 200 毫秒后隐藏

        if (node instanceof Control) {
            // 对 Control 类型的节点，直接调用 setTooltip
            ((Control) node).setTooltip(tooltip);
        } else {
            // 对非 Control 类型的节点，使用 Tooltip.install
            Tooltip.install(node, tooltip);
        }
    }

    // 删除文件

    // 删除文件或空文件夹
    public static boolean deleteFile(File file) {
        // 检查文件是否存在
        if (file.exists()) {
            // 如果是文件，则直接删除
            if (file.isFile()) {
                return file.delete();
            }
            // 如果是文件夹，先删除文件夹中的内容，然后删除文件夹
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        // 递归删除文件夹中的文件或子文件夹
                        deleteFile(f);
                    }
                }
                return file.delete();  // 删除空文件夹
            }
        }
        return false;  // 文件不存在或删除失败
    }

    // 判断文件是否在某个目录下
    public static boolean isFileInDirectory(File file, File directory) {
        // 检查文件和目录是否有效
        if (file == null || directory == null) {
            return false;
        }

        // 获取文件的父目录
        File parent = file.getParentFile();

        // 判断文件是否存在并且父目录是否是目标目录
        return parent != null && parent.equals(directory);
    }

    public static int countSubString(String src,String sub)
    {
        // 创建正则表达式模式
        Pattern pattern = Pattern.compile(Pattern.quote(sub));
        Matcher matcher = pattern.matcher(src);

        // 统计子串出现次数
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static double getLineRatio(String text,int pos)
    {
        if(pos > text.length())pos = text.length()-1;
        char[] chars = text.toCharArray();
        String sub = text.substring(0,pos);
        int a = countSubString(sub,"\n");
        int b = countSubString(text,"\n");
        return (double)a / (double)b;
    }

    public static String getNearLines(String text,int Lines,int pos)
    {
        int len = text.length();
        if(pos >= len)pos = len-1;
        int cnt = 0;
        int st,ed;
        for(st = pos;st > 0 && cnt < Lines;st--)
        {
            if(text.charAt(st) == '\n')cnt++;
            if(cnt >= Lines)break;
        }
        cnt = 0;
        for(ed = pos;ed < len-1 && cnt < Lines;ed++)
        {
            if(text.charAt(ed) == '\n')cnt++;
            if(cnt >= Lines)break;
        }
        // 创建正则表达式模式
        Pattern pattern = Pattern.compile(Pattern.quote("```"));
        Matcher matcher = pattern.matcher(text);

        // 统计子串出现次数
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        if(count % 2 == 1)text = "```"+text;
//        return text.substring(st,ed+1);
        return text;
    }

    public static boolean checkUserValid(String ID,String password,String name)
    {
//        ICrawlable crawler = ClassRoomExecutor.getInstance();
//        try {
//            crawler.initial(ID, password, name);
//
//            crawler.login();
//            System.out.println(crawler.getCourseList());
//            crawler.searchCourse(course);
//            System.out.println(crawler.getTeachers());
//            crawler.gotoTargetTeacherCourse(teacher);
//            System.out.println(crawler.getCourseTimeTable());
//            crawler.gotoCourseTime(timeTable);
//
////            crawler.downloadCourseVideo();
//        } finally {
//            crawler.quit();
//        }
        return true;
    }


    public static ImageView getIcon(Image configimg)
    {// 加载图片资源
//        javafx.scene.image.Image configimg = new javafx.scene.image.Image(getClass().getResource("/images/configure.png").toExternalForm());
        ImageView configIcon = new ImageView(configimg);
        configIcon.setFitHeight(20);
        configIcon.setFitWidth(16);
        return configIcon;
    }

    /**
     * 判断给定的文件是否是 PowerPoint 文件。
     *
     * @param file 要判断的文件
     * @return 如果是 PowerPoint 文件，返回 true；否则返回 false
     */
    public static boolean isPowerPointFile(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        }

        // 获取文件名并检查扩展名
        String fileName = file.getName().toLowerCase(); // 忽略大小写
        return fileName.endsWith(".ppt") || fileName.endsWith(".pptx") || fileName.endsWith(".ppsx");
    }

    /**
     * 判断给定的文件路径是否是 PowerPoint 文件。
     *
     * @param filePath 要判断的文件路径
     * @return 如果是 PowerPoint 文件，返回 true；否则返回 false
     */
    public static boolean isPowerPointFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        return isPowerPointFile(new File(filePath));
    }

    public static boolean isWordFile(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        }

        // 获取文件名并检查扩展名
        String fileName = file.getName().toLowerCase(); // 忽略大小写
        return fileName.endsWith(".doc") || fileName.endsWith(".docx");
    }

    /**
     * 判断给定的文件路径是否是 PowerPoint 文件。
     *
     * @param filePath 要判断的文件路径
     * @return 如果是 PowerPoint 文件，返回 true；否则返回 false
     */
    public static boolean isWordFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        return isWordFile(new File(filePath));
    }

    public static boolean isPDFFile(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        }

        // 获取文件名并检查扩展名
        String fileName = file.getName().toLowerCase(); // 忽略大小写
        return fileName.endsWith(".pdf");
    }

    /**
     * 判断给定的文件路径是否是 PowerPoint 文件。
     *
     * @param filePath 要判断的文件路径
     * @return 如果是 PowerPoint 文件，返回 true；否则返回 false
     */
    public static boolean isPDFFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        return isPDFFile(new File(filePath));
    }

    public static int countLines(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        // 行宽度限制
        final int CHINESE_WIDTH = 20; // 一个汉字等于一个宽度单位
        final int ENGLISH_WIDTH = 45; // 英文字母、数字、标点等 40 个算一行

        int totalWidth = 0; // 当前行的总宽度
        int lineCount = 1; // 默认至少有一行

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\n') {
                // 换行符单独算一行
                lineCount++;
                totalWidth = 0; // 重置当前行宽度
            } else if (isChinese(c)) {
                // 汉字增加宽度
                totalWidth += 1;
            } else {
                // 英文字符增加宽度
                totalWidth += 0.5;
            }

            // 如果当前宽度超出限制，增加行数并重置宽度
            if (totalWidth >= CHINESE_WIDTH) {
                lineCount++;
                totalWidth = 0;
            }
        }

        return lineCount;
    }

    public static int calculateLineCount(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        int lineCount = 0; // 行数计数器
        int currentLineLength = 0; // 当前行已占用的长度

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\n') {
                // 遇到换行符，增加一行，重置当前行长度
                lineCount++;
                currentLineLength = 0;
            } else {
                // 计算字符长度，汉字和全角字符为1，半角字符为0.5
                int charLength = isFullWidth(c) ? 1 : 1;
                currentLineLength += charLength;

                // 如果当前行长度超过20，增加一行，并重置当前行长度
                if (currentLineLength > 20) {
                    lineCount++;
                    currentLineLength = charLength; // 新行以当前字符开始
                }
            }
        }

        // 如果最后一行有内容，增加一行
        if (currentLineLength > 0) {
            lineCount++;
        }

        return lineCount;
    }

    // 判断字符是否为全角字符（包括汉字）
    private static boolean isFullWidth(char c) {
        // 汉字范围 (CJK Unified Ideographs)
        if (c >= '\u4e00' && c <= '\u9fff') {
            return true;
        }

        // 全角字符范围
        return Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS;
    }


    public static int countRenderedLines(String markdown) {
        // 使用 Flexmark 库渲染 Markdown 内容
        Parser parser = Parser.builder().build();

        HtmlRenderer renderer = HtmlRenderer.builder().build();

        // 解析 Markdown 为 HTML
        com.vladsch.flexmark.util.ast.Document document = parser.parse(markdown);
        String htmlContent = renderer.render(document);

        // 计算渲染后的 HTML 内容的行数
        return countLinesInHtml(htmlContent);
    }

    private static int countLinesInHtml(String htmlContent) {
        // 去掉 HTML 标签
        String plainText = htmlContent.replaceAll("<[^>]+>", ""); // 移除 HTML 标签

        // 按行分割文本
        String[] lines = plainText.split("\n");

        // 根据行内容的长度，进行简单的行数估算（可以根据需要更复杂的逻辑）
        int totalLines = 0;
        for (String line : lines) {
            totalLines += calculateLinesForText(line);
        }
        return totalLines;
    }

    private static int calculateLinesForText(String text) {
        int chineseCharCount = 0;
        int englishCharCount = 0;

        // 统计中文和英文字符数
        for (char c : text.toCharArray()) {
            if (isChinese(c)) {
                chineseCharCount++;
            } else if (isEnglish(c)) {
                englishCharCount++;
            }
        }

        // 计算中文和英文的行数
        int chineseLines = (int) Math.ceil((double) chineseCharCount / 20);
        int englishLines = (int) Math.ceil((double) englishCharCount / 45);

        return chineseLines + englishLines;
    }

    // 判断是否是中文字符
    private static boolean isChinese(char c) {
        return String.valueOf(c).matches("[\u4E00-\u9FA5]");
    }

    // 判断是否是英文字符
    private static boolean isEnglish(char c) {
        return String.valueOf(c).matches("[a-zA-Z0-9\\p{Punct}]");
    }


public static void main(String[] args)
    {
        String tmp = "hello";
        System.out.println(tmp.substring(1,5));
    }

}
