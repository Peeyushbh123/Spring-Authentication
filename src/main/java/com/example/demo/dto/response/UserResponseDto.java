package com.example.demo.dto.response;

import com.example.demo.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponseDto {

    @JsonProperty("id")
    private int id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email_id")
    private String email;

    @JsonProperty("is_logged")
    private int isLogged;

    public UserResponseDto() {

    }

    public UserResponseDto(int id, String firstName, String lastName, String email, int isLogged) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isLogged = isLogged;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsLogged() {
        return isLogged;
    }

    public void setIsLogged(int isLogged) {
        this.isLogged = isLogged;
    }

    public void convertToUserResponse(User user){
        setEmail(user.getEmail());
        setFirstName(user.getFirstname());
        setLastName(user.getLastname());
        setId(user.getId());
        setIsLogged(user.getIs_login());
    }
}
