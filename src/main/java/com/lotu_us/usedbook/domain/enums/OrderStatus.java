package com.lotu_us.usedbook.domain.enums;

public enum OrderStatus {
    COMPLETE("완료"), CANCEL("취소");

    String value;
    OrderStatus(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
