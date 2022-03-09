package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.service.OrderBasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/basket")
public class OrderBasketApiController {
    private final OrderBasketService orderBasketService;

    /**
     * 장바구니에 상품 저장
     */
    @PostMapping("/item/{itemId}/{count}")
    public ResponseEntity save(
            @PathVariable Long itemId,
            @PathVariable int count,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        orderBasketService.save(principalDetails, itemId, count);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
