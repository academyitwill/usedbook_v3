package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.util.ErrorResponse;
import com.lotu_us.usedbook.util.exception.DataBaseException;
import com.lotu_us.usedbook.util.exception.PasswordNotEqualException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice   //모든 컨트롤러에서 발생할 수 있는 예외를 잡아 처리할 수 있다.
public class ExceptionController {

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity databaseExceptionHandler(DataBaseException exception){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setField("");
        errorResponse.setObjectName("database");
        errorResponse.setCode("database");
        errorResponse.setDefaultMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
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
