package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.OrderBasketDTO;
import com.lotu_us.usedbook.domain.dto.OrderDTO;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.Order;
import com.lotu_us.usedbook.domain.entity.OrderItem;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.repository.OrderBasketRepository;
import com.lotu_us.usedbook.repository.OrderRepository;
import com.lotu_us.usedbook.util.exception.CustomException;
import com.lotu_us.usedbook.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
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

        List<Item> itemList = itemRepository.findAllByIdIn(orderDTO.getItemIds());
        List<OrderItem> orderItemList = OrderItem.createList(itemList);
        Order order = Order.builder()
                .member(principalDetails.getMember())
                .orderItems(orderItemList).build();

        Order save = orderRepository.save(order);
        return save.getId();
    }
}
