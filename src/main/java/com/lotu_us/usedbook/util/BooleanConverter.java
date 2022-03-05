package com.lotu_us.usedbook.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Entity의 boolean타입을 True, False로 저장될 수 있게한다.
 */
@Converter
public class BooleanConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if(attribute != null && attribute){
            return "TRUE";
        }
        return "FALSE";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        if("TRUE".equalsIgnoreCase(dbData)){
            return true;
        }
        return false;
    }
}
