package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.OrderBasketDTO;
import com.lotu_us.usedbook.domain.dto.OrderDTO;
import com.lotu_us.usedbook.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderApiController {
    private final OrderService orderService;

    /**
     * 장바구니에서 주문하기로 선택한 상품들 리스트 저장
     */
    @PostMapping("/temp")
    public ResponseEntity basketToOrder(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody Map<String, List<Long>> map
    ){
        principalDetails.setItemIdList(map.get("itemIds"));
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("")
    public ResponseEntity load(@AuthenticationPrincipal PrincipalDetails principalDetails){
        List<OrderBasketDTO.Response> load = orderService.load(principalDetails.getItemIdList());
        return ResponseEntity.status(HttpStatus.OK).body(load);
    }


    /**
     * 주문하기
     */
    @PostMapping("")
    public ResponseEntity save(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Validated @RequestBody OrderDTO.Save orderDTO
    ){
        orderService.save(principalDetails, orderDTO);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
