package com.flipkart.automation.listeners;

import com.flipkart.automation.constants.FrameworkConstants;
import com.flipkart.automation.utils.LoggerUtil;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = FrameworkConstants.MAX_RETRY_COUNT;
    
    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess()) {
            if (retryCount < MAX_RETRY_COUNT) {
                retryCount++;
                LoggerUtil.info("Retrying test: " + result.getMethod().getMethodName() + 
                    " - Attempt: " + retryCount);
                return true;
            }
        }
        return false;
    }
}
