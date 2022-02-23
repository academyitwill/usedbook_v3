package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.domain.dto.MemberDTO;
import com.lotu_us.usedbook.service.MemberService;
import com.lotu_us.usedbook.util.aop.ReturnBindingResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;

@Validated  //requestparam, pathvariable의 경우는 클래스레벨에 붙여서 검증해야한다. //메서드레벨에 붙여서 검증하는건 modelattribute, requestbody이다.
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/join")
    @ReturnBindingResultError
    public ResponseEntity join(@Validated @ModelAttribute MemberDTO.Join memberDTO, BindingResult bindingResult){
        memberService.join(memberDTO);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
