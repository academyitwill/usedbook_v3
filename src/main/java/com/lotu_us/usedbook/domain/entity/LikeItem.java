package com.lotu_us.usedbook.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likeitem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public LikeItem(Member member, Item item) {
        this.member = member;
        this.item = item;
    }

    @Override
    public String toString() {
        return "LikeItem{" +
                "id=" + id +
                ", member=" + member +
                ", item=" + item +
                '}';
    }
}
