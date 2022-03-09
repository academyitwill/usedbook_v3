package com.lotu_us.usedbook.domain.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderDTO {

    @Getter
    @ToString
    public static class Save{
        @NotNull
        private List<Long> itemIds;

        public Save(List<Long> itemIds) {
            this.itemIds = itemIds;
        }
    }
}
