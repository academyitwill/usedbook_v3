package com.lotu_us.usedbook.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int depth;

    private String content;

    private LocalDateTime createTime;

    @Builder
    public Comment(Member writer, Item item, int depth, String content) {
        this.writer = writer;
        this.item = item;
        this.depth = depth;
        this.content = content;
    }

    @PrePersist
    public void setCreateTime() {
        this.createTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
//                ", writer=" + writer +
//                ", item=" + item +
                ", depth=" + depth +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
