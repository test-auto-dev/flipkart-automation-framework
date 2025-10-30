package com.flipkart.automation.utils;

import com.flipkart.automation.constants.FrameworkConstants;
import com.flipkart.automation.drivers.BrowserFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    
    public static String captureScreenshot(String testName) {
        try {
            WebDriver driver = BrowserFactory.getDriver();
            
            if (driver == null) {
                LoggerUtil.error("Driver is null, cannot capture screenshot");
                return null;
            }
            
            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String screenshotName = testName + "_" + timestamp + ".png";
            String screenshotPath = FrameworkConstants.SCREENSHOT_PATH + screenshotName;
            
            File screenshotDir = new File(FrameworkConstants.SCREENSHOT_PATH);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotPath);
            FileUtils.copyFile(source, destination);
            
            LoggerUtil.info("Screenshot captured: " + screenshotPath);
            return screenshotPath;
            
        } catch (Exception e) {
            LoggerUtil.error("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
