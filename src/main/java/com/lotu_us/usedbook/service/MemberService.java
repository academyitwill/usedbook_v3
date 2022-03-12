package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.domain.dto.MemberDTO;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.repository.MemberRepository;
import com.lotu_us.usedbook.util.CustomMailSender;
import com.lotu_us.usedbook.util.exception.CustomException;
import com.lotu_us.usedbook.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomMailSender customMailSender;

    /**
     * form회원가입
     * @exception : 존재하는 이메일이라면 ErrorCode.EMAIL_DUPLICATED
     * @exception : 존재하는 닉네임이라면 ErrorCode.NICKNAME_DUPLICATED
     */
    public Long join(MemberDTO.Join memberDTO) {
        String encodePassword = bCryptPasswordEncoder.encode(memberDTO.getPassword());
        joinEmailDuplicatedCheck(memberDTO.getEmail());
        joinNicknameDuplicatedCheck(memberDTO.getNickname());

        Member member = Member.JoinForm()
                .email(memberDTO.getEmail())
                .nickname(memberDTO.getNickname())
                .password(encodePassword).build();

        memberRepository.save(member);
        return member.getId();
    }

    /**
     * form회원가입 - 이메일 중복체크
     * @exception : 존재하는 이메일이라면 ErrorCode.EMAIL_DUPLICATED
     */
    public boolean joinEmailDuplicatedCheck(String email) {
        memberRepository.findByEmail(email).ifPresent(m -> {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        });
        return true;
    }

    /**
     * form회원가입 - 닉네임 중복체크
     * @exception : 존재하는 닉네임이라면 ErrorCode.NICKNAME_DUPLICATED
     */
    public boolean joinNicknameDuplicatedCheck(String nickname) {
        memberRepository.findByNickname(nickname).ifPresent(m -> {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATED);
        });
        return true;
    }


    /**
     * 닉네임 수정
     * @param memberId
     * @param updateNickname
     * @exception : 일치하는 memberId 없으면 -> ErrorCode.ID_NOT_FOUND
     * @exception : 중복되는 닉네임이 있다면 -> ErrorCode.NICKNAME_DUPLICATED
     * @exception : 닉네임이 이전과 같다면 -> ErrorCode.NICKNAME_EQUAL_PREVIOUS
     */
    public void updateNickname(Long memberId, String updateNickname) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
            new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        memberRepository.findByNickname(updateNickname).ifPresent((member1) -> {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATED);
        });

        if(member.getNickname().equals(updateNickname)){
            throw new CustomException(ErrorCode.NICKNAME_EQUAL_PREVIOUS);
        }

        member.changeNickname(updateNickname);
    }

    /**
     * 패스워드 수정
     * @param memberId
     * @param memberDTO
     * @exception : 일치하는 memberId 없으면 -> ErrorCode.ID_NOT_FOUND
     * @exception : 기존 비밀번호와 DB 비밀번호가 불일치하면 -> ErrorCode.PASSWORD_NOT_EQUAL
     * @exception : 기존 비밀번호와 새 비밀번호가 일치하면 -> ErrorCode.PASSWORD_EQUAL_PREVIOUS
     */
    public void updatePassword(Long memberId, MemberDTO.UpdatePassword memberDTO) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
            new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        //DB에 저장된 비밀번호와 사용자가 입력한 기존 비밀번호 같아야 새 비밀번호로 변경 가능하다.
        if(! bCryptPasswordEncoder.matches(memberDTO.getOldPassword(), member.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_NOT_EQUAL);
        }

        //기존 비밀번호와 새 비밀번호가 같다면 변경하지 않는다.
        if(bCryptPasswordEncoder.matches(memberDTO.getNewPassword(), member.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_EQUAL_PREVIOUS);
        }

        String encodePassword = bCryptPasswordEncoder.encode(memberDTO.getNewPassword());
        member.changePassword(encodePassword);
    }


    /**
     * 회원조회
     * @param memberId
     * @exception : 일치하는 memberId 없으면 -> ErrorCode.ID_NOT_FOUND
     * @return MemberDTO.Response
     */
    public MemberDTO.Response detail(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        MemberDTO.Response response = MemberDTO.entityToDto(member);
        return response;
    }

    /**
     * 비밀번호 찾기
     * @param memberDTO
     * @exception : 일치하는 이메일 없으면 -> ErrorCode.EMAIL_NOT_FOUND
     * @exception : 이메일의 member와 닉네임이 다르면 -> ErrorCode.NICKNAME_NOT_EQUAL
     */
    public void findPassword(MemberDTO.findPassword memberDTO) {
        Member member = memberRepository.findByEmail(memberDTO.getEmail()).orElseThrow(() ->
                new CustomException(ErrorCode.EMAIL_NOT_FOUND)
        );

        if(! member.getNickname().equals(memberDTO.getNickname())){
            throw new CustomException(ErrorCode.NICKNAME_NOT_EQUAL);
        }

        //임시 비밀번호
        UUID uid = UUID.randomUUID();
        String tempPassword = uid.toString().substring(0,10) + "p2$";
        customMailSender.sendFindPasswordMail(memberDTO, tempPassword);

        tempPassword = bCryptPasswordEncoder.encode(tempPassword);
        member.changePassword(tempPassword);
    }
}
