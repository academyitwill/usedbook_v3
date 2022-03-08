package com.lotu_us.usedbook.domain.entity;

import com.lotu_us.usedbook.domain.enums.OrderStatus;
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

    private LocalDateTime orderTime = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COMPLETE;

    @Builder
    public Order(Member member, List<OrderItem> orderItems) {
        this.member = member;
        this.orderItems = orderItems;
    }


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", orderItems=" + orderItems +
                ", orderTime=" + orderTime +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
