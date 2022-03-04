package com.lotu_us.usedbook.domain.dto;

import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentDTO {

    @Getter
    @ToString
    public static class Write{
        @NotNull
        private int depth;
        @NotBlank
        private String content;

        @Builder
        public Write(int depth, String content) {
            this.depth = depth;
            this.content = content;
        }
    }
}
