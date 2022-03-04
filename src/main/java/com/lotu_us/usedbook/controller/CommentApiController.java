package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.CommentDTO;
import com.lotu_us.usedbook.service.CommentService;
import com.lotu_us.usedbook.util.aop.ReturnBindingResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentApiController {
    private final CommentService commentService;

    /**
     * 댓글 작성
     * @param itemId
     * @param commentDTO
     * @param principalDetails
     */
    @PostMapping("/{itemId}")
    @ReturnBindingResultError
    public ResponseEntity write(
            @PathVariable Long itemId,
            @Validated @RequestBody CommentDTO.Write commentDTO,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        commentService.write(principalDetails, itemId, commentDTO);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 댓글 리스트 보기
     * @param itemId
     * @return
     */
    @GetMapping("/{itemId}/list")
    public ResponseEntity list(@PathVariable Long itemId){
        List<CommentDTO.Response> list = commentService.list(itemId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

}
