
package org.threeQuarters.scrapper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ClassListExecutor extends Crawler {

    private static final String TARGET_URL = "https://spoc.buaa.edu.cn/spocnew/jxkj2";
    private static final String courseContentXPath = "/html/body/div[1]/div/div[9]/div[2]";
    private static final String courseClassName = "wdkc";

    public ClassListExecutor() {}

    /**
     * get class list for the login user
     * @return class list<String>
     */
    public List<String> getCourseList() throws RuntimeException {
        try {
            driver.get(LOGIN_URL);
            ssoLog.login(driver, wait);
            driver.get(TARGET_URL);
            ssoLog.login(driver, wait);

            // MUST wait and get page source roughly!!!
            Thread.sleep(2000);
            String PageSource = driver.getPageSource();

            List<WebElement> courseList = driver.findElements(By.className(courseClassName));
            List<String> courses = new ArrayList<String>();
            for(WebElement course: courseList) {
                List<String> params = List.of(course.getText().split("\n"));
                courses.add(params.get(1));
            }
            return courses;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void quit() {
        driver.quit();
    }
}
