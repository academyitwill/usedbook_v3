package com.lotu_us.usedbook.domain.dto;

import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.enums.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ItemDTO {


    @Getter
    @ToString
    public static class Write{
        @Size(min = 3, max = 30, message = "{title.size}")
        private String title;

        @NotNull
        private Category category;

        @Min(value = 1000, message = "{price.min}")
        @Max(value = 1000000, message = "{price.max}")
        private int price;

        @Min(value = 1, message = "{stock.min}")
        @Max(value = 100, message = "{stock.max}")
        private int stock;

        @NotBlank
        private String content;

        @Builder
        public Write(String title, Category category, int price, int stock, String content) {
            this.title = title;
            this.category = category;
            this.price = price;
            this.stock = stock;
            this.content = content;
        }
    }



    @Getter
    @ToString
    public static class Response{
        private Long id;
        private String title;
        private String seller;
        private String category;
        private int price;
        private int stock;
        private String saleStatus;
        private String content;
        private int likeCount;
        private int viewCount;
        private String createTime;
        private List<ItemFileDTO.Response> files;

        private boolean likeStatus = false;     //현재 로그인한 사용자가 해당 게시글에 좋아요를 눌렀는지
        private boolean memberIsSeller = false;       //현재 로그인한 사용자가 해당 게시글을 작성한 사람인지

        public void setLikeStatus(boolean likeStatus) {
            this.likeStatus = likeStatus;
        }
        public void memberIsSellerTrue() {
            this.memberIsSeller = true;
        }
    }


    public static ItemDTO.Response entityToDtoResponse(Item item) {
        ItemDTO.Response response = new Response();
        response.id = item.getId();
        response.title = item.getTitle();
        response.seller = item.getSeller().getNickname();
        response.category = item.getCategory().getValue();
        response.price = item.getPrice();
        response.stock = item.getStock();
        response.saleStatus = item.getSaleStatus().getValue();
        response.content = item.getContent();
        response.likeCount = item.getLikeCount();
        response.viewCount = item.getViewCount() +1;
        response.createTime = item.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        response.files = ItemFileDTO.entityListToDtoList(item.getFiles());
        return response;
    }

    @Getter
    @ToString
    public static class ListResponse{
        private Long id;
        private String seller;
        private String title;
        private String category;
        private String createTime;
        private String saleStatus;
        private int viewCount;
        private int commentCount;
    }

    public static ItemDTO.ListResponse entityToDtoListResponse(Item item){
        ListResponse listResponse = new ListResponse();
        listResponse.id = item.getId();
        listResponse.seller = item.getSeller().getNickname();
        listResponse.title = item.getTitle();
        listResponse.category = item.getCategory().getValue();
        listResponse.createTime = item.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        listResponse.saleStatus = item.getSaleStatus().getValue();
        listResponse.viewCount = item.getViewCount();
        listResponse.commentCount = item.getCommentCount();
        return listResponse;
    }


    @Getter
    @ToString
    public static class ResponseIndex{
        private Long id;
        private String title;
        private int price;
        private String fileName;
    }

    public static ResponseIndex entityToDTOIndex(Item item) {
        ResponseIndex index = new ResponseIndex();
        index.id = item.getId();
        index.title = item.getTitle();
        index.price = item.getPrice();
        if(item.getFiles().size() == 0){
            index.fileName = "";
        }else{
            index.fileName = item.getFiles().get(0).getFileName();
        }

        return index;
    }
}
