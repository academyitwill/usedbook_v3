package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.ItemDTO;
import com.lotu_us.usedbook.domain.entity.LikeItem;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.domain.enums.Category;
import com.lotu_us.usedbook.repository.LikeItemRepository;
import com.lotu_us.usedbook.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class LikeItemServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private ItemService itemService;
    @Autowired private LikeItemService likeItemService;
    @Autowired private LikeItemRepository likeItemRepository;

    private Member member;
    private Long itemId;
    private PrincipalDetails principalDetails;
    private List<MultipartFile> fileList = new ArrayList<>();

    @BeforeEach
    void before(){
        member = memberRepository.save(new Member("123@123.com", "123"));
        principalDetails = new PrincipalDetails(member);

        ItemDTO.Write itemDTO = ItemDTO.Write.builder()
                .title("제목").category(Category.CARTOON).price(1000).stock(10).content("내용입니당").build();
        fileList.add(new MockMultipartFile("file1", "file1.jpg", MediaType.IMAGE_JPEG_VALUE, "hello".getBytes(StandardCharsets.UTF_8)));
        itemId = itemService.write(member, itemDTO, fileList);
    }

    @Test
    @DisplayName("관심상품 등록 성공")
    void item_Like_Success(){
        //given member, item

        //when
        Long likeItemId = likeItemService.like(principalDetails, itemId);

        //then
        LikeItem likeItem = likeItemRepository.findById(likeItemId).orElse(null);
        Assertions.assertThat(likeItem.getItem().getId()).isEqualTo(itemId);
        Assertions.assertThat(likeItem.getMember().getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("관심상품 해제 성공")
    void item_Dislike_Success(){
        //given member, item
        Long likeItemId = likeItemService.like(principalDetails, itemId);

        //when
        likeItemService.disLike(principalDetails, itemId);

        //then
        LikeItem likeItem = likeItemRepository.findById(likeItemId).orElse(null);
        Assertions.assertThat(likeItem).isNull();
    }

    @Test
    @DisplayName("관심상품 리스트 조회 성공")
    void item_Like_List_Success(){
        //given member, item
        Long likeItemId1 = likeItemService.like(principalDetails, itemId);

        //when
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<ItemDTO.Response> list = likeItemService.list(principalDetails, pageable);

        //then
        Assertions.assertThat(list.getContent().size()).isEqualTo(1);
        Assertions.assertThat(list.getContent().get(0).getSeller()).isEqualTo(member.getNickname());
        Assertions.assertThat(list.getContent().get(0).getId()).isEqualTo(itemId);
    }
}
