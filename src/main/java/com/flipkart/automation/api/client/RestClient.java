package com.flipkart.automation.api.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.flipkart.automation.constants.FrameworkConstants;
import com.flipkart.automation.utils.LoggerUtil;
import java.util.Map;

public class RestClient {
    
    private RequestSpecification requestSpec;
    
    public RestClient() {
        RestAssured.baseURI = FrameworkConstants.BASE_URI;
    }
    
    public RestClient(String baseUri) {
        RestAssured.baseURI = baseUri;
    }
    
    private RequestSpecification getRequestSpecification() {
        requestSpec = RestAssured.given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON);
        return requestSpec;
    }
    
    public Response get(String endpoint) {
        LoggerUtil.info("GET Request: " + endpoint);
        Response response = getRequestSpecification().get(endpoint);
        LoggerUtil.info("Response Status: " + response.getStatusCode());
        return response;
    }
    
    public Response get(String endpoint, Map<String, String> queryParams) {
        LoggerUtil.info("GET Request with params: " + endpoint);
        Response response = getRequestSpecification()
            .queryParams(queryParams)
            .get(endpoint);
        LoggerUtil.info("Response Status: " + response.getStatusCode());
        return response;
    }
    
    public Response post(String endpoint, Object body) {
        LoggerUtil.info("POST Request: " + endpoint);
        Response response = getRequestSpecification()
            .body(body)
            .post(endpoint);
        LoggerUtil.info("Response Status: " + response.getStatusCode());
        return response;
    }
    
    public Response post(String endpoint, Map<String, String> headers, Object body) {
        LoggerUtil.info("POST Request with headers: " + endpoint);
        Response response = getRequestSpecification()
            .headers(headers)
            .body(body)
            .post(endpoint);
        LoggerUtil.info("Response Status: " + response.getStatusCode());
        return response;
    }
    
    public Response put(String endpoint, Object body) {
        LoggerUtil.info("PUT Request: " + endpoint);
        Response response = getRequestSpecification()
            .body(body)
            .put(endpoint);
        LoggerUtil.info("Response Status: " + response.getStatusCode());
        return response;
    }
    
    public Response delete(String endpoint) {
        LoggerUtil.info("DELETE Request: " + endpoint);
        Response response = getRequestSpecification().delete(endpoint);
        LoggerUtil.info("Response Status: " + response.getStatusCode());
        return response;
    }
}
