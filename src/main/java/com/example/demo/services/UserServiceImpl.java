package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.example.demo.custom.exception.EntityNotFoundException;
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

	// Description-- get All Users
	@Override
	public List<UserResponseDto> getAllUsers() throws EntityNotFoundException {
		LOGGER.info("Getting Users...");
		List<UserResponseDto> userResponseList=new ArrayList<>();
		List<User> userList=iUserDao.getAllUsers();
		if(CollectionUtils.isEmpty(userList)) {
			LOGGER.warn("No Users!");
			throw new EntityNotFoundException(MessageEnum.NO_USERS.toString());
		}
		for(User user:userList){
			UserResponseDto userResponseDto=new UserResponseDto();
			userResponseDto.convertToUserResponse(user);
			userResponseList.add(userResponseDto);
		}
		LOGGER.info("Successfully fetch all users!");
		return userResponseList;
	}

	// Description-- adding a user
	@Transactional
	@Override
	public UserResponseDto addUser(UserRequestDto u) throws ServiceLayerException{
		LOGGER.info("adding User...");
		if(!userServiceValidators.is_Valid_Password(u.getPassword())) {
			LOGGER.error("Password is not valid!");
			throw new ServiceLayerException(MessageEnum.PASSWORD_STRENGTH_WEAK.toString());
		}
		if(userRepository.findByEmail(u.getEmailId()).isPresent()){
			LOGGER.warn("Email Already Exists!");
			throw new ServiceLayerException(MessageEnum.EMAIL_ALREADY_USED.toString());
		}

		String encodedPassword=bCryptPasswordEncoder.encode(u.getPassword());
		User user=new User();
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

	// Description-- delete a user with given id and password
	@Transactional
	@Override
	public UserResponseDto deleteUser(int id,String password) throws EntityNotFoundException,ServiceLayerException{
		LOGGER.info("Deleting user..");
		Optional<User> deleteUser=iUserDao.getUserById(id);
		if(!deleteUser.isPresent()) {
			LOGGER.warn("user not found");
			throw new EntityNotFoundException(MessageEnum.DATA_NOT_FOUND.toString());
		}
		if(!bCryptPasswordEncoder.matches(password,deleteUser.get().getPassword())){
			LOGGER.error("Not authorised");
			throw new ServiceLayerException(MessageEnum.NOT_AUTHORIZED.toString());
		}
		if(deleteUser.get().getIs_login()==0){
			LOGGER.error("Not logged in");
			throw new ServiceLayerException(MessageEnum.NOT_LOGGED_IN.toString());
		}
		LOGGER.info("Successfully Deleted!");
		iUserDao.deleteUserById(id);
		UserResponseDto userResponse=new UserResponseDto();
		userResponse.convertToUserResponse(deleteUser.get());
		return userResponse;
	}

	// Description-- logging in with given credentials
	@Override
	public UserResponseDto loginUser(LoginRequestDto credentials) throws EntityNotFoundException,ServiceLayerException{
		LOGGER.info("Logging user..");
		Optional<User> findUser=userRepository.findByEmail(credentials.getEmailId());
		if(!findUser.isPresent()) {
			LOGGER.warn("user not found");
			throw new EntityNotFoundException(MessageEnum.DATA_NOT_FOUND.toString());
		}
		User user = findUser.get();
		if(user.getIs_login()==1){
			LOGGER.warn("Already logged in");
			throw new ServiceLayerException(MessageEnum.ALREADY_LOGGED_IN.toString());
		}
		String givenPassword=credentials.getPassword();
		String correctPassword= user.getPassword();
		if(!bCryptPasswordEncoder.matches(givenPassword, correctPassword)) {
			LOGGER.info("Password not match");
			throw new ServiceLayerException(MessageEnum.PASSWORD_NOT_MATCH.toString());
		}
		LOGGER.info("Successfully logged in!");
		user.setIs_login(1);
		iUserDao.addUser(user);
		UserResponseDto userResponse=new UserResponseDto();
		userResponse.convertToUserResponse(user);
		return userResponse;
	}

	// Description-- logging out with given id and password
	@Override
	public UserResponseDto logoutUser(int userId,String password) throws EntityNotFoundException,ServiceLayerException{
		LOGGER.info("Logging out user..");
		Optional<User> findUser=iUserDao.getUserById(userId);
		if(!findUser.isPresent()) {
			LOGGER.warn("user is null");
			throw new EntityNotFoundException(MessageEnum.DATA_NOT_FOUND.toString());
		}
		User user = findUser.get();
		if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
			LOGGER.error("Not authorised");
			throw new ServiceLayerException(MessageEnum.NOT_AUTHORIZED.toString());
		}
		if(user.getIs_login()==0){
			LOGGER.error("user not logged in");
			throw new ServiceLayerException(MessageEnum.NOT_LOGGED_IN.toString());
		}
		LOGGER.info("Successfully logged out!");
		user.setIs_login(0);
		User savedUser=iUserDao.addUser(user);
		UserResponseDto userResponse=new UserResponseDto();
		userResponse.convertToUserResponse(savedUser);
		return userResponse;
	}

	// Description-- updating details of user with given password and userId
	@Transactional
	@Override
	public UserResponseDto updateUser(UpdateRequestDto updateDetails, int userId) throws EntityNotFoundException,ServiceLayerException {
		LOGGER.info("Updating user..");
		Optional<User> findUser=iUserDao.getUserById(userId);
		if(!findUser.isPresent()) {
			LOGGER.warn("user not found");
			throw new EntityNotFoundException(MessageEnum.DATA_NOT_FOUND.toString());
		}
		User user = findUser.get();
		if(!bCryptPasswordEncoder.matches(updateDetails.getOldPassword(), user.getPassword())){
			LOGGER.error("Not Authorised");
			throw new ServiceLayerException(MessageEnum.NOT_AUTHORIZED.toString());
		}
		if(user.getIs_login()==0){
			LOGGER.error("user not logged in!");
			throw new ServiceLayerException(MessageEnum.NOT_LOGGED_IN.toString());
		}
		user.setFirstname(updateDetails.getFirstName());
		user.setLastname(updateDetails.getLastName());
		String encodedPassword=bCryptPasswordEncoder.encode(updateDetails.getNewPassword());
		user.setPassword(encodedPassword);
		LOGGER.info("Successfully updated!");
		iUserDao.addUser(user);
		UserResponseDto userResponse=new UserResponseDto();
		userResponse.convertToUserResponse(user);
		return userResponse;
	}
	
}
