package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.ItemDTO;
import com.lotu_us.usedbook.domain.dto.LikeItemDTO;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.LikeItem;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.repository.LikeItemRepository;
import com.lotu_us.usedbook.util.exception.CustomException;
import com.lotu_us.usedbook.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeItemService {
    private final ItemRepository itemRepository;
    private final LikeItemRepository likeItemRepository;

    /**
     * 관심상품 등록
     * @param principalDetails
     * @param itemId
     */
    public Long like(PrincipalDetails principalDetails, Long itemId) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
            new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        LikeItem likeItem = LikeItem.builder()
                .member(principalDetails.getMember())
                .item(item).build();

        LikeItem save = likeItemRepository.save(likeItem);
        item.addLikeCount(item.getLikeCount());

        return save.getId();
    }

    /**
     * 관심상품 해제
     * @param principalDetails
     * @param itemId
     */
    public void disLike(PrincipalDetails principalDetails, Long itemId) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        Long memberId = principalDetails.getMember().getId();
        likeItemRepository.deleteByMemberIdAndItemId(memberId, itemId);
        item.removeLikeCount(item.getLikeCount());
    }

    /**
     * 관심상품 리스트 조회
     * @param principalDetails
     * @return
     */
    public PageImpl<ItemDTO.Response> list(PrincipalDetails principalDetails, Pageable pageable) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        Long memberId = principalDetails.getMember().getId();
        Page<LikeItem> byMemberId = likeItemRepository.findByMemberId(memberId, pageable);
        List<ItemDTO.Response> list = byMemberId.getContent().stream()
                .map(likeItem -> LikeItemDTO.entityToDTO(likeItem))
                .collect(Collectors.toList());

        PageImpl<ItemDTO.Response> responses = new PageImpl<>(list, byMemberId.getPageable(), byMemberId.getTotalElements());

        return responses;
    }

    /**
     * 관심상품 등록여부 조회
     */
    public boolean isLiked(Long memberId, Long itemId){
        LikeItem likeItem = likeItemRepository.findByMemberIdAndItemId(memberId, itemId).orElse(null);
        if(likeItem == null){
            return false;
        }
        return true;
    }

}
