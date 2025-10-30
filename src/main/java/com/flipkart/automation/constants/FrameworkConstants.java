package com.flipkart.automation.constants;

public final class FrameworkConstants {
    
    private FrameworkConstants() {}
    
    // Timeouts
    public static final int EXPLICIT_WAIT_TIMEOUT = 20;
    public static final int IMPLICIT_WAIT_TIMEOUT = 10;
    public static final int PAGE_LOAD_TIMEOUT = 30;
    
    // Paths
    public static final String CONFIG_FILE_PATH = System.getProperty("user.dir") + 
        "/src/test/resources/config/config.properties";
    public static final String EXTENT_CONFIG_PATH = System.getProperty("user.dir") + 
        "/src/test/resources/config/extent-config.xml";
    public static final String SCREENSHOT_PATH = System.getProperty("user.dir") + 
        "/test-output/screenshots/";
    public static final String EXTENT_REPORT_PATH = System.getProperty("user.dir") + 
        "/test-output/extent-reports/";
    
    // Retry
    public static final int MAX_RETRY_COUNT = 2;
    
    // API
    public static final String BASE_URI = "https://www.flipkart.com/api";
    public static final int API_TIMEOUT = 30000;
}
