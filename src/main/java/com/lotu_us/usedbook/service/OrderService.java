package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.OrderDTO;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.Order;
import com.lotu_us.usedbook.domain.entity.OrderItem;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.repository.OrderRepository;
import com.lotu_us.usedbook.util.exception.CustomException;
import com.lotu_us.usedbook.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

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
