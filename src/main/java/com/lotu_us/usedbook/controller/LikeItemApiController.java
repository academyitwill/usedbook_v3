package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.LikeItemDTO;
import com.lotu_us.usedbook.service.LikeItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likeitem")
public class LikeItemApiController {
    private final LikeItemService likeItemService;

    /**
     * 관심상품 등록
    */
    @PostMapping("/{itemId}")
    public ResponseEntity like(
            @PathVariable Long itemId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        Long likeId = likeItemService.like(principalDetails, itemId);
        return ResponseEntity.status(HttpStatus.OK).body(likeId);
    }

    /**
     * 관심상품 해제
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity dislike(
            @PathVariable Long itemId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        likeItemService.disLike(principalDetails, itemId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 회원별 관심상품 목록
     */
    @GetMapping("/list/member/{memberId}")
    public ResponseEntity list(@AuthenticationPrincipal PrincipalDetails principalDetails){
        List<LikeItemDTO.Response> list = likeItemService.list(principalDetails);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}
