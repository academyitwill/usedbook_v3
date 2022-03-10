package com.lotu_us.usedbook.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderitem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @Setter
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public OrderItem(Item item) {
        this.item = item;
    }

    public static List<OrderItem> createList(List<Item> itemList) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (Item item : itemList) {
            OrderItem orderItem = new OrderItem(item);
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}