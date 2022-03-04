package com.lotu_us.usedbook.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    //대댓글
    private Long parentId;



    @Builder
    public Comment(Member writer, Item item, int depth, String content, Long parentId) {
        this.writer = writer;
        this.item = item;
        this.depth = depth;
        this.content = content;
        this.parentId = parentId;
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

    public void changeContent(String content) {
        this.content = content;
    }
}
