package org.threeQuarters;

import org.threeQuarters.DataScrapper.ClassRoomExecutor;
import org.threeQuarters.DataScrapper.ICrawlable;

public class CrawlerExample {

    static String number = "22375080";
    static String password = "njqyb18283827207";
    static String name = "杨佳宇轩";

    static String course = "计算机硬件基础（软件专业）";
    static String teacher = "邓莹莹";
    static String timeTable = "第10周星期2第3,4节";

    public static void main(String[] args) {
        ICrawlable crawler = ClassRoomExecutor.getInstance();
        try {
            crawler.initial(number, password, name);

            crawler.login();
            System.out.println(crawler.getCourseList());
            crawler.searchCourse(course);
            System.out.println(crawler.getTeachers());
            crawler.gotoTargetTeacherCourse(teacher);
            System.out.println(crawler.getCourseTimeTable());
            crawler.gotoCourseTime(timeTable);

//            crawler.downloadCourseVideo();
        } finally {
            crawler.quit();
        }
    }
}
