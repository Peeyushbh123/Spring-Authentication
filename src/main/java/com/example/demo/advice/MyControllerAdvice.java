package com.example.demo.advice;

import com.example.demo.custom.exception.EntityNotFoundException;
import com.example.demo.custom.exception.ServiceLayerException;
import com.example.demo.enums.MessageEnum;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.example.demo.dto.response.APIResponse;

@RestControllerAdvice
public class MyControllerAdvice extends ResponseEntityExceptionHandler {

    // Description-- Globally handling the Entity Not Found Exception
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(EntityNotFoundException entityNotFoundException){
        APIResponse<String> res=new APIResponse<>();
        res.setMessage(entityNotFoundException.getErrorMessage());
        return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
    }

    // Description-- Globally handling the Service Layer Exception
    @ExceptionHandler(ServiceLayerException.class)
    public ResponseEntity<?> handleServiceLayerException(ServiceLayerException serviceLayerException){
        APIResponse<String> res=new APIResponse<>();
        res.setMessage(serviceLayerException.getErrorMessage());
        return new ResponseEntity<>(res,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Description-- Globally handling the Javax Validation Exception
    @Override
    protected @NonNull ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult bindingResult=ex.getBindingResult();
        APIResponse<String> res=new APIResponse<>();
        if(bindingResult.hasFieldErrors("firstName")){
            res.setMessage(MessageEnum.SEND_PROPER_FIRST_NAME.toString());
            return new ResponseEntity<>(res,HttpStatus.NOT_ACCEPTABLE);
        }
        if(bindingResult.hasFieldErrors("lastName")){
            res.setMessage(MessageEnum.SEND_PROPER_LAST_NAME.toString());
            return new ResponseEntity<>(res,HttpStatus.NOT_ACCEPTABLE);
        }
        if(bindingResult.hasFieldErrors("emailId")){
            res.setMessage(MessageEnum.EMAIL_NOT_VALID.toString());
            return new ResponseEntity<>(res,HttpStatus.NOT_ACCEPTABLE);
        }
        if(bindingResult.hasFieldErrors("password")){
            res.setMessage(MessageEnum.SEND_PROPER_PASSWORD.toString());
            return new ResponseEntity<>(res,HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(res,HttpStatus.NOT_ACCEPTABLE);
    }

    // Description-- Globally handling all other Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAnyOtherException(Exception exception){
        APIResponse<String> res=new APIResponse<>();
        res.setMessage(exception.getMessage());
        return new ResponseEntity<>(res,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
