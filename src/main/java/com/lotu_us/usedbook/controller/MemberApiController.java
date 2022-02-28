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
@RequestMapping("/api/member")
public class MemberApiController {
    private final MemberService memberService;

    /**
     * 회원가입
     */
    @PostMapping("/join")
    @ReturnBindingResultError
    public ResponseEntity join(@Validated @RequestBody MemberDTO.Join memberDTO, BindingResult bindingResult){
        Long memberId = memberService.join(memberDTO);
        return ResponseEntity.status(HttpStatus.OK).body(memberId);
    }

    /**
     * 닉네임 수정
     */
    @PutMapping("/update/{memberId}/nickname/{updateNickname}")
    public ResponseEntity updateNickname(
            @PathVariable Long memberId,
            @PathVariable @Size(max = 10, message = "{nickname.SizeMax}") String updateNickname
    ){
        //@PathVariable은 notnull조건을 추가하지 않아도된다. url에 입력하지 않으면 아예 다른 url이 되어버리니까!
        memberService.updateNickname(memberId, updateNickname);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    /**
     * 비밀번호 수정
     */
    @PutMapping("/update/{memberId}/password")
    @ReturnBindingResultError
    public ResponseEntity updatePassword(
            @PathVariable Long memberId,
            @Validated @RequestBody MemberDTO.UpdatePassword memberDTO, BindingResult bindingResult
    ){
        memberService.updatePassword(memberId, memberDTO);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    /**
     * 회원조회
     */
    @GetMapping("/{memberId}")
    @ReturnBindingResultError
    public ResponseEntity detail(@PathVariable Long memberId){
        MemberDTO.Response response = memberService.detail(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
