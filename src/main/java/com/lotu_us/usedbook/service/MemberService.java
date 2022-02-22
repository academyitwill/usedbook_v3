package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.domain.dto.MemberDTO;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(MemberDTO.Join memberDTO) {

        String encodePassword = bCryptPasswordEncoder.encode(memberDTO.getPassword());

        Member member = Member.JoinForm()
                .email(memberDTO.getEmail())
                .nickname(memberDTO.getNickname())
                .password(encodePassword).build();

        memberRepository.save(member);
    }
}
