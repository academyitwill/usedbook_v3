package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.CommentDTO;
import com.lotu_us.usedbook.domain.entity.Comment;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.repository.CommentRepository;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.repository.MemberRepository;
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
class CommentServiceTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private CommentService commentService;
    @Autowired private CommentRepository commentRepository;

    private Member member;
    private Item item;
    private PrincipalDetails principalDetails;

    @BeforeEach
    void before(){
        member = memberRepository.save(new Member("123@123.com", "123"));
        principalDetails = new PrincipalDetails(member);
        item = itemRepository.save(new Item(member, "제목22"));
    }


    @Test
    @DisplayName("댓글 작성 성공")
    void write_Success(){
        //given
        CommentDTO.Write commentDTO = CommentDTO.Write.builder()
                .depth(0).content("댓글내용").build();

        //when
        CommentDTO.Response write = commentService.write(principalDetails, item.getId(), commentDTO);

        //then
        Comment comment = commentRepository.findById(write.getId()).orElse(null);
        Assertions.assertThat(comment.getContent()).isEqualTo(commentDTO.getContent());
        Assertions.assertThat(item.getComments().size()).isEqualTo(1);
        Assertions.assertThat(item.getCommentCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 작성 실패 - 회원이 아닌 경우")
    void write_Fail_Writer_Is_Not_Member(){
        //given
        CommentDTO.Write commentDTO = CommentDTO.Write.builder()
                .depth(0).content("댓글내용").build();

        //when
        principalDetails = null;

        //then
        CustomException customException = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            commentService.write(principalDetails, item.getId(), commentDTO);
        });
        org.junit.jupiter.api.Assertions.assertEquals(customException.getErrorCode(), ErrorCode.ONLY_MEMBER);
    }

    @Test
    @DisplayName("댓글 작성 실패 - 상품이 존재하지 않는 경우")
    void write_Fail_Not_Exist_Item(){
        //given
        CommentDTO.Write commentDTO = CommentDTO.Write.builder()
                .depth(0).content("댓글내용").build();

        //when
        itemRepository.deleteById(item.getId());

        //then
        CustomException customException = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            commentService.write(principalDetails, item.getId(), commentDTO);
        });
        org.junit.jupiter.api.Assertions.assertEquals(customException.getErrorCode(), ErrorCode.ID_NOT_FOUND);
    }

    @Test
    @DisplayName("댓글 리스트 조회 성공")
    void list_Success(){
        //given
        CommentDTO.Write commentDTO1 = CommentDTO.Write.builder().depth(0).content("댓글내용1").build();
        CommentDTO.Write commentDTO2 = CommentDTO.Write.builder().depth(1).content("댓글내용2").build();
        commentService.write(principalDetails, item.getId(), commentDTO1);
        commentService.write(principalDetails, item.getId(), commentDTO2);

        //when
        List<CommentDTO.Response> responseList = commentService.list(item.getId());

        //then
        Assertions.assertThat(responseList.size()).isEqualTo(item.getCommentCount());
        Assertions.assertThat(responseList.get(0).getContent()).isEqualTo(commentDTO1.getContent());
        Assertions.assertThat(responseList.get(1).getContent()).isEqualTo(commentDTO2.getContent());
    }

    //댓글 리스트 조회 실패 : 상품 아이디가 없을 때. ID_NOT_FOUND

    @Test
    @DisplayName("댓글 수정 성공")
    void edit_Success(){
        //given
        CommentDTO.Write commentDTO = CommentDTO.Write.builder().depth(0).content("댓글내용1").build();
        CommentDTO.Response write = commentService.write(principalDetails, item.getId(), commentDTO);

        //when
        CommentDTO.Edit editDTO = CommentDTO.Edit.builder().content("댓글내용수정").build();
        commentService.edit(principalDetails, write.getId(), editDTO);

        //then
        Comment comment = commentRepository.findById(write.getId()).orElse(null);
        Item item = itemRepository.findById(this.item.getId()).orElse(null);

        Assertions.assertThat(editDTO.getContent()).isEqualTo(comment.getContent());
        Assertions.assertThat(editDTO.getContent()).isEqualTo(item.getComments().get(0).getContent());
    }

    @Test
    @DisplayName("댓글 수정 실패 - 수정하는 사람이 회원이 아닌 경우")
    void edit_Fail_Not_Member(){
        //given
        CommentDTO.Write commentDTO = CommentDTO.Write.builder().depth(0).content("댓글내용1").build();
        CommentDTO.Response write = commentService.write(principalDetails, item.getId(), commentDTO);

        //when
        CommentDTO.Edit editDTO = CommentDTO.Edit.builder().content("댓글내용수정").build();
        principalDetails = null;

        //then
        CustomException customException = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            commentService.edit(principalDetails, write.getId(), editDTO);
        });
        org.junit.jupiter.api.Assertions.assertEquals(customException.getErrorCode(), ErrorCode.ONLY_MEMBER);
    }

    @Test
    @DisplayName("댓글 수정 실패 - 수정하는 사람이 작성자가 아닌 경우")
    void edit_Fail_Editor_Is_Not_Writer(){
        //given
        CommentDTO.Write commentDTO = CommentDTO.Write.builder().depth(0).content("댓글내용1").build();
        CommentDTO.Response write = commentService.write(principalDetails, item.getId(), commentDTO);

        //when
        CommentDTO.Edit editDTO = CommentDTO.Edit.builder().content("댓글내용수정").build();
        principalDetails = new PrincipalDetails(new Member("1234@1234.com", "1234"));

        //then
        CustomException customException = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            commentService.edit(principalDetails, write.getId(), editDTO);
        });
        org.junit.jupiter.api.Assertions.assertEquals(customException.getErrorCode(), ErrorCode.EDIT_ACCESS_DENIED);
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void delete_Success(){
        //given
        CommentDTO.Write commentDTO1 = CommentDTO.Write.builder().depth(0).content("댓글내용1").build();
        CommentDTO.Response write1 = commentService.write(principalDetails, item.getId(), commentDTO1);

        //when
        commentService.delete(principalDetails, item.getId(), write1.getId());

        //then
        Comment comment = commentRepository.findById(write1.getId()).orElse(null);
        Assertions.assertThat(comment.isViewStatus()).isFalse();
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 삭제하는 자가 회원이 아닌 경우")
    void delete_Fail_Not_Member(){
        //given
        CommentDTO.Write commentDTO1 = CommentDTO.Write.builder().depth(0).content("댓글내용1").build();
        CommentDTO.Response write = commentService.write(principalDetails, item.getId(), commentDTO1);

        //when
        principalDetails = null;

        //then
        CustomException customException = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            commentService.delete(principalDetails, item.getId(), write.getId());
        });
        org.junit.jupiter.api.Assertions.assertEquals(customException.getErrorCode(), ErrorCode.ONLY_MEMBER);
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 삭제하는 자가 작성자가 아닌 경우")
    void delete_Fail_Deleter_Is_Not_Writer(){
        //given
        CommentDTO.Write commentDTO1 = CommentDTO.Write.builder().depth(0).content("댓글내용1").build();
        CommentDTO.Response write = commentService.write(principalDetails, item.getId(), commentDTO1);

        //when
        principalDetails = new PrincipalDetails(new Member("1234@1234.com", "1234"));

        //then
        CustomException customException = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            commentService.delete(principalDetails, item.getId(), write.getId());
        });
        org.junit.jupiter.api.Assertions.assertEquals(customException.getErrorCode(), ErrorCode.DELETE_ACCESS_DENIED);
    }

}