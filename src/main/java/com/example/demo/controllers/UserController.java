package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.UpdateRequestDto;
import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.enums.MessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entities.User;
import com.example.demo.services.UserServiceImpl;
import com.example.demo.dto.response.APIResponse;
import javax.validation.Valid;

@RestController
public class UserController {
	
	@Autowired
	private UserServiceImpl userserv;
	
	@GetMapping("/users")
	public ResponseEntity<?> getUsers() {
		APIResponse<List<UserResponseDto> > res=new APIResponse<>();
		List<UserResponseDto> list=userserv.getAllUsers();
		res.setData(list);
		res.setMessage(MessageEnum.FOUND_ALL_USERS.toString());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@PutMapping("/login")
	public ResponseEntity<?> signIn(@RequestBody LoginRequestDto loginRequest) {
		APIResponse<UserResponseDto> res=new APIResponse<>();
		UserResponseDto response=userserv.loginUser(loginRequest);
		res.setData(response);
		res.setMessage(MessageEnum.LOGGED_IN.toString());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	@PutMapping("/logout/{accId}")
	public ResponseEntity<?> signUp(@PathVariable("accId") int accId) {
		APIResponse<UserResponseDto> res=new APIResponse<>();
		UserResponseDto response=userserv.logoutUser(accId);
		res.setData(response);
		res.setMessage(MessageEnum.LOGGED_OUT.toString());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteaccount/{id}")
	public ResponseEntity<?> deleteAccount(@PathVariable("id") int id) {
		APIResponse<UserResponseDto> res=new APIResponse<>();
		UserResponseDto response=userserv.deleteUser(id);
		res.setData(response);
		res.setMessage(MessageEnum.SUCCESS_DELETE.toString());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	@PostMapping("/createaccount")
	public ResponseEntity<?> createAccount(@Valid @RequestBody UserRequestDto userCreateRequest){
		APIResponse<UserResponseDto> res=new APIResponse<>();
		UserResponseDto savedUser=userserv.addUser(userCreateRequest);
		res.setData(savedUser);
		res.setMessage(MessageEnum.SUCCESS_CREATE_ACCOUNT.toString());
		return new ResponseEntity<>(res,HttpStatus.CREATED);
	}
	
	@PutMapping("/updateaccount/{id}")
	public ResponseEntity<?> updateAccount(@RequestBody UpdateRequestDto userUpdateRequest, @PathVariable("id") int id){
		APIResponse<UserResponseDto> res=new APIResponse<>();
		UserResponseDto response=userserv.updateUser(userUpdateRequest,id);
		res.setData(response);
		res.setMessage(MessageEnum.SUCCESS_UPDATED.toString());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
}
