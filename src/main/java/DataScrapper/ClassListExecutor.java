package DataScrapper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ClassListExecutor {

    private WebDriver driver;
    private WebDriverWait wait;

    public ClassListExecutor() {}
    public ClassListExecutor(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * get class list for the login user
     * @return class list<String>
     */
    public List<String> getCourseList() {
        return null;
    }
}
