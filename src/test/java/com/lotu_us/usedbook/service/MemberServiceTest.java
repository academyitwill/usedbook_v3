package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.domain.dto.MemberDTO;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.repository.MemberRepository;
import com.lotu_us.usedbook.util.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("form 회원가입 성공")
    void join_Success(){
        //given
        MemberDTO.Join memberDTO = MemberDTO.Join.builder()
                .nickname("12").password("12").email("12@12").build();

        //when
        Long memberId = memberService.join(memberDTO);

        //then
        Assertions.assertThat(memberId).isNotNull();
        Member byEmail = memberRepository.findByEmail("12@12").orElse(null);
        Assertions.assertThat(byEmail.getNickname()).isEqualTo("12");
    }

    @Test
    @DisplayName("form 회원가입 실패 - 중복되는 이메일")
    void join_Fail_Email_Duplicated(){
        //given
        MemberDTO.Join memberDTO = MemberDTO.Join.builder()
                .nickname("12").password("12").email("12@12").build();
        Long memberId = memberService.join(memberDTO);

        //when
        MemberDTO.Join memberDTO2 = MemberDTO.Join.builder()
                .nickname("12").password("12").email("12@12").build();

        //then
        org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            Long memberId2 = memberService.join(memberDTO2);
        });
    }

    @Test
    @DisplayName("form 회원가입 실패 - 중복되는 닉네임")
    void join_Fail_Nickname_Duplicated(){
        //given
        MemberDTO.Join memberDTO = MemberDTO.Join.builder()
                .nickname("12").password("12").email("12@12").build();
        Long memberId = memberService.join(memberDTO);

        //when
        MemberDTO.Join memberDTO2 = MemberDTO.Join.builder()
                .nickname("12").password("12").email("13@13").build();

        //then
        org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            Long memberId2 = memberService.join(memberDTO2);
        });
    }

    //=========================================================================

    @Test
    @DisplayName("닉네임 수정 성공")
    void nickname_Update_Success(){
        //given
        MemberDTO.Join memberDTO = MemberDTO.Join.builder()
                .nickname("12").password("12").email("12@12").build();
        Long memberId = memberService.join(memberDTO);

        //when
        memberService.updateNickname(memberId, "12333");

        //then
        Member byEmail = memberRepository.findByEmail("12@12").orElse(null);
        Assertions.assertThat(byEmail.getNickname()).isEqualTo("12333");
    }

    @Test
    @DisplayName("닉네임 수정 실패 - 없는 회원번호")
    void nickname_Update_Fail_ID_NOT_FOUND(){
        //given

        //when

        //then
        try{
            memberService.updateNickname(0L, "12333");
            fail();
        }catch (Exception e){
        }
    }

    @Test
    @DisplayName("닉네임 수정 실패 - 기존과 일치하는 닉네임")
    void nickname_Update_Fail_Nickname_Equals_Previous(){
        //given
        MemberDTO.Join memberDTO = MemberDTO.Join.builder()
                .nickname("12").password("12").email("12@12").build();
        Long memberId = memberService.join(memberDTO);

        //when

        //then
        org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            memberService.updateNickname(memberId, "12");
        });

    }
    //=========================================================================

    @Test
    @DisplayName("비밀번호 수정 성공")
    void password_Update_Success(){
        //given
        MemberDTO.Join memberDTO = MemberDTO.Join.builder()
                .nickname("12").password("12").email("12@12").build();
        Long memberId = memberService.join(memberDTO);

        //when //then
        MemberDTO.UpdatePassword updateDTO = new MemberDTO.UpdatePassword("12", "133");
        memberService.updatePassword(memberId, updateDTO);
    }

    @Test
    @DisplayName("비밀번호 수정 실패 - DB와 다른 기존 비밀번호")
    void password_Update_Fail_oldPassword_notequal(){
        //given
        MemberDTO.Join memberDTO = MemberDTO.Join.builder()
                .nickname("12").password("12").email("12@12").build();
        Long memberId = memberService.join(memberDTO);

        //when
        MemberDTO.UpdatePassword updateDTO = new MemberDTO.UpdatePassword("wpej@13423452", "12");

        //then
        org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            memberService.updatePassword(memberId, updateDTO);
        });
    }

    @Test
    @DisplayName("비밀번호 수정 실패 - 기존 비밀번호와 새 비밀번호가 일치")
    void password_Update_Fail_oldPassword_equal_newPassword(){
        //given
        MemberDTO.Join memberDTO = MemberDTO.Join.builder()
                .nickname("12").password("12").email("12@12").build();
        Long memberId = memberService.join(memberDTO);

        //when
        MemberDTO.UpdatePassword updateDTO = new MemberDTO.UpdatePassword("12", "12");

        //then
        org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            memberService.updatePassword(memberId, updateDTO);
        });
    }
    //=========================================================================

    @Test
    @DisplayName("회원 조회 성공")
    void detail_Success(){
        //given
        MemberDTO.Join memberDTO = MemberDTO.Join.builder()
                .nickname("12").password("12").email("12@12").build();
        Long memberId = memberService.join(memberDTO);

        //when
        MemberDTO.Response detail = memberService.detail(memberId);

        //then
        System.out.println(detail);
        Assertions.assertThat(detail.getEmail()).isEqualTo("12@12");
    }
    //=========================================================================

}