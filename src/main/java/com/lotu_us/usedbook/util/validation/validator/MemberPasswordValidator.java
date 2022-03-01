package com.lotu_us.usedbook.util.validation.validator;

import com.lotu_us.usedbook.util.validation.annotation.Password;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class MemberPasswordValidator implements ConstraintValidator<Password, String> {

    private int min;
    private int max;

    private final String allowNumber = "(?=.*[0-9])";
    private final String allowEnglish = "(?=.*[a-z])";
    private final String allowSpecial = "(?=.*[!\"#$%&'()*+,-./:;<=>?@^_`{|}\\[\\]~\\\\])(?=\\S+$)";
    private final String NotAllowSpace = "(?=\\S+$)";
    public final String passwordRegexp = "^"+allowNumber+allowEnglish+allowSpecial+NotAllowSpace+".{5,20}$";

    @Override
    public void initialize(Password constraintAnnotation) {
        //어노테이션 입력 시 파라미터로 들어온 값 초기화
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if(!value.matches(passwordRegexp)){
            context.buildConstraintViolationWithTemplate("비밀번호는 숫자, 영어, 특수문자 각각 1개 이상 포함하고 "+min+"자 ~ "+ max +"자로 입력해주세요").addConstraintViolation();
            return false;
        }

        return true;
    }
}
