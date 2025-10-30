package com.flipkart.automation.utils;

import com.flipkart.automation.constants.FrameworkConstants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class WaitUtil {
    
    private static WebDriver driver;
    
    public WaitUtil(WebDriver driver) {
        this.driver = driver;
    }
    
    public static boolean waitForElementToBeVisible(WebElement element, int seconds) {
        try {
            WebDriver driver = com.flipkart.automation.drivers.BrowserFactory.getDriver();
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean waitForElementToBeClickable(WebElement element) {
        try {
            WebDriver driver = com.flipkart.automation.drivers.BrowserFactory.getDriver();
            new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.EXPLICIT_WAIT_TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean waitForElementsToBeVisible(List<WebElement> elements, int seconds) {
        try {
            WebDriver driver = com.flipkart.automation.drivers.BrowserFactory.getDriver();
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfAllElements(elements));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
