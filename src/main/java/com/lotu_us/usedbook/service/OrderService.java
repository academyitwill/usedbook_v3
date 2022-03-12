package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.OrderBasketDTO;
import com.lotu_us.usedbook.domain.dto.OrderDTO;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.domain.entity.Order;
import com.lotu_us.usedbook.domain.entity.OrderItem;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.repository.OrderBasketRepository;
import com.lotu_us.usedbook.repository.OrderRepository;
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
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderBasketRepository orderBasketRepository;


    /**
     * 장바구니에서 넘어온 상품들 보여주기
     * @param itemIdList
     */
    public List<OrderBasketDTO.Response> load(List itemIdList) {

        List<OrderBasketDTO.Response> collect = orderBasketRepository.findAllByItemIdIn(itemIdList).stream()
                .map(orderBasket -> OrderBasketDTO.entityToDto(orderBasket))
                .collect(Collectors.toList());
        return collect;
    }


    /**
     * 주문하기
     * @param principalDetails
     * @param orderDTO
     */
    public Long save(PrincipalDetails principalDetails, OrderDTO.Save orderDTO) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        List<OrderItem> orderItemList = OrderItem.createList(itemRepository, orderDTO);
        //order에 createitem 주입
        Order order = Order.builder()
                .member(principalDetails.getMember())
                .orderItems(orderItemList)
                .address(orderDTO.getAddress())
                .payment(orderDTO.getPayment()).build();

        Order save = orderRepository.save(order);

        //주문한 상품ID는 장바구니에서 삭제
        removeOrderItemInBasket(principalDetails.getMember(), orderItemList);

        //해당 상품의 수량 감소
        //해당 상품의 수량이 0이면 상품 상태 "판매완료"로 변경
        minusItemStock(orderItemList);

        return save.getId();
    }


    /**
     * 주문 상세보기
     * @param principalDetails
     * @param orderId
     * @return
     */
    public OrderDTO.Response detail(PrincipalDetails principalDetails, Long orderId) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        Order order = orderRepository.findById(orderId).orElse(null);
        OrderDTO.Response response = OrderDTO.entityToDTO(order);
        return response;
    }

    /**
     * 주문 리스트
     * @param principalDetails
     * @param pageable
     * @return
     */
    public PageImpl<OrderDTO.ResponseList> list(PrincipalDetails principalDetails, Pageable pageable) {
        if(principalDetails == null){
            throw new CustomException(ErrorCode.ONLY_MEMBER);
        }

        Long memberId = principalDetails.getMember().getId();
        Page<Order> byMemberId = orderRepository.findByMemberId(memberId, pageable);
        List<OrderDTO.ResponseList> content = byMemberId.getContent().stream()
                .map(order -> OrderDTO.entityToDTOList(order))
                .collect(Collectors.toList());

        PageImpl<OrderDTO.ResponseList> list = new PageImpl<>(content, byMemberId.getPageable(), byMemberId.getTotalElements());

        return list;
    }


    /**
     * 주문한 상품ID는 장바구니에서 삭제
     */
    private void removeOrderItemInBasket(Member member, List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            orderBasketRepository.deleteByMemberIdAndItemId(member.getId(), orderItem.getItem().getId());
        }
    }

    /**
     * 해당 상품의 수량 감소
     * 해당 상품의 수량이 0이면 상품 상태 "판매완료"로 변경
     */
    private void minusItemStock(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            orderItem.getItem().minusStock(orderItem.getCount());
        }
    }
}
