package com.flipkart.automation.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.flipkart.automation.constants.FrameworkConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {
    
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    public static void initReport() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String reportPath = FrameworkConstants.EXTENT_REPORT_PATH + 
                "ExtentReport_" + timestamp + ".html";
            
            File reportDir = new File(FrameworkConstants.EXTENT_REPORT_PATH);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("Flipkart Automation Test Report");
            sparkReporter.config().setReportName("Test Execution Report");
            sparkReporter.config().setEncoding("utf-8");
            
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Application", "Flipkart");
            extent.setSystemInfo("Environment", System.getProperty("environment", "QA"));
            extent.setSystemInfo("Browser", System.getProperty("browser", "Chrome"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
        }
    }
    
    public static void createTest(String testName) {
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
    }
    
    public static void createTest(String testName, String description) {
        ExtentTest extentTest = extent.createTest(testName, description);
        test.set(extentTest);
    }
    
    public static ExtentTest getTest() {
        return test.get();
    }
    
    public static void logInfo(String message) {
        getTest().log(Status.INFO, message);
    }
    
    public static void logPass(String message) {
        getTest().log(Status.PASS, message);
    }
    
    public static void logFail(String message) {
        getTest().log(Status.FAIL, message);
    }
    
    public static void logSkip(String message) {
        getTest().log(Status.SKIP, message);
    }
    
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
