package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequestDto {

    @JsonProperty("email_id")
    private String emailId;

    @JsonProperty("password")
    private String password;

    public LoginRequestDto() {

    }

    public LoginRequestDto(String emailId, String password) {
        this.emailId = emailId;
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
