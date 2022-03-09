package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.OrderBasketDTO;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.domain.entity.OrderBasket;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.repository.OrderBasketRepository;
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
public class OrderBasketService {
    private final ItemRepository itemRepository;
    private final OrderBasketRepository orderBasketRepository;

    /**
     * 장바구니에 상품 저장
     * @param principalDetails
     * @param itemId
     * @param count
     * @exception : 회원이 아닌 경우 ErrorCode.ONLY_MEMBER;
     * @exception : 상품이 존재하지 않는다면 ErrorCode.ID_NOT_FOUND
     * @exception : 이미 담은 상품이라면 ErrorCode.ALREADY_SAVED_BASKET
     */
    public Long save(PrincipalDetails principalDetails, Long itemId, int count) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        Member member = principalDetails.getMember();
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
            new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        orderBasketRepository.findByMemberIdAndItemId(member.getId(), itemId).ifPresent((orderBasket) -> {
            throw new CustomException(ErrorCode.ALREADY_SAVED_BASKET);
        });

        OrderBasket orderBasket = OrderBasket.builder()
                .member(member)
                .item(item)
                .count(count).build();

        OrderBasket save = orderBasketRepository.save(orderBasket);
        return save.getId();
    }

    /**
     * 장바구니 상품 수량 수정
     * @param principalDetails
     * @param itemId
     * @param count
     * @exception : 회원이 아닌 경우 ErrorCode.ONLY_MEMBER;
     * @exception : 상품이 존재하지 않는다면 ErrorCode.ID_NOT_FOUND
     */
    public void update(PrincipalDetails principalDetails, Long itemId, int count) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        Member member = principalDetails.getMember();
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        OrderBasket orderBasket = orderBasketRepository.findByMemberIdAndItemId(member.getId(), itemId).orElse(null);
        orderBasket.changeCount(count);
    }

    /**
     * 장바구니 상품 삭제
     * @param principalDetails
     * @param itemId
     * @exception : 회원이 아닌 경우 ErrorCode.ONLY_MEMBER;
     */
    public void delete(PrincipalDetails principalDetails, Long itemId) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        Member member = principalDetails.getMember();

        orderBasketRepository.deleteByMemberIdAndItemId(member.getId(), itemId);
    }

    /**
     * 장바구니 상품 리스트보기
     * @param principalDetails
     * @exception : 회원이 아닌 경우 ErrorCode.ONLY_MEMBER;
     */
    public List<OrderBasketDTO.Response> list(PrincipalDetails principalDetails) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        Member member = principalDetails.getMember();
        List<OrderBasketDTO.Response> collect = orderBasketRepository.findAllByMemberId(member.getId()).stream()
                .map(orderBasket -> OrderBasketDTO.entityToDto(orderBasket))
                .collect(Collectors.toList());

        return collect;
    }
}
