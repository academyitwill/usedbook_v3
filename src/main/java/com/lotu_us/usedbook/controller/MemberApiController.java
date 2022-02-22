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
