package com.lotu_us.usedbook.domain.dto;

import com.lotu_us.usedbook.domain.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;

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

    @Getter
    @ToString
    public static class Response{
        private Long id;
        private String writer;
        private String content;
        private int depth;
        private String createTime;
    }

    public static CommentDTO.Response entityToDTOResponse(Comment comment) {
        CommentDTO.Response response = new CommentDTO.Response();
        response.id = comment.getId();
        response.writer = comment.getWriter().getNickname();
        response.content = comment.getContent();
        response.depth = comment.getDepth();
        response.createTime = comment.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return response;
    }
}
