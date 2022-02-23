package com.lotu_us.usedbook.domain.enums;

public enum SaleStatus {
    READY("판매중"), COMPLETE("판매완료");

    private String value;
    public String getValue() {
        return value;
    }
    SaleStatus(String value) {
        this.value = value;
    }
}
