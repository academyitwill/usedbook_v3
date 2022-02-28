package com.lotu_us.usedbook.util.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    //global
    DATABASE_ERROR("database", "database", "데이터베이스 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    PASSWORD_NOT_EQUAL("password", "password.notequal", "패스워드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    ID_NOT_FOUND("id", "id.notfound", "해당 id를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST)
    ;

    private String cause;
    private String code;
    private String message;
    private HttpStatus httpStatus;

    private ErrorCode(String cause, String code, String message, HttpStatus httpStatus) {
        this.cause = cause;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCause() {
        return cause;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
