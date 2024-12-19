package org.example;

import org.threeQuarters.database.DirectoryInitial;
import org.threeQuarters.scrapper.IPPTCrawlable;
import org.threeQuarters.scrapper.NoneResourcesException;
import org.threeQuarters.scrapper.PPTExecutor;

public class PPTCrawlerExample {

    static String number = ""; // e.g. 22375080
    static String password = "";
    static String name = "杨佳宇轩";

    static String course = "计算机硬件基础（软件专业）";
    static String timeTable = "第3周星期5第6,7节";

    public static void main(String[] args) {
        DirectoryInitial.initial();

        IPPTCrawlable crawler = new PPTExecutor();
        try {
            crawler.initial(number, password, name);

            crawler.login();
            System.out.println(crawler.getCourseList());
            crawler.gotoTargetCourse(course);
            System.out.println(crawler.getTimeTable());
            System.out.println(crawler.downloadPPT(timeTable));
        } catch (NoneResourcesException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            crawler.quit();
        }

    }
}
