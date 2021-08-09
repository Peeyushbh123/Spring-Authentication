package com.example.demo.service;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.custom.exception.NotFoundException;
import com.example.demo.dao.IUserDao;
import com.example.demo.entities.User;
import com.example.demo.repository.IUserRepository;
import com.example.demo.services.IUserService;
import com.example.demo.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

	@Mock
	private IUserDao iUserDao;

	@InjectMocks
	private UserServiceImpl iUserService;

	List<User> userList=new ArrayList<>();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		User user1=new User(1,"peeyush@gmail.com","lambda@123","Peeyush","Bharadwaj",0);
		User user2=new User(2,"sudhanshu@gmail.com","paytm@123","Sudhanshu","Bharadwaj",0);
		User user3=new User(3,"rohit@gmail.com","one97@123","Rohit","Agarwal",0);
		userList.add(user1);
		userList.add(user2);
		userList.add(user3);
	}

//	@Test
//	void getAllUsers() throws NotFoundException {
//        when(iUserService.getAllUsers()).thenReturn(userList);
//        List<User> users = iUserService.getAllUsers();
//        assertEquals(3,userList.size());
//	}

//	@Test
//	void createUser() {
//	    User testUser=new User(12,"peeyu@gmail.com","Paytm@123","Peeyush","Bharadwaj",0);
//	    when(iUserService.addUser(testUser)).thenReturn(testUser);
//	    User actualUser=iUserService.addUser(testUser);
//	    assertEquals(12,actualUser.getId());
//	}
//
//	@Test
//	void deleteUser() {
//
//	}
//
//	@Test
//	void loginUser() {
//
//	}
//
//	@Test
//	void logoutUser() {
//
//	}

}
