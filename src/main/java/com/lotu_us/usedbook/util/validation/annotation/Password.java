package com.lotu_us.usedbook.util.validation.annotation;

import com.lotu_us.usedbook.util.validation.validator.MemberPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented                                                                     //애노테이션 정보를 문서에 같이 보여줌
@Constraint(validatedBy = MemberPasswordValidator.class)                              //하단 설명 참고
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })   //해당 어노테이션이 어디에 사용될 수 있는지
@Retention(RUNTIME)                                                             //해당 어노테이션을 언제까지 유지할지
public @interface Password {

    /*
    @Constraint
    애노테이션을 Bean Validation Constraint로 만들어주는 애노테이션입니다.
    이 애노테이션을 가진 클래스는 반드시 아래와 같은 속성값을 가져야합니다.
    validatedBy = 구현클래스명.class
    */
    String message() default "Password is not allow";       //default message
    Class<?>[] groups() default {};                         //targeted group을 customize하기 위해 사용
    Class<? extends Payload>[] payload() default {};        //확장성을 위해 사용


    //파라미터
    public int min() default 2;
    public int max() default 20;
}
