package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserRequestDto {
    @NotEmpty
    @Size(min=2)
    @JsonProperty("first_name")
    private String firstName;

    @NotEmpty
    @Size(min=2)
    @JsonProperty("last_name")
    private String lastName;

    @NotEmpty
    @Email
    @JsonProperty("email_id")
    private String emailId;

    @NotEmpty
    @Size(min = 6,max = 60)
    @JsonProperty("password")
    private String password;

    public UserRequestDto() {

    }

    public UserRequestDto(String firstName, String lastName, String emailId, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
