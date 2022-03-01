package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.domain.dto.MemberDTO;
import com.lotu_us.usedbook.util.validation.annotation.Email;
import com.lotu_us.usedbook.util.validation.annotation.Nickname;
import com.lotu_us.usedbook.util.validation.annotation.Password;
import com.lotu_us.usedbook.service.MemberService;
import com.lotu_us.usedbook.util.aop.ReturnBindingResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated  //requestparam, pathvariable의 경우는 클래스레벨에 붙여서 검증해야한다. //메서드레벨에 붙여서 검증하는건 modelattribute, requestbody이다.
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberApiController {
    private final MemberService memberService;

    /**
     * 회원가입 - 저장
     */
    @PostMapping("")
    @ReturnBindingResultError
    public ResponseEntity join(@Validated @RequestBody MemberDTO.Join memberDTO, BindingResult bindingResult){
        Long memberId = memberService.join(memberDTO);
        return ResponseEntity.status(HttpStatus.OK).body(memberId);
    }

    /**
     * 회원가입 - 이메일 검증 및 중복체크
     */
    @PostMapping("/email/check/{email}")
    public boolean joinEmailCheck(@PathVariable @Email String email){
        return memberService.joinEmailDuplicatedCheck(email);
    }

    /**
     * 회원가입 - 닉네임 검증 및 중복체크
     */
    @PostMapping("/nickname/check/{nickname}")
    public boolean joinNicknameCheck(@PathVariable @Nickname String nickname){
        return memberService.joinNicknameDuplicatedCheck(nickname);
    }

    /**
     * 회원가입 - 비밀번호 양식 일치 확인
     * (javascript에서 검증로직 추가하는 것으로 변경하자. 채팅 기능까지 완료 후에 추가할 예정)
     */
    @PostMapping("/password/check")
    public boolean joinPasswordCheck(@RequestBody Map<String, @Password String> map){
        return true;
    }

    /**
     * 닉네임 수정
     */
    @PutMapping("/{memberId}/nickname/{updateNickname}")
    public ResponseEntity updateNickname(@PathVariable Long memberId, @PathVariable @Nickname String updateNickname){
        memberService.updateNickname(memberId, updateNickname);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    /**
     * 비밀번호 수정
     */
    @PutMapping("/{memberId}/password")
    @ReturnBindingResultError
    public ResponseEntity updatePassword(
            @PathVariable Long memberId,
            @Validated @RequestBody MemberDTO.UpdatePassword memberDTO, BindingResult bindingResult
    ){
        memberService.updatePassword(memberId, memberDTO);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    /**
     * 비밀번호 찾기
     */
    @PostMapping("/findPassword")
    @ReturnBindingResultError
    public ResponseEntity findPassword(@Validated @RequestBody MemberDTO.findPassword memberDTO, BindingResult bindingResult){
        memberService.findPassword(memberDTO);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    /**
     * 회원조회
     */
    @GetMapping("/{memberId}")
    public ResponseEntity detail(@PathVariable Long memberId){
        MemberDTO.Response response = memberService.detail(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
