package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.ItemDTO;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.domain.enums.Category;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.repository.MemberRepository;
import com.lotu_us.usedbook.util.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.Map;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private ItemService itemService;
    @Autowired private ItemRepository itemRepository;

    private Member member;
    private PrincipalDetails principalDetails;
    private List<MultipartFile> fileList = new ArrayList<>();

    @BeforeEach
    void before(){
        member = memberRepository.save(new Member("123@123.com", "123"));
        principalDetails = new PrincipalDetails(member);
        fileList.add(new MockMultipartFile("file1", "file1.jpg", MediaType.IMAGE_JPEG_VALUE, "hello".getBytes(StandardCharsets.UTF_8)));
        fileList.add(new MockMultipartFile("file2", "file2.png", MediaType.IMAGE_PNG_VALUE, "hello2".getBytes(StandardCharsets.UTF_8)));
    }


    @Test
    @DisplayName("상품 저장 성공")
    void write_Success(){
        //given
        ItemDTO.Write itemDTO = ItemDTO.Write.builder()
                .title("제목").category(Category.CARTOON).price(1000).stock(10).content("내용입니당").build();

        //when
        Long itemId = itemService.write(member, itemDTO, fileList);

        //then
        Item item = itemRepository.findById(itemId).orElse(null);
        Assertions.assertThat(item.getSeller().getNickname()).isEqualTo(member.getNickname());
        Assertions.assertThat(item.getFiles().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품 상세보기 성공")
    void detail_Success(){
        //given
        ItemDTO.Write itemDTO = ItemDTO.Write.builder()
                .title("제목").category(Category.CARTOON).price(1000).stock(10).content("내용입니당").build();

        //when
        Long itemId = itemService.write(member, itemDTO, fileList);
        ItemDTO.Response detail = itemService.detail(itemId, principalDetails);

        //then
        Assertions.assertThat(itemDTO.getTitle()).isEqualTo(detail.getTitle());
        Assertions.assertThat(itemDTO.getPrice()).isEqualTo(detail.getPrice());
        Assertions.assertThat(itemDTO.getContent()).isEqualTo(detail.getContent());
    }

    @Test
    @DisplayName("상품 상세보기 실패 - 없는 상품아이디")
    void detail_Fail(){
        //given
        ItemDTO.Write itemDTO = ItemDTO.Write.builder()
                .title("제목").category(Category.CARTOON).price(1000).stock(10).content("내용입니당").build();
        Long itemId = itemService.write(member, itemDTO, fileList);

        //when
        //then
        org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            ItemDTO.Response detail = itemService.detail(0L, principalDetails);
        });
    }

    @Test
    @DisplayName("상품 상세보기 - 게시글의 작성자인 경우 수정 가능해야함")
    void detail_memberIsSeller(){
        //given
        ItemDTO.Write itemDTO = ItemDTO.Write.builder()
                .title("제목").category(Category.CARTOON).price(1000).stock(10).content("내용입니당").build();
        Long itemId = itemService.write(member, itemDTO, fileList);

        //when
        PrincipalDetails another = new PrincipalDetails(new Member("1234@1234.com", "1234"));
        ItemDTO.Response detail = itemService.detail(itemId, another);

        //then
        Assertions.assertThat(detail.getSeller()).isNotEqualTo("12@12.com");
        Assertions.assertThat(detail.isMemberIsSeller()).isFalse();
    }

    @Test
    @DisplayName("상품 수정 성공")
    void update_Success(){
        //given
        ItemDTO.Write itemDTO = ItemDTO.Write.builder()
                .title("제목").category(Category.CARTOON).price(1000).stock(10).content("내용입니당").build();
        Long itemId = itemService.write(member, itemDTO, fileList);

        //when
        ItemDTO.Write updateDTO = ItemDTO.Write.builder()
                .title("제목2").category(Category.HUMANITIES).price(1500).stock(5).content("내용입니당222").build();

        List<MultipartFile> plusFileList = new ArrayList<>();
        plusFileList.add(new MockMultipartFile("file3", "file3.jpg", MediaType.IMAGE_JPEG_VALUE, "hello3".getBytes(StandardCharsets.UTF_8)));
        List<String> removeFileList = null;

        itemService.edit(itemId, member, updateDTO, plusFileList, removeFileList);

        //then
        ItemDTO.Response detail = itemService.detail(itemId, principalDetails);
        Assertions.assertThat(detail.getTitle()).isEqualTo(updateDTO.getTitle());
        Assertions.assertThat(detail.getPrice()).isEqualTo(updateDTO.getPrice());
        Assertions.assertThat(detail.getContent()).isEqualTo(updateDTO.getContent());
        Assertions.assertThat(detail.getFiles().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void delete_Success(){
        //given
        ItemDTO.Write itemDTO = ItemDTO.Write.builder()
                .title("제목").category(Category.CARTOON).price(1000).stock(10).content("내용입니당").build();
        Long itemId = itemService.write(member, itemDTO, fileList);

        //when
        itemService.remove(itemId, member);

        //then
        org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            itemService.detail(itemId, principalDetails);
        });
    }

    @Test
    @DisplayName("상품 리스트 보기")
    void list(){
        //given
        String category = Category.NOVEL.toString().toLowerCase();
        String search = "title,h";
        Pageable pageable = PageRequest.of(1, 2, Sort.Direction.DESC, "createTime");

        //when
        Map list = itemService.list(category, search, pageable);

        //then
        Assertions.assertThat(list.get("category")).isEqualTo(Category.NOVEL.getValue());
    }


}