package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.UpdateRequestDto;
import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.APIResponse;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.enums.MessageEnum;
import com.example.demo.services.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    UserServiceImpl userService;

    List<UserResponseDto> userList=new ArrayList<>();
    private ObjectMapper objectMapper;

    @Before
    public void setUp(){
        objectMapper=new ObjectMapper();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        UserResponseDto userResponse1=new UserResponseDto(1,"Peeyush","Bharadwaj","peeyush@gmail.com",0);
        UserResponseDto userResponse2=new UserResponseDto(2,"Rohit","Bharadwaj","rohit@gmail.com",1);
        UserResponseDto userResponse3=new UserResponseDto(3,"Sudhanshu","Sharma","sudhanshu@gmail.com",0);
        userList.add(userResponse1);
        userList.add(userResponse2);
        userList.add(userResponse3);
    }

    //-----------------------Test for getUsers() controller-----------------------------------

    @Test
    public void getUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(userList);
        String jasonRequest = objectMapper.writeValueAsString(userList);
        Mockito.when(userService.getAllUsers()).thenReturn(userList);
        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .content(jasonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        String resultContent = mvcResult.getResponse().getContentAsString();
        APIResponse<?> apiResponse = objectMapper.readValue(resultContent,APIResponse.class);
        Assert.assertEquals(apiResponse.getMessage(), MessageEnum.FOUND_ALL_USERS.toString());
    }

    //-----------------------Test for createAccount() controller-----------------------------------

    @DisplayName("case when javax validation doesn't fail")
    @Test
    public void createAccount_case_1() throws Exception {
        UserRequestDto userRequestDto=new UserRequestDto("Peeyush","Bharadwaj","peeyush@gmail.com","Lambda@123");
        UserResponseDto testUserResponse=new UserResponseDto(4,"Peeyush","Bharadwaj","peeyush@gmail.com",0);
        Mockito.when(userService.addUser(userRequestDto)).thenReturn(testUserResponse);
        String jasonRequest = objectMapper.writeValueAsString(userRequestDto);
        MvcResult mvcResult = mockMvc.perform(post("/createaccount")
                        .content(jasonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();
        String resultContent = mvcResult.getResponse().getContentAsString();
        APIResponse<?> apiResponse = objectMapper.readValue(resultContent,APIResponse.class);
        Assert.assertEquals(apiResponse.getMessage(), MessageEnum.SUCCESS_CREATE_ACCOUNT.toString());
    }

    @DisplayName("case when javax validation fails")
    @Test
    public void createAccount_case_2() throws Exception {
        UserRequestDto userRequestDto=new UserRequestDto("","Bharadwaj","peeyush@gmail.com","Lambda@123");
        UserResponseDto testUserResponse=new UserResponseDto(4,"Peeyush","Bharadwaj","peeyush@gmail.com",0);
        Mockito.when(userService.addUser(userRequestDto)).thenReturn(testUserResponse);
        String jasonRequest = objectMapper.writeValueAsString(userRequestDto);
        MvcResult mvcResult = mockMvc.perform(post("/createaccount")
                        .content(jasonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    //-----------------------Test for signIn() controller-----------------------------------

    @Test
    public void signIn() throws Exception {
        LoginRequestDto loginRequestDto=new LoginRequestDto("peeyush@gmail.com","Lambda@123");
        UserResponseDto testUserResponse=new UserResponseDto(4,"Peeyush","Bharadwaj","peeyush@gmail.com",0);
        Mockito.when(userService.loginUser(loginRequestDto)).thenReturn(testUserResponse);
        String jasonRequest = objectMapper.writeValueAsString(loginRequestDto);
        MvcResult mvcResult = mockMvc.perform(put("/login")
                        .content(jasonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        String resultContent = mvcResult.getResponse().getContentAsString();
        APIResponse<?> apiResponse = objectMapper.readValue(resultContent,APIResponse.class);
        Assert.assertEquals(apiResponse.getMessage(), MessageEnum.LOGGED_IN.toString());
    }

    //-----------------------Test for signOut() controller-----------------------------------

    @Test
    public void signOut() throws Exception {
        Map<String,String> payLoad=new HashMap<>();
        payLoad.put("password","Lambda@123");
        UserResponseDto testUserResponse=new UserResponseDto(4,"Peeyush","Bharadwaj","peeyush@gmail.com",1);
        Mockito.when(userService.logoutUser(Mockito.anyInt(),Mockito.anyString())).thenReturn(testUserResponse);
        String jasonRequest = objectMapper.writeValueAsString(payLoad);
        MvcResult mvcResult = mockMvc.perform(put("/logout/{id}",4)
                        .content(jasonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String resultContent = mvcResult.getResponse().getContentAsString();
        APIResponse<?> apiResponse = objectMapper.readValue(resultContent,APIResponse.class);
        Assert.assertEquals(apiResponse.getMessage(), MessageEnum.LOGGED_OUT.toString());
    }

    //-----------------------Test for updateAccount() controller-----------------------------------

    @Test
    public void updateAccount() throws Exception {
        UpdateRequestDto updateRequestDto=new UpdateRequestDto("sudhanshu","Bharadwaj","Lambda@123","Peeyush@123");
        UserResponseDto testUserResponse=new UserResponseDto(4,"Peeyush","Bharadwaj","peeyush@gmail.com",0);
        Mockito.when(userService.updateUser(updateRequestDto,4)).thenReturn(testUserResponse);
        String jasonRequest = objectMapper.writeValueAsString(updateRequestDto);
        MvcResult mvcResult = mockMvc.perform(put("/updateaccount/{id}",4)
                        .content(jasonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String resultContent = mvcResult.getResponse().getContentAsString();
        APIResponse<?> apiResponse = objectMapper.readValue(resultContent,APIResponse.class);
        Assert.assertEquals(apiResponse.getMessage(), MessageEnum.SUCCESS_UPDATED.toString());
    }
}
