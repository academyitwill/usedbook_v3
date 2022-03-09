package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.OrderBasketDTO;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.domain.entity.OrderBasket;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.repository.MemberRepository;
import com.lotu_us.usedbook.repository.OrderBasketRepository;
import com.lotu_us.usedbook.util.exception.CustomException;
import com.lotu_us.usedbook.util.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class OrderBasketServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private OrderBasketService orderBasketService;
    @Autowired private OrderBasketRepository orderBasketRepository;

    private Member member;
    private Item item;
    private PrincipalDetails principalDetails;

    @BeforeEach
    void before(){
        member = memberRepository.save(new Member("123@123.com", "123"));
        item = itemRepository.save(new Item(member, "제목1"));
        principalDetails = new PrincipalDetails(member);

//        ItemDTO.Write itemDTO = ItemDTO.Write.builder()
//                .title("제목").category(Category.CARTOON).price(1000).stock(10).content("내용입니당").build();
//        fileList.add(new MockMultipartFile("file1", "file1.jpg", MediaType.IMAGE_JPEG_VALUE, "hello".getBytes(StandardCharsets.UTF_8)));
//        Long writeId = itemService.write(member, itemDTO, fileList);
        System.out.println("===============================================================================================================================");
    }

    @Test
    @DisplayName("장바구니 담기 성공")
    void basket_save_success(){
        Long orderBasketId = orderBasketService.save(principalDetails, item.getId(), 3);

        OrderBasket orderBasket = orderBasketRepository.findById(orderBasketId).orElse(null);
        Assertions.assertThat(orderBasket.getId()).isEqualTo(orderBasketId);
        Assertions.assertThat(orderBasket.getItem().getTitle()).isEqualTo(item.getTitle());
    }

    @Test
    @DisplayName("장바구니 담기 실패 - 이미 담은 상품")
    void basket_save_fail_already_save_item(){
        Long orderBasketId = orderBasketService.save(principalDetails, item.getId(), 3);

        CustomException customException = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            orderBasketService.save(principalDetails, item.getId(), 5);
        });

        org.junit.jupiter.api.Assertions.assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_SAVED_BASKET);
    }

    @Test
    @DisplayName("장바구니 수량 수정 성공")
    void basket_update_count_success(){
        Long orderBasketId = orderBasketService.save(principalDetails, item.getId(), 3);
        orderBasketService.update(principalDetails, item.getId(), 5);

        OrderBasket orderBasket = orderBasketRepository.findById(orderBasketId).orElse(null);
        Assertions.assertThat(orderBasket.getCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("장바구니 상품 삭제 성공")
    void basket_delete_item_success(){
        Long orderBasketId = orderBasketService.save(principalDetails, item.getId(), 3);
        orderBasketService.delete(principalDetails, item.getId());

        OrderBasket orderBasket = orderBasketRepository.findById(orderBasketId).orElse(null);
        Assertions.assertThat(orderBasket).isNull();
    }

    @Test
    @DisplayName("장바구니 상품 리스트보기")
    void basket_item_list(){
        Long orderBasketId = orderBasketService.save(principalDetails, item.getId(), 3);
        Item item2 = itemRepository.save(new Item(member, "제목2"));
        Long orderBasketId2 = orderBasketService.save(principalDetails, item2.getId(), 5);

        List<OrderBasketDTO.Response> list = orderBasketService.list(principalDetails);
        Assertions.assertThat(list.size()).isEqualTo(2);
        Assertions.assertThat(list.get(0).getCount()).isEqualTo(3);
        Assertions.assertThat(list.get(1).getCount()).isEqualTo(5);
    }

}
