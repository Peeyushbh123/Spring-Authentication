package com.example.demo.dto.response;

public class APIResponse<T> {
    private T data;
    private String message;

    public APIResponse() {
        data=null;
        message=null;
    }
    public APIResponse(T data, String message) {
        super();
        this.data = data;
        this.message = message;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
