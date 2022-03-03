package com.lotu_us.usedbook.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
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

    @Builder
    public ItemFile(Item item, String filePath, String fileName) {
        this.item = item;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "ItemFile{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
