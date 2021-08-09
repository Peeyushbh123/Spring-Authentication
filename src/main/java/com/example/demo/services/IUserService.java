package com.example.demo.services;

import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.UpdateRequestDto;
import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entities.User;

import java.util.List;
import java.util.Map;

public interface IUserService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto addUser(UserRequestDto user);

    UserResponseDto deleteUser(int id);

    UserResponseDto loginUser(LoginRequestDto credentials);

    UserResponseDto logoutUser(int userId);

    UserResponseDto updateUser(UpdateRequestDto updateDetails, int userId);

}
