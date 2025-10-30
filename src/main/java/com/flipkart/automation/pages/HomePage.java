package com.flipkart.automation.pages;

import com.flipkart.automation.base.BasePage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.flipkart.automation.utils.WaitUtil;

public class HomePage extends BasePage {
    
    @FindBy(xpath = "//input[@name='q' or @placeholder='Search for Products, Brands and More']")
    private WebElement searchBox;
    
    @FindBy(xpath = "//button[@type='submit' or contains(@class,'search')]")
    private WebElement searchButton;
    
    @FindBy(xpath = "//a[contains(text(),'Login')]")
    private WebElement loginLink;
    
    @FindBy(xpath = "//button[contains(@class,'close') or text()='✕']")
    private WebElement closePopup;
    
    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
    
    public void closeLoginPopupIfPresent() {
        try {
            if (WaitUtil.waitForElementToBeVisible(closePopup, 5)) {
                closePopup.click();
            }
        } catch (Exception e) {
            // Popup not present, continue
        }
    }
    
    public SearchResultsPage searchProduct(String productName) {
        closeLoginPopupIfPresent();
        WaitUtil.waitForElementToBeClickable(searchBox);
        searchBox.clear();
        searchBox.sendKeys(productName);
        searchBox.sendKeys(Keys.ENTER);
        return new SearchResultsPage(driver);
    }
    
    public boolean isHomePageDisplayed() {
        return WaitUtil.waitForElementToBeVisible(searchBox, 10);
    }
}
