package org.threeQuarters.scrapper;

import org.threeQuarters.database.DirectoryInitial;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    protected ChromeOptions options;
    protected WebDriver driver;
    protected WebDriverWait wait;

    protected static final String LOGIN_URL = "https://sso.buaa.edu.cn/login";

    public Crawler() {
        WebDriverManager.chromedriver().setup();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", DirectoryInitial.paths.get("PPT"));
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_uploaded", true);
        prefs.put("safebrowsing.enabled", true);
        options = new ChromeOptions();
//        options.addArguments("--headless");
        options.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
}
