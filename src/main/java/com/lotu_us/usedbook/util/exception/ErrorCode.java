package com.lotu_us.usedbook.util.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    //global
    DATABASE_ERROR("database", "database", "데이터베이스 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    PASSWORD_NOT_EQUAL("password", "password.notequal", "패스워드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    ID_NOT_FOUND("id", "id.notfound", "해당 id를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    ONLY_MEMBER("access", "access.denied", "회원만 기능을 이용할 수 있습니다.", HttpStatus.BAD_REQUEST),

    //member
    EMAIL_DUPLICATED("email", "email.duplicated", "이메일이 중복됩니다.", HttpStatus.BAD_REQUEST),
    NICKNAME_DUPLICATED("nickname", "nickname.duplicated", "닉네임이 중복됩니다.", HttpStatus.BAD_REQUEST),
    NICKNAME_EQUAL_PREVIOUS("nickname", "nickname.equal.previous", "닉네임이 이전과 같습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_EQUAL_PREVIOUS("newPassword", "password.equal.previous", "기존 비밀번호와 새 비밀번호가 일치합니다.", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND("email", "email.notfound", "해당 이메일을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    NICKNAME_NOT_EQUAL("nickname", "nickname.notequal", "닉네임이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    //item
    EDIT_ACCESS_DENIED("edit", "edit.access.denied", "수정 권한이 없습니다.", HttpStatus.BAD_REQUEST),
    DELETE_ACCESS_DENIED("delete", "delete.access.denied", "삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST),

    //orderbasket
    ALREADY_SAVED_BASKET("save", "already.saved.orderbasket", "이미 장바구니에 담긴 상품입니다.", HttpStatus.BAD_REQUEST)

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
