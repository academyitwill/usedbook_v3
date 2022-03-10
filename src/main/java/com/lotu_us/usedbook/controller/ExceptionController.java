package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.util.error.ErrorResponse;
import com.lotu_us.usedbook.util.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ControllerAdvice   //모든 컨트롤러에서 발생할 수 있는 예외를 잡아 처리할 수 있다.
public class ExceptionController {

    @ExceptionHandler(CustomException.class) //예외 종류마다 처리할 메서드를 정의한다
    public ResponseEntity customExceptionHandler(CustomException exception){
        return ResponseEntity
                .status(exception.getResponse().getHttpStatus())
                .body(exception.getResponse());
    }

    //RequestParam, PathVariable Validated
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity constraintViolationExceptionHandler(ConstraintViolationException exception){
        String cause = "";
        String message = "";
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            cause = constraintViolation.getPropertyPath().toString();
            message = constraintViolation.getMessage();
        }
        cause = cause.split("\\.")[1];

        ErrorResponse errorResponse = ErrorResponse.builder()
                .cause(cause)
                .code("parameter.validated")
                .message(message)
                .httpStatus(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        System.out.println("==========================");
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<ErrorResponse> errorResponseList = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .cause(fieldError.getField())
                    .code(fieldError.getCode())
                    .message(fieldError.getDefaultMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST).build();
            errorResponseList.add(errorResponse);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseList);
    }
}
