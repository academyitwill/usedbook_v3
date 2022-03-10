package com.lotu_us.usedbook.domain.dto;

import com.lotu_us.usedbook.domain.entity.LikeItem;
import lombok.Getter;
import lombok.ToString;

public class LikeItemDTO {

    public static ItemDTO.Response entityToDTO(LikeItem likeItem){
        ItemDTO.Response response = ItemDTO.entityToDtoResponse(likeItem.getItem());

        return response;
    }

}
