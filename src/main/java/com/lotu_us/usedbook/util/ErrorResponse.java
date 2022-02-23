package com.lotu_us.usedbook.util;

import com.lotu_us.usedbook.util.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    private String causeTarget;
    private String code;
    private String message;
    private HttpStatus httpStatus;

    @Builder
    public ErrorResponse(String cause, String code, String message, HttpStatus httpStatus) {
        this.causeTarget = cause;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ErrorResponse(ErrorCode errorCode) {
        this.causeTarget = errorCode.getCause();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

}
