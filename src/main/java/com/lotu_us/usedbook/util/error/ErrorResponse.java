package com.lotu_us.usedbook.util.error;

import com.lotu_us.usedbook.util.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String cause;
    private String code;
    private String message;
    private HttpStatus httpStatus;

    @Builder
    public ErrorResponse(String cause, String code, String message, HttpStatus httpStatus) {
        this.cause = cause;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ErrorResponse(ErrorCode errorCode) {
        this.cause = errorCode.getCause();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.httpStatus = errorCode.getHttpStatus();
    }

}
