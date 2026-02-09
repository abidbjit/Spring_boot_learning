package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private boolean success;
    private String message;
    private int code;
    private Object resource;
    private ApiCollection<?> collection;
    private String error;

    // Constructors
    public ApiResponse() {
    }

    private ApiResponse(boolean success, String message, int code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    // Static factory methods for success responses with resource
    public static ApiResponse successResource(String message, Object resource) {
        ApiResponse response = new ApiResponse(true, message, 200);
        response.resource = resource;
        return response;
    }

    public static ApiResponse createdResource(String message, Object resource) {
        ApiResponse response = new ApiResponse(true, message, 201);
        response.resource = resource;
        return response;
    }

    // Static factory methods for success responses with collection
    public static ApiResponse successCollection(String message, ApiCollection<?> collection) {
        ApiResponse response = new ApiResponse(true, message, 200);
        response.collection = collection;
        return response;
    }

    public static ApiResponse successCollection(String message, Object collection) {
        ApiResponse response = new ApiResponse(true, message, 200);
        response.resource = collection;
        return response;
    }

    public static ApiResponse noContent(String message) {
        return new ApiResponse(true, message, 204);
    }

    // Static factory methods for error responses
    public static ApiResponse error(String message, int code, String error) {
        ApiResponse response = new ApiResponse(false, message, code);
        response.error = error;
        return response;
    }

    public static ApiResponse badRequest(String message, String error) {
        ApiResponse response = new ApiResponse(false, message, 400);
        response.error = error;
        return response;
    }

    public static ApiResponse notFound(String message) {
        ApiResponse response = new ApiResponse(false, message, 404);
        response.error = "Resource not found";
        return response;
    }

    public static ApiResponse internalError(String message) {
        ApiResponse response = new ApiResponse(false, message, 500);
        response.error = "Internal server error";
        return response;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getResource() {
        return resource;
    }

    public void setResource(Object resource) {
        this.resource = resource;
    }

    public ApiCollection<?> getCollection() {
        return collection;
    }

    public void setCollection(ApiCollection<?> collection) {
        this.collection = collection;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
