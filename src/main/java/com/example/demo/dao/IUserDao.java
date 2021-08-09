package com.example.demo.dao;

import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserDao {
    Optional<User> getUserById(int id);

    List<User> getAllUsers();

    User addUser(User u);

    void deleteUserById(int id);
}
