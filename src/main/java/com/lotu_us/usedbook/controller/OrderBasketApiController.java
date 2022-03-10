package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.OrderBasketDTO;
import com.lotu_us.usedbook.service.OrderBasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Long orderBasketId = orderBasketService.save(principalDetails, itemId, count);
        return ResponseEntity.status(HttpStatus.OK).body(orderBasketId);
    }

    /**
     * 장바구니에 상품 수량 수정
     */
    @PutMapping("/item/{itemId}/{count}")
    public ResponseEntity update(
            @PathVariable Long itemId,
            @PathVariable int count,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        orderBasketService.update(principalDetails, itemId, count);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 장바구니에 상품 삭제
     */
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity delete(
            @PathVariable Long itemId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        orderBasketService.delete(principalDetails, itemId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 장바구니 리스트
     * @param principalDetails
     * @return
     */
    @GetMapping("")
    public ResponseEntity list(@AuthenticationPrincipal PrincipalDetails principalDetails){
        List<OrderBasketDTO.Response> list = orderBasketService.list(principalDetails);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

}
