package org.threeQuarters;

import database.DirectoryInitial;
import scrapper.VideoExecutor;
import scrapper.IVideoCrawlable;

public class VideoCrawlerExample {

    static String number = "22374271"; // e.g. 22375080
    static String password = "buAA101199@.@";
    static String name = "杨佳宇轩";

    static String course = "计算机硬件基础（软件专业）";
    static String teacher = "牛建伟";
    static String timeTable = "第3周星期2第3,4节";

    public static void main(String[] args) {
        DirectoryInitial.initial();

        IVideoCrawlable crawler = new VideoExecutor();
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
