package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.domain.entity.OrderBasket;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.repository.MemberRepository;
import com.lotu_us.usedbook.repository.OrderBasketRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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




}
