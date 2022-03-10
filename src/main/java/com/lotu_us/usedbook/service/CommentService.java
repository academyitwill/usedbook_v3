package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.CommentDTO;
import com.lotu_us.usedbook.domain.entity.Comment;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.repository.CommentRepository;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.util.exception.CustomException;
import com.lotu_us.usedbook.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    /**
     * 댓글 작성
     * @param principalDetails
     * @param itemId
     * @param commentDTO
     * @exception : 회원이 아니라면 댓글 작성 불가 ErrorCode.ONLY_MEMBER
     * @exception : 없는 상품번호라면 ErrorCode.ID_NOT_FOUND (댓글 작성 중 삭제된 경우엔 댓글 못달게)
     * @return
     */
    public CommentDTO.Response write(PrincipalDetails principalDetails, Long itemId, CommentDTO.Write commentDTO) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            return new CustomException(ErrorCode.ID_NOT_FOUND);
        });

        Comment comment = Comment.builder()
                .writer(principalDetails.getMember())
                .item(item)
                .depth(commentDTO.getDepth())
                .parentId(commentDTO.getParentId())
                .content(commentDTO.getContent()).build();

        commentRepository.save(comment);
        item.getComments().add(comment);  //싱크 맞추기용
        item.addCommentCount(item.getCommentCount());

        return CommentDTO.entityToDTOResponse(comment);
    }

    /**
     * 댓글 리스트 보기
     * @param itemId
     * @exception : 상품ID가 존재하지 않으면 ErrorCode.ID_NOT_FOUND
     */
    public List<CommentDTO.Response> list(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        List<CommentDTO.Response> collect = item.getComments().stream()
                .map(comment -> CommentDTO.entityToDTOResponse(comment))
                .collect(Collectors.toList());

        return collect;
    }

    /**
     * 댓글 수정
     * @param principalDetails
     * @param commentId
     * @param editDTO
     * @exception : 수정하는 사람이 회원이 아니면 ONLY_MEMBER
     * @exception : 수정하는 사람이 댓글 작성자가 아니면 ErrorCode.EDIT_ACCESS_DENIED
     * commentId는 자신만 수정, 삭제할 수 있으니 타인이 접근할 수 없음. 따라서 반드시 존재함 -> exception 처리 안함
     */
    public void edit(PrincipalDetails principalDetails, Long commentId, CommentDTO.Edit editDTO) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        Comment comment = commentRepository.findById(commentId).orElse(null);
        String loginMember = principalDetails.getMember().getEmail();
        String commentWriter = comment.getWriter().getEmail();
        if(!loginMember.equals(commentWriter)){
            throw new CustomException(ErrorCode.EDIT_ACCESS_DENIED);
        }

        comment.changeContent(editDTO.getContent());
    }

    /**
     * 댓글 삭제
     * @param principalDetails
     * @param itemId
     * @param commentId
     * @exception : 수정하는 사람이 회원이 아니면 ONLY_MEMBER
     * @exception : 수정하는 사람이 댓글 작성자가 아니면 ErrorCode.EDIT_ACCESS_DENIED
     */
    public void delete(PrincipalDetails principalDetails, Long itemId, Long commentId) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        Comment comment = commentRepository.findById(commentId).orElse(null);
        String loginMember = principalDetails.getMember().getEmail();
        String commentWriter = comment.getWriter().getEmail();
        if(!loginMember.equals(commentWriter)){
            throw new CustomException(ErrorCode.DELETE_ACCESS_DENIED);
        }

        //commentRepository.deleteById(commentId);

        //Item item = itemRepository.findById(itemId).orElse(null);
        //item.getComments().removeIf(comment1 -> comment1.getId().equals(commentId) ); //싱크 맞추기용
        //item.removeCommentCount(item.getCommentCount());

        //실제 삭제하진않고 viewStatus를 false로 처리하여 view에서도 삭제된 댓글을 알 수 있게처리한다.
        comment.changeViewStatusFalse();
    }

    /**
     * 대시보드 내가 쓴 댓글 리스트
     * @param pageable
     * @return
     */
    public PageImpl<CommentDTO.DashboardRes> dashboardList(Member member, Pageable pageable) {
        Page<Comment> byMemberId = commentRepository.findByMemberId(member.getId(), pageable);
        List<CommentDTO.DashboardRes> list = byMemberId.getContent().stream()
                .map(comment -> CommentDTO.entityToDashboardRes(comment))
                .collect(Collectors.toList());

        PageImpl<CommentDTO.DashboardRes> dashboardRes = new PageImpl<>(list, byMemberId.getPageable(), byMemberId.getTotalElements());
        System.out.println(dashboardRes);

        return dashboardRes;
    }
}
