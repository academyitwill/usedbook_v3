package com.lotu_us.usedbook.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
