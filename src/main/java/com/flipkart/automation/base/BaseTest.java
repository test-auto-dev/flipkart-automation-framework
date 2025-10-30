package com.flipkart.automation.base;

import com.flipkart.automation.constants.FrameworkConstants;
import com.flipkart.automation.drivers.BrowserFactory;
import com.flipkart.automation.reports.ExtentReportManager;
import com.flipkart.automation.utils.LoggerUtil;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import java.time.Duration;

public class BaseTest {
    
    protected WebDriver driver;
    
    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "environment", "headless", "gridUrl"})
    public void setUp(
            @Optional("chrome") String browser,
            @Optional("qa") String environment,
            @Optional("false") String headless,
            @Optional("") String gridUrl) {
        
        LoggerUtil.info("Starting test execution");
        
        browser = System.getProperty("browser", browser);
        environment = System.getProperty("environment", environment);
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", headless));
        gridUrl = System.getProperty("gridUrl", gridUrl);
        
        driver = BrowserFactory.createDriver(browser, isHeadless, gridUrl);
        driver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(FrameworkConstants.IMPLICIT_WAIT_TIMEOUT));
        driver.manage().timeouts().pageLoadTimeout(
            Duration.ofSeconds(FrameworkConstants.PAGE_LOAD_TIMEOUT));
        driver.manage().window().maximize();
        
        String url = "https://www.flipkart.com";
        driver.get(url);
        LoggerUtil.info("Navigated to: " + url);
    }
    
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        BrowserFactory.quitDriver();
        LoggerUtil.info("Test execution completed");
    }
}
