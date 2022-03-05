package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.LikeItemDTO;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.LikeItem;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.repository.LikeItemRepository;
import com.lotu_us.usedbook.util.exception.CustomException;
import com.lotu_us.usedbook.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
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
    }

    /**
     * 관심상품 리스트 조회
     * @param principalDetails
     * @return
     */
    public List<LikeItemDTO.Response> list(PrincipalDetails principalDetails) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        Long memberId = principalDetails.getMember().getId();
        List<LikeItemDTO.Response> collect = likeItemRepository.findAllByMemberId(memberId).stream()
                .map(likeItem -> LikeItemDTO.entityToDTO(likeItem))
                .collect(Collectors.toList());

        return collect;
    }

}
