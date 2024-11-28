package scrapper;

import database.DirectoryInitial;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PPTExecutor extends Crawler implements IPPTCrawlable {
    private String name;
    private String number;
    private String password;

    private String currentURL;
    private final List<String> pages = new ArrayList<>();
    private List<String> timeTable;
    private WebElement timeTableSpan;
    private WebElement resourceSpan;
    private String PPTPath;

    private static final String TARGET_URL = "https://spoc.buaa.edu.cn/spocnew/jxkj2";
    private static final String courseClassName = "wdkc";
    private static final String courseTimeSpanXPath = "/html/body/div[1]/div/div[3]/div/div/div[2]/div/div/div/div/div[1]/div/div/div/div[1]/div/div/div/div/div/div/span";
    private static final String resourceSpanXPath = "/html/body/div[1]/div/div[3]/div/div/div[2]/div/div/div/div/div[1]/div/div/div/div[2]/div/div[2]";

    private final ClassListExecutor classListExecutor = new ClassListExecutor();

    public PPTExecutor() {}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getNumber() { return number; }

    public void setNumber(String number) { this.number = number; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    /**
     * use student id number, password and name to initial
     * @param number student id number
     * @param password password
     * @param name name
     */
    @Override
    public void initial(String number, String password, String name) {
        this.number = number;
        this.password = password;
        this.name = name;
        ssoLog.setNumber(number);
        ssoLog.setPassword(password);
    }

    /**
     * login
     * @return true if successfully login
     */
    @Override
    public boolean login() {
        try {
            driver.get(LOGIN_URL);
            ssoLog.login(driver, wait);
            driver.get(TARGET_URL);
            ssoLog.login(driver, wait);

            // MUST wait and get page source roughly!!!
            Thread.sleep(2000);
            String PageSource = driver.getPageSource();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * get course list using classListExecutor object
     * which will start another chromedriver thread
     * @return list of course
     */
    @Override
    public List<String> getCourseList() {
        return classListExecutor.getCourseList();
    }

    /**
     * after choosing the course
     * click and switch to the web
     * and get to detail timetable
     * @param course chosen course
     * @return true if successfully getting to
     */
    @Override
    public boolean gotoTargetCourse(String course) {
        return clickTargetCourse(course) && gotoDetailContent(course);
    }

    /**
     * replace the url to get to detail timetable
     * based on the web string operation
     * @param course chosen course
     * @return true iff successfully
     */
    private boolean gotoDetailContent(String course) {
        // https://spoc.buaa.edu.cn/spocnew/mycourse/coursecenter/202420251B210031002001/notice/202420251B210031002001kcgg?isZjDhl
        // https://spoc.buaa.edu.cn/spocnew/mycourse/coursecenter/202420251B210031002001/substance/202420251B210031002001jxml?sflx=2&rouType&isZjDhl
        try {
            //MUST wait for full url
            Thread.sleep(2000);
            String url = driver.getCurrentUrl();
            url = url.replace("notice", "substance").replace("kcgg?isZjDhl", "jxml?sflx=2&rouType&isZjDhl");
            driver.get(url);

            timeTableSpan = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(courseTimeSpanXPath)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * click the course button
     * @param course chosen course
     * @return true if successfully
     */
    private boolean clickTargetCourse(String course) {
        try {
            List<WebElement> courseList = driver.findElements(By.className(courseClassName));
            for(WebElement courseElement : courseList) {
                if(List.of(courseElement.getText().split("\n")).get(1).equals(course)) {
                    WebElement enterClassButton = courseElement.findElement(By.className("jrkc"));

                    Actions actions = new Actions(driver);
                    actions.moveToElement(enterClassButton).perform();
                    wait.until(ExpectedConditions.elementToBeClickable(enterClassButton));
                    enterClassButton.click();

                    WindowUtils.switchPage(currentURL, driver, pages);
                    wait.until(driver -> ((JavascriptExecutor) driver)
                            .executeScript("return document.readyState").equals("complete"));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * get timetable other than special div "课后"
     * @return list of timetable
     */
    @Override
    public List<String> getTimeTable() {
        timeTable = new ArrayList<>(List.of(timeTableSpan.getText().trim().split("\n")));
        timeTable.remove("课后");
        return timeTable;
    }

    /**
     * download all PPTs in the course time to local
     * and return the last path
     * DEFAULT: create a note based on one PPT
     * @param targetTime chosen time
     * @return absolute path of downloaded PPT
     * @throws NoneResourcesException if no resources in the chosen course time
     */
    @Override
    public String downloadPPT(String targetTime) throws NoneResourcesException {
        if(!gotoTargetTime(targetTime) || !walkDownload())
            return null;
        return PPTPath;
    }

    /**
     * click to the target time
     * @param time chosen time
     * @return true if successfully
     */
    private boolean gotoTargetTime(String time) {
        try {
            List<WebElement> allTimeElement = timeTableSpan.findElements(By.className("collapse-panel"));
            for(WebElement element : allTimeElement) {
                if(element.getText().equals(time)) {
                    element.click();
                    resourceSpan = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(resourceSpanXPath)));
                    break;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * walk through all the resources in the course time
     * download all resources ends with ".pptx" or ".ppt"
     * @return true if successfully downloads
     * @throws NoneResourcesException if no resources in the course time
     */
    private boolean walkDownload() throws NoneResourcesException {
        try {
            Thread.sleep(1000);
            List<WebElement> resources = resourceSpan.findElements(By.className("course-nl-list"));

            boolean found = false;

            for(WebElement resourceElement : resources) {
                List<String> detail = List.of(resourceElement.getText().split("\n"));
                if(detail.get(0).endsWith(".pptx") || detail.get(0).endsWith(".ppt")) {
                    found = true;
                    System.out.println(detail);
                    WebElement downloadButton = resourceElement.findElement(By.className("course-zw-more"));
                    downloadButton.click();
                    if(waitForFileToDownload(DirectoryInitial.paths.get("PPT"), detail.get(0))) {
                       System.out.println("download PPT successfully!");
                    } else System.out.println("download time out...");
                }
            }

            if(!found)
                throw new NoneResourcesException();

            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * wait until the file downloaded
     * @param downloadDir target directory, default ./cache/PPT
     * @param fileName full filename including type like "文件""作业"
     * @return true if successfully
     */
    private boolean waitForFileToDownload(String downloadDir, String fileName) {
        File dir = new File(downloadDir);
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < 10 * 1000L) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (fileName.contains(file.getName()) && !file.getName().endsWith(".crdownload")) {
                        PPTPath = Paths.get(downloadDir, file.getName()).toString();
                        return true;
                    }
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void quit() {
        driver.quit();
        classListExecutor.quit();
    }


}
