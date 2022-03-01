package com.lotu_us.usedbook.util.validation.validator;


import com.lotu_us.usedbook.util.validation.annotation.Email;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
public class MemberEmailValidator implements ConstraintValidator<Email, String> {

    String regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

    @Override
    public void initialize(Email constraintAnnotation) {
        //어노테이션 입력 시 파라미터로 들어온 값 초기화
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //기본메시지 비활성화
        context.disableDefaultConstraintViolation();

        value = URLDecoder.decode(value, StandardCharsets.UTF_8);
        //url로 넘어오는 특수문자 처리

        if(!value.matches(regexp)){
            context.buildConstraintViolationWithTemplate("이메일 형식과 맞지 않습니다.").addConstraintViolation();
            return false;
        }

        return true;
    }
}
