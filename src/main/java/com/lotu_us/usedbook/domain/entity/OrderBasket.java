package com.lotu_us.usedbook.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderBasket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderbasket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    @Builder
    public OrderBasket(Member member, Item item, int count) {
        this.member = member;
        this.item = item;
        this.count = count;
    }
}
