package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.CommentDTO;
import com.lotu_us.usedbook.domain.entity.Comment;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.repository.CommentRepository;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.util.exception.CustomException;
import com.lotu_us.usedbook.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
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
     */
    public Long write(PrincipalDetails principalDetails, Long itemId, CommentDTO.Write commentDTO) {
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
                .content(commentDTO.getContent()).build();

        Comment save = commentRepository.save(comment);
        item.getComments().add(comment);
        item.addCommentCount(item.getCommentCount());

        return save.getId();
    }
}
