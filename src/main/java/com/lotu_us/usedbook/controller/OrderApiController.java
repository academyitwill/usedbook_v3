package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.OrderDTO;
import com.lotu_us.usedbook.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderApiController {
    private final OrderService orderService;

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
