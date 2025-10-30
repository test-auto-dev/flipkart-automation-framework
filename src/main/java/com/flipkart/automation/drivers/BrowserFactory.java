package com.flipkart.automation.drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.flipkart.automation.utils.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserFactory {
    
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    
    public static WebDriver createDriver(String browser, boolean headless, String gridUrl) {
        WebDriver webDriver;
        
        try {
            if (gridUrl != null && !gridUrl.isEmpty()) {
                webDriver = createRemoteDriver(browser, headless, gridUrl);
            } else {
                webDriver = createLocalDriver(browser, headless);
            }
            
            driver.set(webDriver);
            LoggerUtil.info("Browser initialized: " + browser);
            return webDriver;
            
        } catch (Exception e) {
            LoggerUtil.error("Failed to initialize browser: " + e.getMessage());
            throw new RuntimeException("Browser initialization failed", e);
        }
    }
    
    private static WebDriver createLocalDriver(String browser, boolean headless) {
        WebDriver webDriver;
        
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                webDriver = new ChromeDriver(getChromeOptions(headless));
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                webDriver = new FirefoxDriver(getFirefoxOptions(headless));
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                webDriver = new EdgeDriver(getEdgeOptions(headless));
                break;
                
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        
        return webDriver;
    }
    
    private static WebDriver createRemoteDriver(String browser, boolean headless, String gridUrl) 
            throws MalformedURLException {
        
        RemoteWebDriver remoteDriver;
        
        switch (browser.toLowerCase()) {
            case "chrome":
                remoteDriver = new RemoteWebDriver(new URL(gridUrl), getChromeOptions(headless));
                break;
                
            case "firefox":
                remoteDriver = new RemoteWebDriver(new URL(gridUrl), getFirefoxOptions(headless));
                break;
                
            case "edge":
                remoteDriver = new RemoteWebDriver(new URL(gridUrl), getEdgeOptions(headless));
                break;
                
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        
        return remoteDriver;
    }
    
    private static ChromeOptions getChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
        }
        
        return options;
    }
    
    private static FirefoxOptions getFirefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        return options;
    }
    
    private static EdgeOptions getEdgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        return options;
    }
    
    public static WebDriver getDriver() {
        return driver.get();
    }
    
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
            LoggerUtil.info("Browser closed successfully");
        }
    }
}
