package com.lotu_us.usedbook.domain.dto;

import com.lotu_us.usedbook.domain.entity.Address;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.Order;
import com.lotu_us.usedbook.domain.entity.OrderItem;
import com.lotu_us.usedbook.domain.enums.Payment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {


    @Getter
    @ToString
    @NoArgsConstructor
    public static class OrderItems{
        private Long itemId;
        private String itemTitle;
        private int itemPrice;
        private int itemCount;

        //order 저장용
        public OrderItems(Long itemId, int itemCount) {
            this.itemId = itemId;
            this.itemCount = itemCount;
        }

        //order Detail 반환용
        public OrderItems(Long itemId, String itemTitle, int itemPrice, int itemCount) {
            this.itemId = itemId;
            this.itemTitle = itemTitle;
            this.itemPrice = itemPrice;
            this.itemCount = itemCount;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class Save{
        @Size(min = 1)
        private List<OrderDTO.OrderItems> orderItemsList = new ArrayList<>();

        @Valid
        private Address address;

        @NotNull
        private Payment payment;

        public List<Long> getItemIdList(){
            return orderItemsList.stream()
                .map(orderItems -> orderItems.getItemId())
                .collect(Collectors.toList());
        }

        public List<Integer> getItemCountList(){
            return orderItemsList.stream()
                .map(orderItems -> orderItems.getItemCount())
                .collect(Collectors.toList());
        }
    }


    @Getter
    @ToString
    public static class Response{
        private Long orderId;
        private List<OrderDTO.OrderItems> orderItems;
        private Address address;
        private String payment;
        private String orderTime;
    }


    public static OrderDTO.Response entityToDTO(Order order) {
        OrderDTO.Response response = new OrderDTO.Response();
        response.orderId = order.getId();
        List<OrderDTO.OrderItems> orderItems = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            Item item = orderItem.getItem();
            orderItems.add(
                    new OrderDTO.OrderItems(
                            item.getId(),
                            item.getTitle(),
                            item.getPrice(),
                            orderItem.getCount()
                    )
            );
        }
        response.orderItems = orderItems;
        response.address = order.getAddress();
        response.payment = order.getPayment().toString();
        response.orderTime = order.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return response;
    }


    @Getter
    @ToString
    public static class ResponseList {
        private Long orderId;
        private List<OrderDTO.OrderItems> orderItems;
        private String orderTime;
    }

    public static OrderDTO.ResponseList entityToDTOList(Order order) {
        OrderDTO.ResponseList response = new OrderDTO.ResponseList();
        response.orderId = order.getId();
        List<OrderDTO.OrderItems> orderItems = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            Item item = orderItem.getItem();
            orderItems.add(
                    new OrderDTO.OrderItems(
                            item.getId(),
                            item.getTitle(),
                            item.getPrice(),
                            orderItem.getCount()
                    )
            );
        }
        response.orderItems = orderItems;
        response.orderTime = order.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return response;
    }



}
