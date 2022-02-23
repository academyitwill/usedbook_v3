package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.util.ErrorResponse;
import com.lotu_us.usedbook.util.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice   //모든 컨트롤러에서 발생할 수 있는 예외를 잡아 처리할 수 있다.
public class ExceptionController {

    @ExceptionHandler(CustomException.class) //예외 종류마다 처리할 메서드를 정의한다
    public ResponseEntity customExceptionHandler(CustomException exception){
        return ResponseEntity
                .status(exception.getResponse().getHttpStatus())
                .body(exception.getResponse());
    }

    @ExceptionHandler(PasswordNotEqualException.class)  //예외 종류마다 처리할 메서드를 정의한다
    public ResponseEntity passwordNotEqualExceptionHandler(PasswordNotEqualException exception){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setField("password");
        errorResponse.setObjectName("");
        errorResponse.setCode("password.notequal");
        errorResponse.setDefaultMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
