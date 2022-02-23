package com.lotu_us.usedbook.util.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
@Aspect
@Slf4j
public class ReturnBindingResultErrorAspect {

    @Pointcut("@annotation(com.lotu_us.usedbook.util.aop.ReturnBindingResultError)")
    private void Target(){

    }

    @Around("Target()") //@Before, @After로 하면 bindingresult 반환되고 이어지는 메서드가 실행되어버린다!!
    public Object aspectAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String type = joinPoint.getSignature().getDeclaringTypeName();
        String method = joinPoint.getSignature().getName();
        log.info("BindingResultAOP Run : type={}", type);
        log.info("BindingResultAOP Run : method={}", method);

        Object[] args = joinPoint.getArgs();
        //메서드에 들어가는 파라미터들 가져오기

        for (Object arg : args) {
            if(arg instanceof BindingResult){
                BindingResult bindingResult = (BindingResult) arg;

                if(bindingResult.hasErrors()){
                    log.info("BindingResultAOP : bindingResult.hasErrors() == true");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult);
                }
            }
        }
        return joinPoint.proceed();
    }
}
