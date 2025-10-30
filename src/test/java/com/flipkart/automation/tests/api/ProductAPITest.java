package com.flipkart.automation.tests.api;

import com.flipkart.automation.api.client.RestClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class ProductAPITest {
    
    private RestClient restClient;
    
    @BeforeClass
    public void setup() {
        // For demo purposes - actual Flipkart API requires authentication
        restClient = new RestClient("https://fakestoreapi.com");
    }
    
    @Test(priority = 1, groups = {"api", "smoke"}, 
          description = "Verify GET all products API")
    public void testGetAllProducts() {
        Response response = restClient.get("/products");
        
        Assert.assertEquals(response.getStatusCode(), 200, 
            "Status code should be 200");
        
        response.then()
            .body("size()", greaterThan(0))
            .body("[0].id", notNullValue())
            .body("[0].title", notNullValue())
            .body("[0].price", notNullValue());
    }
    
    @Test(priority = 2, groups = {"api", "regression"}, 
          description = "Verify GET single product API")
    public void testGetSingleProduct() {
        Response response = restClient.get("/products/1");
        
        Assert.assertEquals(response.getStatusCode(), 200, 
            "Status code should be 200");
        
        response.then()
            .body("id", equalTo(1))
            .body("title", notNullValue())
            .body("price", greaterThan(0f))
            .body("category", notNullValue());
    }
    
    @Test(priority = 3, groups = {"api", "regression"}, 
          description = "Verify product search with category filter")
    public void testGetProductsByCategory() {
        Response response = restClient.get("/products/category/electronics");
        
        Assert.assertEquals(response.getStatusCode(), 200, 
            "Status code should be 200");
        
        response.then()
            .body("size()", greaterThan(0))
            .body("[0].category", equalTo("electronics"));
    }
}
