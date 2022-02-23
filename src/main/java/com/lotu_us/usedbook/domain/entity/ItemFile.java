package com.lotu_us.usedbook.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemfile_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private String filePath;

    private String fileName;
}
