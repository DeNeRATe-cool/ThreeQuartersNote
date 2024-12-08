package scrapper;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.util.*;

public class VideoExecutor extends Crawler implements IVideoCrawlable {

    private static final String historyXPath = "/html/body/div/div[2]/div/div/div[1]/div/div[2]/p";
    private static final String timeTableContentXPath = "/html/body/div/div[2]/div/div/div[1]/div[2]/div[3]";
    private static final String videoSrcXPath = "/html/body/div[1]/div[2]/div/div/div[2]/div[2]/div[1]/div[1]/div[2]/div/div[2]/div[1]/div/div[2]/div[1]/div[1]/video";

    private List<WebElement> courseElements;
    private List<WebElement> courseTimeTable;
    private String currentURL;
    private String videoSource;
    private List<String> pages = new ArrayList<>();

    private String number;
    private String password;
    private String name;
    private String course;
    private String teacher;
    private String timeTable;

    private final ClassListExecutor classListExecutor = new ClassListExecutor();
    private TeacherExecutor teacherExecutor;
    private TimeTableExecutor timeTableExecutor;

    public VideoExecutor() {}

    public String getNumber() {
        return number;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getTimeTable() {
        return timeTable;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setTimeTable(String timeTable) {
        this.timeTable = timeTable;
    }

    @Override
    public void initial(String number, String password, String name) {
        this.number = number;
        this.password = password;
        this.name = name;
        ssoLog.setNumber(number);
        ssoLog.setPassword(password);
    }

    private void setupTeacherExecutor() {
        teacherExecutor = new TeacherExecutor(courseElements);
    }

    private void setuptimeTableExecutor() {
        timeTableExecutor = new TimeTableExecutor(courseTimeTable);
    }

    /**
     * turn into the iframe first and send then login
     * @return true iff login successfully
     */
    @Override
    public boolean login() {
        try {
            driver.get(LOGIN_URL);
            ssoLog.login(driver, wait);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * directly visit searching web for the course
     * @param course target course to search
     * @return true iff search the courses successfully
     */
    @Override
    public boolean searchCourse(String course) {
        setCourse(course);

        try {
            String targetURL = "https://classroom.msa.buaa.edu.cn/searchContent?title=" + this.course + "&realname=&trans=&page=1&tenant_code=21";
            driver.get(targetURL);

            wait.until(driver -> ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(historyXPath)));

            courseElements = driver.findElements(By.className("result-course-wrapper"));
            if(courseElements.isEmpty())
                return false;

            setupTeacherExecutor();

            for (WebElement element : courseElements) {
                String text = element.getText();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * iterate the elements and switch to the target course
     * List<String> params is like
     * [北京航空航天大学, 计算机硬件基础（软件专业）, 牛建伟]
     * [软件学院, 计算机硬件基础（软件专业）, 邓莹莹]
     * @param teacher the teacher teaches the course
     * @return true iff gotten the course teaching by the teacher
     */
    public boolean gotoTargetCourse(String teacher) {
        setTeacher(teacher);
        for(WebElement element : courseElements) {
            List<String> params = List.of(element.getText().split("\n"));
            if(params.get(params.size() - 1).equals(teacher)) {
                element.click();
                wait.until(driver -> ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState").equals("complete"));

                WindowUtils.switchPage(currentURL, driver, pages);
                return true;
            }
        }
        return false;
    }

    /**
     * get timetable in the target website with class name "content-inner-one"
     * and set up the timeTableExecutor
     * @return true iff get the timetable successfully
     */
    public boolean targetCourseTimeTable() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(timeTableContentXPath)));
            courseTimeTable = driver.findElements(By.className("content-inner-one"));

            if (courseTimeTable.isEmpty())
                return false;

            setuptimeTableExecutor();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean gotoTargetTeacherCourse(String teacher) {
        try {
            gotoTargetCourse(teacher);
            targetCourseTimeTable();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * List<String> params is like
     * [智播, 第1周星期2第3,4节, 刘子鹏, 回放, 上次学到 00:00:03]
     * [智播, 第1周星期5第6,7节, 刘子鹏, 未开始]
     * @param timeTable chosen timetable
     * @return true iff get to the timetable successfully
     */
    @Override
    public boolean gotoCourseTime(String timeTable) {
        setTimeTable(timeTable);
        try {
            for (WebElement element : courseTimeTable) {
                List<String> params = List.of(element.getText().split("\n"));
                if (params.contains(teacher) && params.contains(timeTable)) {

                    element.click();
                    wait.until(driver -> ((JavascriptExecutor) driver)
                            .executeScript("return document.readyState").equals("complete"));

                    // MUST wait roughly!!!
                    Thread.sleep(1000);

                    for (String windowHandle : driver.getWindowHandles()) {
                        if (!pages.contains(windowHandle)) {
                            pages.add(windowHandle);
                            driver.switchTo().window(windowHandle);
                            currentURL = driver.getWindowHandle();
                            System.out.println("Current URL: " + currentURL);
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * after click to the target url
     * download the video to the default local path
     * @return true iff download successfully
     */
    @Override
    public boolean downloadCourseVideo() {
        try {
            wait.until(driver -> ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete"));
//            WebElement video = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(videoSrcXPath)));
            Thread.sleep(5000);
            WebElement video = driver.findElement(By.xpath(videoSrcXPath));
            videoSource = video.getAttribute("src");

            try {
                VideoDownloader.downloadFile(videoSource, course + "_" + teacher + "_" + timeTable + ".mp4");
                System.out.println("download course video successfully!");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<String> getTeachers() {
        return teacherExecutor.getTeachers();
    }

    @Override
    public List<String> getCourseTimeTable() {
        return timeTableExecutor.getCourseTimeTable();
    }

    @Override
    public List<String> getCourseList() {
        return classListExecutor.getCourseList();
    }

    @Override
    public void quit() {
        driver.quit();
        classListExecutor.quit();
    }

    public static void main(String[] args) {
        VideoExecutor videoExecutor = new VideoExecutor();
        videoExecutor.initial("","22374271","buAA101199@.@");
//        videoExecutor.login();
        System.out.println(videoExecutor.login());
    }

}
