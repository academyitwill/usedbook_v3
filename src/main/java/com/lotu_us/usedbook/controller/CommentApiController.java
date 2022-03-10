package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.CommentDTO;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.service.CommentService;
import com.lotu_us.usedbook.util.aop.ReturnBindingResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentApiController {
    private final CommentService commentService;

    /**
     * 댓글 작성
     */
    @PostMapping("/item/{itemId}/comment")
    @ReturnBindingResultError
    public ResponseEntity write(
            @PathVariable Long itemId,
            @Validated @RequestBody CommentDTO.Write commentDTO,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        CommentDTO.Response write = commentService.write(principalDetails, itemId, commentDTO);
        return ResponseEntity.status(HttpStatus.OK).body(write);
    }

    /**
     * 댓글 리스트 보기
     */
    @GetMapping("/item/{itemId}/comment/list")
    public ResponseEntity list(@PathVariable Long itemId){
        List<CommentDTO.Response> list = commentService.list(itemId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/item/{itemId}/comment/{commentId}")
    public ResponseEntity edit(
            @PathVariable Long itemId,
            @PathVariable Long commentId,
            @Validated @RequestBody CommentDTO.Edit commentDTO,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        commentService.edit(principalDetails, commentId, commentDTO);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/item/{itemId}/comment/{commentId}")
    public ResponseEntity delete(
            @PathVariable Long itemId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        commentService.delete(principalDetails, itemId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 대시보드 내가 쓴 댓글 리스트
     */
    @GetMapping("/comment/dashboard")
    public ResponseEntity mylist(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault(size=10, sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Member member = principalDetails.getMember();
        PageImpl<CommentDTO.DashboardRes> dashboardRes = commentService.dashboardList(member, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(dashboardRes);
    }

}
