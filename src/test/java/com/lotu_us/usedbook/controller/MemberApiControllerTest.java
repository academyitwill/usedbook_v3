package com.lotu_us.usedbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotu_us.usedbook.domain.dto.MemberDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberApiControllerTest {
    @Autowired private MockMvc mock;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("form회원가입 성공")
    void join_Success() throws Exception {
        //given
        MemberDTO.Join memberDTO = MemberDTO.Join.builder()
                .nickname("12").password("pwd123@").email("12@12.com").build();

        String str = objectMapper.writeValueAsString(memberDTO);

        //when //then
        mock.perform(
                post("/api/member").content(str)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andDo(print());
    }

    @Test
    @DisplayName("form회원가입 실패 - validation 조건 불만족")
    void join_Fail() throws Exception {
        //given
        MemberDTO.Join blankMemberDTO = MemberDTO.Join.builder()
                .nickname("").password("").email("").build();

        String str = objectMapper.writeValueAsString(blankMemberDTO);

        //when //then
        mock.perform(
                post("/api/member").content(str)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest())
//        .andExpect(jsonPath("$.[?(@.cause == 'nickname')]message").isNotEmpty())
//        .andExpect(jsonPath("$.[?(@.cause == 'password')]message").isNotEmpty())
        .andExpect(jsonPath("$.[?(@.cause == 'email')]message").isNotEmpty())
        .andDo(print());

        //email을 가장 먼저 검증하고 결과에 맞지않으면 return함
    }
}