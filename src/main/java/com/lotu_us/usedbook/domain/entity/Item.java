package com.lotu_us.usedbook.domain.entity;

import com.lotu_us.usedbook.domain.enums.Category;
import com.lotu_us.usedbook.domain.enums.SaleStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member seller;

    private int price;

    private int stock;

    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

    private LocalDateTime createTime;

    private int likeCount;

    private int viewCount;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<ItemFile> files;
    //files는 단순히 파일을 가져오기만 할 것이다. item의 외래키는 file이 관리할 것이다.

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<Comment> comments;
    //comments는 단순히 댓글을 가져오기만 할 것이다. 외래키는 comment가 관리할 것이다.
}
