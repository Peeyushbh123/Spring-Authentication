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

    @Override
    public Optional<User> getUserById(int id){
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public User addUser(User user){
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(int id){
        userRepository.deleteById(id);
    }
}
