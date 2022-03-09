package com.lotu_us.usedbook.domain.dto;

import com.lotu_us.usedbook.domain.entity.OrderBasket;
import lombok.Getter;
import lombok.ToString;

public class OrderBasketDTO {

    @Getter
    @ToString
    public static class Response{
        private Long id;
        private String itemTitle;
        private int itemPrice;
        private int itemStock;
        private int count;
    }

    public static OrderBasketDTO.Response entityToDto(OrderBasket orderBasket) {
        OrderBasketDTO.Response response = new OrderBasketDTO.Response();
        response.id = orderBasket.getId();
        response.itemTitle = orderBasket.getItem().getTitle();
        response.itemPrice = orderBasket.getItem().getPrice();
        response.itemStock = orderBasket.getItem().getStock();
        response.count = orderBasket.getCount();
        return response;
    }
}
