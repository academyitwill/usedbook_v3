package com.lotu_us.usedbook.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardController {
    @GetMapping("/dashboard/myInfo")
    public String myInfo(){
        return "dashboard/myInfo";
    }

    @GetMapping("/dashboard/myItems")
    public String myItems(){
        return "dashboard/myItems";
    }

    @GetMapping("/dashboard/myComments")
    public String myComments(){
        return "dashboard/myComments";
    }

    @GetMapping("/dashboard/myFavorites")
    public String myFavorites(){
        return "dashboard/myFavorites";
    }

    @GetMapping("/dashboard/myOrders")
    public String myOrders(){
        return "dashboard/myOrders";
    }

    @GetMapping({"/dashboard/myChat", "/dashboard/myChat/{roomId}/{nickname}"})
    public String myChat(){
        return "dashboard/myChat";
    }
}
