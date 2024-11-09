package DataScrapper;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class ClassRoomExecutor {

    /* please use try-finally with the class */

    private static final ClassRoomExecutor instance = new ClassRoomExecutor();

    private static final ChromeOptions options;
    private static final WebDriver driver;
    private static final WebDriverWait wait;
    private static final String LOGIN_URL = "https://sso.buaa.edu.cn/login";
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

    private ClassListExecutor classListExecutor = new ClassListExecutor();
    private TeacherExecutor teacherExecutor;
    private TimeTableExecutor timeTableExecutor;

    static {
        WebDriverManager.chromedriver().setup();
        options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private ClassRoomExecutor() {}

    // 单例模式
    public static ClassRoomExecutor getInstance() {
        return instance;
    }

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

    public void initial(String number, String password, String name) {
        this.number = number;
        this.password = password;
        this.name = name;
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
     * @throws Exception
     */
    public boolean login() throws Exception {
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
     * @throws Exception
     */
    public boolean searchCourse(String course) throws Exception {
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

                currentURL = driver.getWindowHandle();
                pages.add(currentURL);
                System.out.println("Current URL: " + currentURL);
                Set<String> allWindowHandles = driver.getWindowHandles();

                for (String windowHandle : allWindowHandles) {
                    if(!pages.contains(windowHandle)) {
                        pages.add(windowHandle);
                        driver.switchTo().window(windowHandle);
                        currentURL = driver.getWindowHandle();
                        System.out.println("Current URL: " + currentURL);
                        break;
                    }
                }

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

    /**
     * List<String> params is like
     * [智播, 第1周星期2第3,4节, 刘子鹏, 回放, 上次学到 00:00:03]
     * [智播, 第1周星期5第6,7节, 刘子鹏, 未开始]
     * @param timeTable chosen timetable
     * @return true iff get to the timetable successfully
     */
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
    public boolean downloadCourseVideo() throws Exception {
        try {
            wait.until(driver -> ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete"));
            WebElement video = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(videoSrcXPath)));
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

    public List<String> getTeachers() {
        return teacherExecutor.getTeachers();
    }

    public List<String> getCourseTimeTable() {
        return timeTableExecutor.getCourseTimeTable();
    }

    public List<String> getCourseList() {
        return classListExecutor.getCourseList();
    }

    public static void main(String[] args) throws Exception {
        try {
            String number = "22375080";
            String password = "njqyb18283827207";
            String name = "杨佳宇轩";

            String course = "计算机硬件基础（软件专业）";
            String teacher = "邓莹莹";
            String timeTable = "第9周星期5第6,7节";

            ssoLog.setNumber(number);
            ssoLog.setPassword(password);

            ClassRoomExecutor classRoomExecutor = ClassRoomExecutor.getInstance();
            classRoomExecutor.initial(number, password, name);

            classRoomExecutor.login();

            List<String> classes = classRoomExecutor.getCourseList();
            System.out.println(classes);
            classRoomExecutor.searchCourse(course);

            List<String> teachers = classRoomExecutor.getTeachers();
            System.out.println(teachers);
            classRoomExecutor.gotoTargetCourse(teacher);

            classRoomExecutor.targetCourseTimeTable();

            List<String> courseTimeTable = classRoomExecutor.getCourseTimeTable();
            System.out.println(courseTimeTable);
            classRoomExecutor.gotoCourseTime(timeTable);
            classRoomExecutor.downloadCourseVideo();
        } finally {
            driver.quit();
        }
    }
}
