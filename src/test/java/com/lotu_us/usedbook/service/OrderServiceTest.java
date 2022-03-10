package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.OrderDTO;
import com.lotu_us.usedbook.domain.entity.Address;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.domain.entity.Order;
import com.lotu_us.usedbook.domain.enums.Payment;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.repository.MemberRepository;
import com.lotu_us.usedbook.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

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
    private OrderDTO.Save orderDTO = new OrderDTO.Save();

    @BeforeEach
    void before(){
        member = memberRepository.save(new Member("123@123.com", "123"));
        item1 = itemRepository.save(new Item(member, "제목1"));
        item2 = itemRepository.save(new Item(member, "제목2"));
        principalDetails = new PrincipalDetails(member);

        orderDTO.getOrderItemsList().add(new OrderDTO.OrderItems(item1.getId(), 3));
        orderDTO.getOrderItemsList().add(new OrderDTO.OrderItems(item2.getId(), 5));
        orderDTO.setAddress(new Address("12345", "도로명", "상세", "추가정보"));
        orderDTO.setPayment(Payment.CARD);
        System.out.println("===============================================================================================================================");
    }

    @Test
    @DisplayName("주문하기 성공")
    void order_save_success(){
        Long orderId = orderService.save(principalDetails, orderDTO);
        Order order = orderRepository.findById(orderId).orElse(null);
        Assertions.assertThat(order.getId()).isEqualTo(orderId);
        Assertions.assertThat(order.getOrderItems().size()).isEqualTo(orderDTO.getOrderItemsList().size());
        Assertions.assertThat(order.getOrderItems().get(0).getItem().getTitle()).isEqualTo(item1.getTitle());
    }


    @Test
    @DisplayName("주문 상세정보 보여주기 성공")
    void order_detail_success(){
        Long orderId = orderService.save(principalDetails, orderDTO);
        OrderDTO.Response response = orderService.detail(principalDetails, orderId);

        Assertions.assertThat(response.getOrderId()).isEqualTo(orderId);
        Assertions.assertThat(response.getOrderItems().get(0).getItemTitle()).isEqualTo(item1.getTitle());
        Assertions.assertThat(response.getOrderItems().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("주문 리스트 보여주기 성공")
    void order_list_success(){
        Long orderId = orderService.save(principalDetails, orderDTO);
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<OrderDTO.ResponseList> list = orderService.list(principalDetails, pageable);

        Assertions.assertThat(list.getContent().get(0).getOrderItems().size()).isEqualTo(orderDTO.getOrderItemsList().size());
        Assertions.assertThat(list.getContent().get(0).getOrderId()).isEqualTo(orderId);
    }

}
