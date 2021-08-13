package com.example.demo.service;

import com.example.demo.custom.exception.EntityNotFoundException;
import com.example.demo.custom.exception.ServiceLayerException;
import com.example.demo.dao.IUserDao;
import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.UpdateRequestDto;
import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entities.User;
import com.example.demo.enums.MessageEnum;
import com.example.demo.repository.IUserRepository;
import com.example.demo.services.UserServiceImpl;
import com.example.demo.services.UserServiceValidators;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {
	@Mock
	private IUserDao iUserDao;

	@Mock
	private IUserRepository iUserRepository;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	private UserServiceValidators userServiceValidators;

	@InjectMocks
	private UserServiceImpl iUserService;

	List<User> userList=new ArrayList<>();
	private Validator validator;


	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		User user1=new User(1,"peeyush@gmail.com","peeyush@123","Peeyush","Bharadwaj",0);
		User user2=new User(2,"sudhanshu@gmail.com","peeyush@1234","Sudhanshu","Bharadwaj",0);
		User user3=new User(3,"rohit@gmail.com","peeyush@122","Rohit","Agarwal",0);
		userList.add(user1);
		userList.add(user2);
		userList.add(user3);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	//----------------------------------------Test for getAllUsers()-----------------------------------------
	@DisplayName("case when there is no user")
	@Test(expected = EntityNotFoundException.class)
	public void getAllUsers_case_1() throws EntityNotFoundException {
		when(iUserDao.getAllUsers()).thenReturn(null);
		List<UserResponseDto> users = iUserService.getAllUsers();
		assertEquals(0, users.size());
	}

	@DisplayName("case when there are users")
	@Test
	public void getAllUsers_case_2() throws EntityNotFoundException {
		when(iUserDao.getAllUsers()).thenReturn(userList);
		List<UserResponseDto> users = iUserService.getAllUsers();
		assertEquals(3, users.size());
	}


	//----------------------------------------Test for createUser()------------------------------------------
	@DisplayName("case when user is unique and can be added")
	@Test
	public void createUser_case_1() throws ServiceLayerException{
	    User testUser=new User(12,"peeyu@gmail.com","Paytm@123","Peeyush","Bharadwaj",0);
	    Optional<User> user=Optional.empty();

	    when(iUserDao.addUser(any(User.class))).thenReturn(testUser);
	    when(userServiceValidators.is_Valid_Password(anyString())).thenReturn(true);
	    when(iUserRepository.findByEmail(anyString())).thenReturn(user);
	    when(bCryptPasswordEncoder.encode(anyString())).thenReturn("xyz");

		UserRequestDto userRequestDto=new UserRequestDto();
		userRequestDto.setEmailId(testUser.getEmail());
		userRequestDto.setPassword(testUser.getPassword());
		userRequestDto.setFirstName(testUser.getFirstname());
		userRequestDto.setLastName(testUser.getLastname());

	    UserResponseDto actualUser=iUserService.addUser(userRequestDto);
	    assertEquals("peeyu@gmail.com",actualUser.getEmail());
	}

	@DisplayName("case when strength of password given by user is weak")
	@Test
	public void createUser_case_2() throws ServiceLayerException{
		User testUser=new User(12,"peeyu@gmail.com","paytm123","Peeyush","Bharadwaj",0);
		Optional<User> user=Optional.empty();

		when(iUserDao.addUser(any(User.class))).thenReturn(testUser);
		when(userServiceValidators.is_Valid_Password(anyString())).thenReturn(false);
		when(iUserRepository.findByEmail(anyString())).thenReturn(user);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn("xyz");

		UserRequestDto userRequestDto=new UserRequestDto();
		userRequestDto.setEmailId(testUser.getEmail());
		userRequestDto.setPassword(testUser.getPassword());
		userRequestDto.setFirstName(testUser.getFirstname());
		userRequestDto.setLastName(testUser.getLastname());

		ServiceLayerException e = assertThrows(ServiceLayerException.class, () -> iUserService.addUser(userRequestDto));
		assertEquals(MessageEnum.PASSWORD_STRENGTH_WEAK.toString(),e.getErrorMessage());
	}

	@DisplayName("case when user is not unique")
	@Test
	public void createUser_case_3() throws ServiceLayerException{
		User testUser=new User(12,"peeyu@gmail.com","Paytm@123","Peeyush","Bharadwaj",0);
		Optional<User> user=Optional.of(testUser);

		when(iUserDao.addUser(any(User.class))).thenReturn(testUser);
		when(userServiceValidators.is_Valid_Password(anyString())).thenReturn(true);
		when(iUserRepository.findByEmail(anyString())).thenReturn(user);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn("xyz");

		UserRequestDto userRequestDto=new UserRequestDto();
		userRequestDto.setEmailId(testUser.getEmail());
		userRequestDto.setPassword(testUser.getPassword());
		userRequestDto.setFirstName(testUser.getFirstname());
		userRequestDto.setLastName(testUser.getLastname());

		ServiceLayerException e = assertThrows(ServiceLayerException.class, () -> iUserService.addUser(userRequestDto));
		assertEquals(MessageEnum.EMAIL_ALREADY_USED.toString(),e.getErrorMessage());
	}


	//----------------------------------------Test for deleteUser()------------------------------------------

	@DisplayName("case when user does not exist")
	@Test
	public void deleteUser_case_1() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","Rohit@123","Peeyush","Bharadwaj",1);
		Optional<User> user=Optional.empty();
		when(iUserDao.getUserById(anyInt())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);
		EntityNotFoundException e=assertThrows(EntityNotFoundException.class,()->iUserService.deleteUser(testUser.getId(),testUser.getPassword()));
		assertEquals(MessageEnum.DATA_NOT_FOUND.toString(),e.getErrorMessage());
	}

	@DisplayName("case when user exist and password not match")
	@Test
	public void deleteUser_case_2() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","Rohit@123","Peeyush","Bharadwaj",1);
		Optional<User> user=Optional.of(testUser);
		when(iUserDao.getUserById(anyInt())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(false);
		ServiceLayerException e=assertThrows(ServiceLayerException.class,()->iUserService.deleteUser(testUser.getId(),testUser.getPassword()));
		assertEquals(MessageEnum.NOT_AUTHORIZED.toString(),e.getErrorMessage());
	}

	@DisplayName("case when user exist and password also matches")
	@Test
	public void deleteUser_case_3() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","Rohit@123","Peeyush","Bharadwaj",1);
		Optional<User> user=Optional.of(testUser);
		when(iUserDao.getUserById(anyInt())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);
		UserResponseDto actualUser=iUserService.deleteUser(testUser.getId(),testUser.getPassword());
		assertEquals(testUser.getEmail(),actualUser.getEmail());
	}


	//----------------------------------------Test for loginUser()------------------------------------------

	@DisplayName("case when user not exists")
	@Test
	public void loginUser_case_1() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","rohit@123","Peeyush","Bharadwaj",0);
		Optional<User> user=Optional.empty();
		when(iUserRepository.findByEmail(anyString())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);
		when(iUserDao.addUser(testUser)).thenReturn(testUser);
		LoginRequestDto loginRequestDto=new LoginRequestDto("peeyush123@gmail.com","rohit@123");
		EntityNotFoundException e=assertThrows(EntityNotFoundException.class,()->iUserService.loginUser(loginRequestDto));
		assertEquals(MessageEnum.DATA_NOT_FOUND.toString(),e.getErrorMessage());
	}

	@DisplayName("case when user is already logged in")
	@Test
	public void loginUser_case_2() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","rohit@123","Peeyush","Bharadwaj",1);
		Optional<User> user=Optional.of(testUser);
		when(iUserRepository.findByEmail(anyString())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);
		when(iUserDao.addUser(testUser)).thenReturn(testUser);
		LoginRequestDto loginRequestDto=new LoginRequestDto("peeyush123@gmail.com","rohit@123");
		ServiceLayerException e=assertThrows(ServiceLayerException.class,()->iUserService.loginUser(loginRequestDto));
		assertEquals(MessageEnum.ALREADY_LOGGED_IN.toString(),e.getErrorMessage());
	}

	@DisplayName("case when user exists and password not match")
	@Test
	public void loginUser_case_3() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","rohit@123","Peeyush","Bharadwaj",0);
		Optional<User> user=Optional.of(testUser);
		when(iUserRepository.findByEmail(anyString())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(false);
		when(iUserDao.addUser(testUser)).thenReturn(testUser);
		LoginRequestDto loginRequestDto=new LoginRequestDto("peeyush123@gmail.com","rohit@123");
		ServiceLayerException e=assertThrows(ServiceLayerException.class,()->iUserService.loginUser(loginRequestDto));
		assertEquals(MessageEnum.PASSWORD_NOT_MATCH.toString(),e.getErrorMessage());
	}

	@DisplayName("case when user exists and password matches")
	@Test
	public void loginUser_case_4() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","rohit@123","Peeyush","Bharadwaj",0);
		Optional<User> user=Optional.of(testUser);
		when(iUserRepository.findByEmail(anyString())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);
		when(iUserDao.addUser(testUser)).thenReturn(testUser);
		LoginRequestDto loginRequestDto=new LoginRequestDto("peeyush123@gmail.com","rohit@123");
		UserResponseDto actualUser=iUserService.loginUser(loginRequestDto);
		assertEquals(testUser.getEmail(),actualUser.getEmail());
	}


	//----------------------------------------Test for logoutUser()------------------------------------------

	@DisplayName("case when user does not exists")
	@Test
	public void logoutUser_case_1() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","rohit@123","Peeyush","Bharadwaj",1);
		Optional<User> user=Optional.empty();
		when(iUserDao.getUserById(anyInt())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);
		when(iUserDao.addUser(testUser)).thenReturn(testUser);
		EntityNotFoundException e=assertThrows(EntityNotFoundException.class,()->iUserService.logoutUser(1,"rohit@123"));
		assertEquals(MessageEnum.DATA_NOT_FOUND.toString(),e.getErrorMessage());
	}

	@DisplayName("case when user exists and password not match")
	@Test
	public void logoutUser_case_2() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","rohit@123","Peeyush","Bharadwaj",1);
		Optional<User> user=Optional.of(testUser);
		when(iUserDao.getUserById(anyInt())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(false);
		when(iUserDao.addUser(testUser)).thenReturn(testUser);
		ServiceLayerException e=assertThrows(ServiceLayerException.class,()->iUserService.logoutUser(testUser.getId(),testUser.getPassword()));
		assertEquals(MessageEnum.NOT_AUTHORIZED.toString(),e.getErrorMessage());
	}

	@DisplayName("case when user exists and password matches")
	@Test
	public void logoutUser_case_3() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","rohit@123","Peeyush","Bharadwaj",1);
		Optional<User> user=Optional.of(testUser);
		when(iUserDao.getUserById(anyInt())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);
		when(iUserDao.addUser(testUser)).thenReturn(testUser);
		UserResponseDto actualUser=iUserService.logoutUser(1,"rohit@123");
		assertEquals(testUser.getEmail(),actualUser.getEmail());
	}


	//----------------------------------------Test for updateUser()------------------------------------------

	@DisplayName("case when user not exists")
	@Test
	public void updateUser_case_1() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","rohit@123","Peeyush","Bharadwaj",1);
		Optional<User> user=Optional.empty();
		when(iUserDao.getUserById(anyInt())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);
		when(iUserDao.addUser(testUser)).thenReturn(testUser);

		UpdateRequestDto updateRequestDto=new UpdateRequestDto("Sudhanshu","Sharma","rohit@123","rohit@12345");
		EntityNotFoundException e=assertThrows(EntityNotFoundException.class,()->iUserService.updateUser(updateRequestDto,testUser.getId()));
		assertEquals(MessageEnum.DATA_NOT_FOUND.toString(),e.getErrorMessage());
	}

	@DisplayName("case when user exists and password not match")
	@Test
	public void updateUser_case_2() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","rohit@123","Peeyush","Bharadwaj",1);
		Optional<User> user=Optional.of(testUser);
		when(iUserDao.getUserById(anyInt())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(false);
		when(iUserDao.addUser(testUser)).thenReturn(testUser);

		UpdateRequestDto updateRequestDto=new UpdateRequestDto("Sudhanshu","Sharma","rohit@123","rohit@12345");
		ServiceLayerException e=assertThrows(ServiceLayerException.class,()->iUserService.updateUser(updateRequestDto,testUser.getId()));
		assertEquals(MessageEnum.NOT_AUTHORIZED.toString(),e.getErrorMessage());
	}

	@DisplayName("case when user exists and it is not logged in")
	@Test(expected = ServiceLayerException.class)
	public void updateUser_case_3() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","rohit@123","Peeyush","Bharadwaj",0);
		Optional<User> user=Optional.of(testUser);
		when(iUserDao.getUserById(anyInt())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);
		when(iUserDao.addUser(testUser)).thenReturn(testUser);

		UpdateRequestDto updateRequestDto=new UpdateRequestDto("Sudhanshu","Sharma","rohit@123","rohit@12345");
		UserResponseDto actualUser=iUserService.updateUser(updateRequestDto,1);
		assertNull(actualUser);
	}

	@DisplayName("case when user not exists")
	@Test
	public void updateUser_case_4() throws EntityNotFoundException,ServiceLayerException{
		User testUser=new User(1,"peeyush@gmail.com","rohit@123","Peeyush","Bharadwaj",1);
		Optional<User> user=Optional.of(testUser);
		when(iUserDao.getUserById(anyInt())).thenReturn(user);
		when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);
		when(iUserDao.addUser(testUser)).thenReturn(testUser);

		UpdateRequestDto updateRequestDto=new UpdateRequestDto("Sudhanshu","Sharma","rohit@123","rohit@12345");
		UserResponseDto actualUser=iUserService.updateUser(updateRequestDto,1);
		assertEquals(updateRequestDto.getFirstName(),actualUser.getFirstName());
	}

}
