package DataScrapper;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class SSOExecutor {

    private static final SSOExecutor instance = new SSOExecutor();

    private static ChromeOptions options;
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String LOGIN_URL = "https://sso.buaa.edu.cn/login";
    private static final String usernameXPath = "/html/body/div[2]/div/div[3]/div[2]/div[1]/div[1]/div/input";
    private static final String passwordXPath = "/html/body/div[2]/div/div[3]/div[2]/div[1]/div[3]/div/input";
    private static final String buttonXPath = "/html/body/div[2]/div/div[3]/div[2]/div[1]/div[7]/input";
    private static final String historyXPath = "/html/body/div/div[2]/div/div/div[1]/div/div[2]/p";
    private static final String timeTableContentXPath = "/html/body/div/div[2]/div/div/div[1]/div[2]/div[3]";

    private List<WebElement> courseElements;
    private List<WebElement> courseTimeTable;
    private String currentURL;

    private String number;
    private String password;
    private String name;
    private String course;
    private String teacher;

    static {
        WebDriverManager.chromedriver().setup();
        options = new ChromeOptions();
//        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private SSOExecutor() {}

    // 单例模式
    public static SSOExecutor getInstance() {
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

    public void initial(String number, String password, String name) {
        this.number = number;
        this.password = password;
        this.name = name;
    }

    /**
     * turn into the iframe first and send then login
     * @return true iff login successfully
     * @throws Exception
     */
    public boolean login() throws Exception {
        try {
            driver.get(LOGIN_URL);

            WebElement iframe = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@id='loginIframe']")));
            driver.switchTo().frame(iframe);

            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(usernameXPath)));
            WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(passwordXPath)));

            username.sendKeys(this.number);
            password.sendKeys(this.password);

            WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(buttonXPath)));

            loginButton.click();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * directly visit searching web for the course
     * @param course target course to search
     * @param teacher the teacher teaches the course
     * @return true iff search the courses successfully
     * @throws Exception
     */
    public boolean searchCourse(String course, String teacher) throws Exception {
        setCourse(course);
        setTeacher(teacher);

        try {
            String targetURL = "https://classroom.msa.buaa.edu.cn/searchContent?title=" + this.course + "&realname=&trans=&page=1&tenant_code=21";
            driver.get(targetURL);

            wait.until(driver -> ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(historyXPath)));

            courseElements = driver.findElements(By.className("result-course-wrapper"));
            if(courseElements.isEmpty())
                return false;

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
     * @return true iff gotten the course teaching by the teacher
     */
    public boolean gotoTargetCourse() {
        for(WebElement element : courseElements) {
            List<String> params = List.of(element.getText().split("\n"));
            System.out.println(params);
            if(params.get(params.size() - 1).equals(teacher)) {
                element.click();
                wait.until(driver -> ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState").equals("complete"));

                currentURL = driver.getWindowHandle();
                Set<String> allWindowHandles = driver.getWindowHandles();

                for (String windowHandle : allWindowHandles) {
                    if (!windowHandle.equals(currentURL)) {
                        driver.switchTo().window(windowHandle);
                        currentURL = driver.getWindowHandle();
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
     * List<String> params is like
     * [智播, 第1周星期2第3,4节, 刘子鹏, 回放, 上次学到 00:00:03]
     * [智播, 第1周星期5第6,7节, 刘子鹏, 未开始]
     * @return true iff get the timetable successfully
     */
    public boolean targetCourseTimeTable() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(timeTableContentXPath)));
        courseTimeTable = driver.findElements(By.className("content-inner-one"));

        if(courseElements.isEmpty())
            return false;

        for(WebElement element : courseTimeTable) {
            List<String> params = List.of(element.getText().split("\n"));
            System.out.println(params);
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        String number = "22375080";
        String password = "njqyb18283827207";
        String name = "杨佳宇轩";

        String course = "离散数学";
        String teacher = "张梦豪";

        SSOExecutor ssoExecutor = SSOExecutor.getInstance();
        ssoExecutor.initial(number, password, name);

        ssoExecutor.login();


        ssoExecutor.searchCourse(course, teacher);
        ssoExecutor.gotoTargetCourse();
        ssoExecutor.targetCourseTimeTable();
    }
}
