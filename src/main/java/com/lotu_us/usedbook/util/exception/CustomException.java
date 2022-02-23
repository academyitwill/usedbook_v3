package com.lotu_us.usedbook.util.exception;

import com.lotu_us.usedbook.util.ErrorResponse;
import lombok.Builder;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{

    private ErrorResponse errorResponse;

    public static CustomException globalError(ErrorCode errorCode){
        return new CustomException(errorCode);
    }

    private CustomException(ErrorCode errorCode) {
        errorResponse = new ErrorResponse(errorCode);
    }

    @Builder(builderClassName = "localError", builderMethodName = "localError")
    public CustomException(String cause, String code, String message, HttpStatus httpStatus){
        errorResponse = ErrorResponse.builder()
                .cause(cause)
                .code(code)
                .message(message)
                .httpStatus(httpStatus).build();
    }

    public ErrorResponse getResponse(){
        return errorResponse;
    }
}
