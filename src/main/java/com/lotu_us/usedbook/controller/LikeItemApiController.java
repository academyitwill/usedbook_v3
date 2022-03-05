package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.service.LikeItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
