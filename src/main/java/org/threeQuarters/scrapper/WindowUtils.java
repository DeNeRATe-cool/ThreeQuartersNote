package org.threeQuarters.scrapper;

import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Set;

public class WindowUtils {
    public static void switchPage(String currentURL, WebDriver driver, List<String> pages) {
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
    }
}
