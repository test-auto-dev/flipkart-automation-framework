package com.flipkart.automation.pages;

import com.flipkart.automation.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.flipkart.automation.utils.WaitUtil;
import java.util.List;

public class SearchResultsPage extends BasePage {
    
    @FindBy(xpath = "//div[contains(@class,'_1AtVbE') or contains(@class,'product')]")
    private List<WebElement> productList;
    
    @FindBy(xpath = "//span[contains(text(),'results for')]")
    private WebElement searchResultsText;
    
    @FindBy(xpath = "//div[@class='_2kHMtA' or contains(@class,'product-title')]")
    private List<WebElement> productTitles;
    
    public SearchResultsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
    
    public boolean areSearchResultsDisplayed() {
        return WaitUtil.waitForElementToBeVisible(searchResultsText, 15);
    }
    
    public int getProductCount() {
        WaitUtil.waitForElementsToBeVisible(productList, 15);
        return productList.size();
    }
    
    public String getFirstProductTitle() {
        if (productTitles.size() > 0) {
            return productTitles.get(0).getText();
        }
        return "";
    }
}
