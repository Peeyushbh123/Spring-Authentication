package com.example.demo.services;

import com.example.demo.custom.exception.EntityNotFoundException;
import com.example.demo.custom.exception.ServiceLayerException;
import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.UpdateRequestDto;
import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.UserResponseDto;

import java.util.List;

public interface IUserService {

    List<UserResponseDto> getAllUsers() throws EntityNotFoundException;

    UserResponseDto addUser(UserRequestDto user) throws ServiceLayerException;

    UserResponseDto deleteUser(int id,String password) throws EntityNotFoundException,ServiceLayerException;

    UserResponseDto loginUser(LoginRequestDto credentials) throws EntityNotFoundException,ServiceLayerException;

    UserResponseDto logoutUser(int userId,String password) throws EntityNotFoundException,ServiceLayerException;

    UserResponseDto updateUser(UpdateRequestDto updateDetails, int userId) throws EntityNotFoundException,ServiceLayerException;

}
