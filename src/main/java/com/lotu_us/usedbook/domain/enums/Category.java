package com.lotu_us.usedbook.domain.enums;

public enum Category {
    NOVEL("소설"),
    HUMANITIES("인문"),
    CARTOON("만화");

    private final String value;
    Category(String value) {  this.value = value;  }
    public String getValue() {
        return value;
    }
}
