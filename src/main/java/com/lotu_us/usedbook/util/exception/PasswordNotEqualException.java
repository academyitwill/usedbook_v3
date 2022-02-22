package com.lotu_us.usedbook.util.exception;

public class PasswordNotEqualException extends RuntimeException {
    public PasswordNotEqualException() {
        super("패스워드가 일치하지 않습니다.");
    }

    public PasswordNotEqualException(String message) {
        super(message);
    }
}
