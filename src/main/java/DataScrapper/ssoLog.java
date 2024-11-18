package DataScrapper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ssoLog {
    private static final String usernameXPath = "/html/body/div[2]/div/div[3]/div[2]/div[1]/div[1]/div/input";
    private static final String passwordXPath = "/html/body/div[2]/div/div[3]/div[2]/div[1]/div[3]/div/input";
    private static final String buttonXPath = "/html/body/div[2]/div/div[3]/div[2]/div[1]/div[7]/input";

    private static String number;
    private static String password;

    ssoLog() {}
    ssoLog(String number, String password) {
        ssoLog.number = number;
        ssoLog.password = password;
    }

    public static void setNumber(String number) {
        ssoLog.number = number;
    }

    public static void setPassword(String password) {
        ssoLog.password = password;
    }

    public static void login(WebDriver driver, WebDriverWait wait) {
        WebElement iframe = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@id='loginIframe']")));
        driver.switchTo().frame(iframe);

        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(usernameXPath)));
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(passwordXPath)));

        usernameInput.sendKeys(number);
        passwordInput.sendKeys(password);

        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(buttonXPath)));
        loginButton.click();
    }
}
