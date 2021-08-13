package com.example.demo.controllers;

import com.example.demo.custom.exception.EntityNotFoundException;
import com.example.demo.custom.exception.ServiceLayerException;
import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.UpdateRequestDto;
import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.APIResponse;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.enums.MessageEnum;
import com.example.demo.services.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;

	// Description-- Getting all the Users
	@GetMapping("/users")
	public ResponseEntity<?> getUsers() throws EntityNotFoundException {
		APIResponse<List<UserResponseDto> > res=new APIResponse<>();
		List<UserResponseDto> list=userService.getAllUsers();
		res.setData(list);
		res.setMessage(MessageEnum.FOUND_ALL_USERS.toString());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// Description--  Login request with email and password
	@PutMapping("/login")
	public ResponseEntity<?> signIn(@RequestBody LoginRequestDto loginRequest) throws EntityNotFoundException,ServiceLayerException{
		APIResponse<UserResponseDto> res=new APIResponse<>();
		UserResponseDto response=userService.loginUser(loginRequest);
		res.setData(response);
		res.setMessage(MessageEnum.LOGGED_IN.toString());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	// Description-- Logout request with userId and password
	@PutMapping("/logout/{accId}")
	public ResponseEntity<?> signOut(
			@PathVariable("accId") int accId,@RequestBody Map<String,String> payLoad
	) throws EntityNotFoundException,ServiceLayerException {
		APIResponse<UserResponseDto> res=new APIResponse<>();
		UserResponseDto response=userService.logoutUser(accId,payLoad.get("password"));
		res.setData(response);
		res.setMessage(MessageEnum.LOGGED_OUT.toString());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	// Description--  delete account with given id and password
	@DeleteMapping("/deleteaccount/{id}")
	public ResponseEntity<?> deleteAccount(
			@RequestBody Map<String,String> payLoad,@PathVariable("id") int id
	) throws EntityNotFoundException,ServiceLayerException {
		APIResponse<UserResponseDto> res=new APIResponse<>();
		UserResponseDto response=userService.deleteUser(id,payLoad.get("password"));
		res.setData(response);
		res.setMessage(MessageEnum.SUCCESS_DELETE.toString());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	// Description-- create account for the given details
	@PostMapping("/createaccount")
	public ResponseEntity<?> createAccount(
			@Valid @RequestBody UserRequestDto userCreateRequest
	) throws ServiceLayerException{
		APIResponse<UserResponseDto> res=new APIResponse<>();
		UserResponseDto savedUser=userService.addUser(userCreateRequest);
		res.setData(savedUser);
		res.setMessage(MessageEnum.SUCCESS_CREATE_ACCOUNT.toString());
		return new ResponseEntity<>(res,HttpStatus.CREATED);
	}

	// Description-- update account for the user with given id and password
	@PutMapping("/updateaccount/{id}")
	public ResponseEntity<?> updateAccount(
			@RequestBody UpdateRequestDto userUpdateRequest, @PathVariable("id") int id
	) throws EntityNotFoundException, ServiceLayerException {
		APIResponse<UserResponseDto> res=new APIResponse<>();
		UserResponseDto response=userService.updateUser(userUpdateRequest,id);
		res.setData(response);
		res.setMessage(MessageEnum.SUCCESS_UPDATED.toString());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
}
