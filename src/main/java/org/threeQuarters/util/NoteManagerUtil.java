package org.threeQuarters.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoteManagerUtil {

    public static String removeUUIDFromContent(String content) {
        if(!validateStringStart(content))
        {
            matchPatternAndPrintErrors(content);
            System.out.println("boo how");
            System.out.println(content);
            return content;
        }
        String pattern = "^uuid:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}(\\r\\n|\\r|\\n)" +
                "coursename:.*(\\r\\n|\\r|\\n)---(\\r\\n|\\r|\\n)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(content);
        m.find(0);
        System.out.println(content.substring(m.start(),m.end()));
        StringBuffer sb = new StringBuffer(content);
        sb.delete(m.start(), m.end());
        content = new String(sb);
        System.out.println(content);
        return content;
    }

    public static String getUUIDFromContent(String content) {
        if(!validateStringStart(content))
        {
            matchPatternAndPrintErrors(content);
            System.out.println("boo how");
            System.out.println(content);
            return content;
        }
        String pattern = "^uuid:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}(\\r\\n|\\r|\\n)" +
                "coursename:.*(\\r\\n|\\r|\\n)---(\\r\\n|\\r|\\n)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(content);
        m.find(0);
        System.out.println(content.substring(m.start(),m.end()));
        return content.substring(m.start(),m.end());
    }

    public static void matchPatternAndPrintErrors(String input) {
        // 定义正则表达式
        String uuidRegex = "^uuid:([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})";
        String coursenameRegex = "coursename:(.+)";
        String endingRegex = "---";

        // 逐个匹配不同部分并输出错误
        // 1. 匹配 UUID 部分
        Pattern uuidPattern = Pattern.compile(uuidRegex);
        Matcher uuidMatcher = uuidPattern.matcher(input);
        if (!uuidMatcher.find()) {
            System.out.println("UUID部分不匹配！");
        } else {
            System.out.println("UUID匹配成功: " + uuidMatcher.group(1));
        }

        // 2. 匹配 coursename 部分
        Pattern coursenamePattern = Pattern.compile(coursenameRegex);
        Matcher coursenameMatcher = coursenamePattern.matcher(input);
        if (!coursenameMatcher.find()) {
            System.out.println("课程名部分不匹配！");
        } else {
            System.out.println("课程名匹配成功: " + coursenameMatcher.group(1));
        }

        // 3. 匹配结束符部分
        Pattern endingPattern = Pattern.compile(endingRegex);
        Matcher endingMatcher = endingPattern.matcher(input);
        System.out.println("input:"+input);
        if (!endingMatcher.find()) {
            System.out.println("结束符部分不匹配！");
        } else {
            System.out.println("结束符匹配成功");
        }
    }


    // 含有配置参数开头
    public static boolean validateStringStart(String input) {
        // 定义正则表达式
        String regex = "^uuid:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}(\\r\\n|\\r|\\n)" +
                "coursename:.*(\\r\\n|\\r|\\n)---(\\r\\n|\\r|\\n)";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // 判断是否匹配
        return matcher.find();
    }

    public static String extractUuid(String input) {

        int pt = 0;
        while(input.charAt(pt)!=':') {pt++;}
        StringBuffer sb = new StringBuffer();
        pt++;
        while(input.charAt(pt)!='\n' && input.charAt(pt)!='\r') {sb.append(input.charAt(pt++));}
        return new String(sb);
    }

    public static String getPrefix(String uuid,String coursename){
        // 构造模式串
        String prefix = "uuid:" + uuid + "\n" +
                "coursename:" + coursename + "\n---\n";
        return prefix;
    }

    public static String addPrefixWithUuidAndCoursename(String uuid, String coursename, String input) {
        // 构造模式串
        String prefix = "uuid:" + uuid + "\n" +
                "coursename:" + coursename + "\n---\n";

        // 将模式串添加到输入字符串前面
        return prefix + input;
    }


    public static void main(String[] args) {
        String pattern = ".*?---";
        String str = "计组球球了!!!";
//        str = removeUUIDFromContent(str);
        str = addPrefixWithUuidAndCoursename(UUID.randomUUID().toString(), "计组",str);
        System.out.println(str);
    }

}
