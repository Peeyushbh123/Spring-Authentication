package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateRequestDto {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("new_password")
    private String newPassword;

    @JsonProperty("old_password")
    private String oldPassword;

    public UpdateRequestDto() {
    }

    public UpdateRequestDto(String firstName, String lastName, String oldpassword,String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.newPassword = newPassword;
        this.oldPassword=oldpassword;
    }

    public String getFirstName() {

        return firstName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getLastName() {

        return lastName;
    }
}
