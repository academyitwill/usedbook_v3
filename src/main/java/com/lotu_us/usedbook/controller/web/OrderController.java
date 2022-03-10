package com.lotu_us.usedbook.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OrderController {

    @GetMapping("/basket")
    public String basket(){
        return "order/orderbasket";
    }

    @GetMapping("/order")
    public String order() {
        return "order/order";
    }

    @GetMapping("/order/{orderId}")
    public String detail(@PathVariable Long orderId){
        return "order/detail";
    }
}
