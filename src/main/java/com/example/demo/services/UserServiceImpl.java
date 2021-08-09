package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.example.demo.custom.exception.NotFoundException;
import com.example.demo.custom.exception.ServiceLayerException;
import com.example.demo.dao.IUserDao;
import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.UpdateRequestDto;
import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.enums.MessageEnum;
import com.example.demo.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.example.demo.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private IUserDao iUserDao;
	
	@Autowired
	private UserServiceValidators userServiceValidators;

	@Autowired
	private IUserRepository userRepository;

	@Override
	public List<UserResponseDto> getAllUsers(){
		LOGGER.info("Getting Users...");
		List<UserResponseDto> userResponseList=new ArrayList<>();
		List<User> userList=iUserDao.getAllUsers();
		if(CollectionUtils.isEmpty(userList)) {
			LOGGER.warn("No Users!");
			throw new NotFoundException(MessageEnum.NO_USERS.toString());
		}
		for(User user:userList){
			UserResponseDto userResponseDto=new UserResponseDto();
			userResponseDto.convertToUserResponse(user);
			userResponseList.add(userResponseDto);
		}
		LOGGER.info("Successfully fetch all users!");
		return userResponseList;
	}

	@Transactional
	@Override
	public UserResponseDto addUser(UserRequestDto u) {
		LOGGER.info("adding User...");
		if(u==null) {
			LOGGER.error("user not found");
			throw new NotFoundException(MessageEnum.GIVEN_DETAILS_ARE_ILLEGAL.toString());
		}
		if(!userServiceValidators.is_Valid_Password(u.getPassword())) {
			LOGGER.error("Password is not valid!");
			throw new ServiceLayerException(MessageEnum.PASSWORD_STRENGTH_WEAK.toString());
		}
		if(userRepository.findByEmail(u.getEmailId()).isPresent()){
			LOGGER.warn("Email Already Exists!");
			throw new ServiceLayerException(MessageEnum.EMAIL_ALREADY_USED.toString());
		}
		User user=new User();
		String encodedPassword=bCryptPasswordEncoder.encode(u.getPassword());
		user.setPassword(encodedPassword);
		user.setIs_login(0);
		user.setFirstname(u.getFirstName());
		user.setLastname(u.getLastName());
		user.setEmail(u.getEmailId());
		User addedUser=iUserDao.addUser(user);
		UserResponseDto userResponse=new UserResponseDto();
		userResponse.convertToUserResponse(addedUser);
		LOGGER.info("Successfully added!");
		return userResponse;
	}

	@Transactional
	@Override
	public UserResponseDto deleteUser(int id) {
		LOGGER.info("Deleting user..");
		Optional<User> deleteUser=iUserDao.getUserById(id);
		if(!deleteUser.isPresent()) {
			LOGGER.warn("user not found");
			throw new NotFoundException(MessageEnum.DATA_NOT_FOUND.toString());
		}
		LOGGER.info("Successfully Deleted!");
		iUserDao.deleteUserById(id);
		UserResponseDto userResponse=new UserResponseDto();
		userResponse.convertToUserResponse(deleteUser.get());
		return userResponse;
	}

	@Override
	public UserResponseDto loginUser(LoginRequestDto credentials) {
		LOGGER.info("Logging user..");
		Optional<User> findUser=userRepository.findByEmail(credentials.getEmailId());
		if(!findUser.isPresent()) {
			LOGGER.warn("user not found");
			throw new NotFoundException(MessageEnum.DATA_NOT_FOUND.toString());
		}
		String givenPassword=credentials.getPassword();
		String correctPassword=findUser.get().getPassword();
		if(!bCryptPasswordEncoder.matches(givenPassword, correctPassword)) {
			LOGGER.info("Password not match");
			throw new ServiceLayerException(MessageEnum.PASSWORD_NOT_MATCH.toString());
		}
		LOGGER.info("Successfully logged in!");
		findUser.get().setIs_login(1);
		iUserDao.addUser(findUser.get());
		UserResponseDto userResponse=new UserResponseDto();
		userResponse.convertToUserResponse(findUser.get());
		return userResponse;
	}

	@Override
	public UserResponseDto logoutUser(int userId) {
		LOGGER.info("Logging out user..");
		Optional<User> findUser=iUserDao.getUserById(userId);
		if(!findUser.isPresent()) {
			LOGGER.warn("user is null");
			throw new NotFoundException(MessageEnum.DATA_NOT_FOUND.toString());
		}
		LOGGER.info("Successfully logged out!");
		findUser.get().setIs_login(0);
		iUserDao.addUser(findUser.get());
		UserResponseDto userResponse=new UserResponseDto();
		userResponse.convertToUserResponse(findUser.get());
		return userResponse;
	}

	@Transactional
	@Override
	public UserResponseDto updateUser(UpdateRequestDto updateDetails, int userId) {
		LOGGER.info("Updating user..");
		Optional<User> findUser=iUserDao.getUserById(userId);
		if(!findUser.isPresent()) {
			LOGGER.warn("user not found");
			throw new NotFoundException(MessageEnum.DATA_NOT_FOUND.toString());
		}
		if(findUser.get().getIs_login()==0){
			LOGGER.error("user not logged in!");
			throw new ServiceLayerException(MessageEnum.NOT_LOGGED_IN.toString());
		}
		findUser.get().setFirstname(updateDetails.getFirstName());
		findUser.get().setLastname(updateDetails.getLastName());
		String encodedPassword=bCryptPasswordEncoder.encode(updateDetails.getPassword());
		findUser.get().setPassword(encodedPassword);
		LOGGER.info("Successfully updated!");
		iUserDao.addUser(findUser.get());
		UserResponseDto userResponse=new UserResponseDto();
		userResponse.convertToUserResponse(findUser.get());
		return userResponse;
	}
	
}
