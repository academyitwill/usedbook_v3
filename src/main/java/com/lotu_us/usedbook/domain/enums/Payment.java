package com.lotu_us.usedbook.domain.enums;

public enum Payment {
    CARD("체크/신용카드"), MOBILE("휴대폰결제"), ACCOUNT("무통장입금");

    String value;

    Payment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
