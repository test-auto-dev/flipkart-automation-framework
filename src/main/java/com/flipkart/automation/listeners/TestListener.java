package com.flipkart.automation.listeners;

import com.flipkart.automation.reports.ExtentReportManager;
import com.flipkart.automation.utils.ScreenshotUtil;
import com.flipkart.automation.utils.LoggerUtil;
import org.testng.*;

public class TestListener implements ITestListener, ISuiteListener {
    
    @Override
    public void onStart(ISuite suite) {
        LoggerUtil.info("Test Suite Started: " + suite.getName());
        ExtentReportManager.initReport();
    }
    
    @Override
    public void onFinish(ISuite suite) {
        LoggerUtil.info("Test Suite Finished: " + suite.getName());
        ExtentReportManager.flushReport();
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        LoggerUtil.info("Test Started: " + result.getMethod().getMethodName());
        ExtentReportManager.createTest(result.getMethod().getMethodName(), 
            result.getMethod().getDescription());
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        LoggerUtil.info("Test Passed: " + result.getMethod().getMethodName());
        ExtentReportManager.logPass("Test Passed: " + result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        LoggerUtil.error("Test Failed: " + result.getMethod().getMethodName());
        LoggerUtil.error("Failure Reason: " + result.getThrowable());
        
        String screenshotPath = ScreenshotUtil.captureScreenshot(
            result.getMethod().getMethodName());
        
        ExtentReportManager.logFail("Test Failed: " + result.getMethod().getMethodName());
        ExtentReportManager.logFail("Error: " + result.getThrowable().getMessage());
        
        if (screenshotPath != null) {
            ExtentReportManager.getTest().addScreenCaptureFromPath(screenshotPath);
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        LoggerUtil.info("Test Skipped: " + result.getMethod().getMethodName());
        ExtentReportManager.logSkip("Test Skipped: " + result.getMethod().getMethodName());
    }
}
