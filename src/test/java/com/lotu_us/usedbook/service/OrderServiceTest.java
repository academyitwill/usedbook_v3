package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.OrderDTO;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.domain.entity.Order;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.repository.MemberRepository;
import com.lotu_us.usedbook.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;

    private Member member;
    private Item item1;
    private Item item2;
    private PrincipalDetails principalDetails;
    private List<Long> itemList = new ArrayList<>();

    @BeforeEach
    void before(){
        member = memberRepository.save(new Member("123@123.com", "123"));
        item1 = itemRepository.save(new Item(member, "제목1"));
        item2 = itemRepository.save(new Item(member, "제목2"));
        principalDetails = new PrincipalDetails(member);

        itemList.add(item1.getId());
        itemList.add(item2.getId());
        System.out.println("===============================================================================================================================");
    }

    @Test
    @DisplayName("주문하기 성공")
    void order_save_success(){
        OrderDTO.Save orderDTO = new OrderDTO.Save(itemList);

        Long orderId = orderService.save(principalDetails, orderDTO);
        Order order = orderRepository.findById(orderId).orElse(null);
        Assertions.assertThat(order.getId()).isEqualTo(orderId);
        Assertions.assertThat(order.getOrderItems().size()).isEqualTo(itemList.size());
        Assertions.assertThat(order.getOrderItems().get(0).getItem().getTitle()).isEqualTo(item1.getTitle());
    }
}
