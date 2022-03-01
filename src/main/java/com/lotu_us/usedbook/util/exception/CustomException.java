package com.lotu_us.usedbook.util.exception;

import com.lotu_us.usedbook.util.ErrorResponse;

public class CustomException extends RuntimeException{

    private ErrorResponse errorResponse = new ErrorResponse();

    public CustomException(ErrorCode errorCode) {
        this.errorResponse = new ErrorResponse(errorCode);
    }

    public ErrorResponse getResponse(){
        return errorResponse;
    }
}
