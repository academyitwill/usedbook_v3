package com.lotu_us.usedbook.domain.dto;

import com.lotu_us.usedbook.domain.entity.LikeItem;
import lombok.Getter;
import lombok.ToString;

public class LikeItemDTO {

    @Getter
    @ToString
    public static class Response{
        private Long id;
        private MemberDTO.Response member;
        private ItemDTO.Response item;
    }

    public static LikeItemDTO.Response entityToDTO(LikeItem likeItem){
        LikeItemDTO.Response response = new LikeItemDTO.Response();

        response.id = likeItem.getId();
        response.member = MemberDTO.entityToDto(likeItem.getMember());
        response.item = ItemDTO.entityToDtoResponse(likeItem.getItem());

        return response;
    }

}
