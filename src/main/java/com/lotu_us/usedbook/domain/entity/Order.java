package com.lotu_us.usedbook.domain.entity;

import com.lotu_us.usedbook.domain.dto.OrderDTO;
import com.lotu_us.usedbook.domain.enums.OrderStatus;
import com.lotu_us.usedbook.domain.enums.Payment;
import com.lotu_us.usedbook.repository.ItemRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COMPLETE;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Payment payment;

    private LocalDateTime orderTime = LocalDateTime.now();

    @Builder
    public Order(Member member, List<OrderItem> orderItems, Address address, Payment payment) {
        this.member = member;
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(this);
        }
        this.orderItems = orderItems;
        this.address = address;
        this.payment = payment;
    }


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", orderItems=" + orderItems +
                ", orderStatus=" + orderStatus +
                ", address=" + address +
                ", payment=" + payment +
                ", orderTime=" + orderTime +
                '}';
    }
}
