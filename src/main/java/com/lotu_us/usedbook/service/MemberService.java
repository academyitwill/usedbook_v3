package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.domain.dto.MemberDTO;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.repository.MemberRepository;
import com.lotu_us.usedbook.util.exception.ErrorCode;
import com.lotu_us.usedbook.util.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long join(MemberDTO.Join memberDTO) {
        String encodePassword = bCryptPasswordEncoder.encode(memberDTO.getPassword());

        Member member = Member.JoinForm()
                .email(memberDTO.getEmail())
                .nickname(memberDTO.getNickname())
                .password(encodePassword).build();

        memberRepository.save(member);
        return member.getId();
    }


    public void updateNickname(Long memberId, String updateNickname) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw CustomException.globalError(ErrorCode.ID_NOT_FOUND);
        });

        if(member.getNickname().equals(updateNickname)){
            throw CustomException.localError()
                    .causeTarget("nickname")
                    .code("nickname.equal.previous")
                    .message("닉네임이 이전과 같습니다.")
                    .httpStatus(HttpStatus.BAD_REQUEST).build();
        }

        member.changeNickname(updateNickname);
    }
}
