package com.flipkart.automation.tests.ui;

import com.flipkart.automation.base.BaseTest;
import com.flipkart.automation.listeners.RetryAnalyzer;
import com.flipkart.automation.pages.HomePage;
import com.flipkart.automation.pages.SearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchTest extends BaseTest {
    
    @Test(priority = 1, groups = {"smoke", "regression"}, 
          description = "Verify search functionality with valid product",
          retryAnalyzer = RetryAnalyzer.class)
    public void testSearchWithValidProduct() {
        HomePage homePage = new HomePage(driver);
        
        Assert.assertTrue(homePage.isHomePageDisplayed(), 
            "Home page should be displayed");
        
        SearchResultsPage searchResults = homePage.searchProduct("iPhone 15");
        
        Assert.assertTrue(searchResults.areSearchResultsDisplayed(), 
            "Search results should be displayed");
        
        Assert.assertTrue(searchResults.getProductCount() > 0, 
            "Product count should be greater than 0");
        
        String firstProduct = searchResults.getFirstProductTitle();
        Assert.assertTrue(firstProduct.toLowerCase().contains("iphone"), 
            "First product should contain 'iphone'");
    }
    
    @Test(priority = 2, groups = {"regression"}, 
          description = "Verify search with multiple products",
          retryAnalyzer = RetryAnalyzer.class)
    public void testSearchWithMultipleKeywords() {
        HomePage homePage = new HomePage(driver);
        SearchResultsPage searchResults = homePage.searchProduct("Samsung Galaxy");
        
        Assert.assertTrue(searchResults.areSearchResultsDisplayed(), 
            "Search results should be displayed");
        Assert.assertTrue(searchResults.getProductCount() > 5, 
            "Should have multiple products");
    }
}
