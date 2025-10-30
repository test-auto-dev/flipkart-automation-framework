package com.flipkart.automation.base;

import org.openqa.selenium.WebDriver;

public class BasePage {
    
    protected WebDriver driver;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
    }
    
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
