package com.lotu_us.usedbook.domain.entity;

import com.lotu_us.usedbook.domain.dto.OrderDTO;
import com.lotu_us.usedbook.repository.ItemRepository;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @Setter
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    @Builder
    public OrderItem(Item item, int count) {
        this.item = item;
        this.count = count;
    }


    public static List<OrderItem> createList(ItemRepository itemRepository, OrderDTO.Save orderDTO) {
        List<Item> allByIdIn = itemRepository.findAllByIdIn(orderDTO.getItemIdList());

        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < allByIdIn.size(); i++) {
             Item item = allByIdIn.get(i);
             int count = orderDTO.getItemCountList().get(i);

             OrderItem orderItem = new OrderItem(item, count);
             orderItems.add(orderItem);
        }
        return orderItems;
    }
}
