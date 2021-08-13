package com.example.demo.dao;

import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entities.User;
import com.example.demo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDaoImpl implements IUserDao {

    @Autowired
    private IUserRepository userRepository;

    // Description-- Get a user with id
    @Override
    public Optional<User> getUserById(int id){
        return userRepository.findById(id);
    }

    // Description-- Get all users
    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    // Description-- Adding a user
    @Override
    public User addUser(User user){
        return userRepository.save(user);
    }

    // Description-- Delete a user with id
    @Override
    public void deleteUserById(int id){
        userRepository.deleteById(id);
    }
}
