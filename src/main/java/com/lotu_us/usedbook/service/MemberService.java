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

    /**
     * form회원가입
     */
    public Long join(MemberDTO.Join memberDTO) {
        String encodePassword = bCryptPasswordEncoder.encode(memberDTO.getPassword());

        Member member = Member.JoinForm()
                .email(memberDTO.getEmail())
                .nickname(memberDTO.getNickname())
                .password(encodePassword).build();

        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 닉네임 수정
     * @param memberId
     * @param updateNickname
     * @exception : 일치하는 memberId 없으면 -> CustomException.globalError(ErrorCode.ID_NOT_FOUND)
     * @exception : 닉네임이 이전과 같다면 -> CustomException.localError
     */
    public void updateNickname(Long memberId, String updateNickname) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                CustomException.globalError(ErrorCode.ID_NOT_FOUND)
        );

        if(member.getNickname().equals(updateNickname)){
            throw CustomException.localError()
                    .cause("nickname")
                    .code("nickname.equal.previous")
                    .message("닉네임이 이전과 같습니다.")
                    .httpStatus(HttpStatus.BAD_REQUEST).build();
        }

        member.changeNickname(updateNickname);
    }

    /**
     * 패스워드 수정
     * @param memberId
     * @param memberDTO
     * @exception : 일치하는 memberId 없으면 -> CustomException.globalError(ErrorCode.ID_NOT_FOUND)
     * @exception : 기존 비밀번호와 DB 비밀번호가 불일치하면 -> CustomException.globalError(ErrorCode.PASSWORD_NOT_EQUAL);
     * @exception : 기존 비밀번호와 새 비밀번호가 일치하면 -> CustomException.localError
     */
    public void updatePassword(Long memberId, MemberDTO.UpdatePassword memberDTO) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
            CustomException.globalError(ErrorCode.ID_NOT_FOUND)
        );

        //DB에 저장된 비밀번호와 사용자가 입력한 기존 비밀번호 같아야 새 비밀번호로 변경 가능하다.
        if(! bCryptPasswordEncoder.matches(memberDTO.getOldPassword(), member.getPassword())){
            throw CustomException.globalError(ErrorCode.PASSWORD_NOT_EQUAL);
        }

        //기존 비밀번호와 새 비밀번호가 같다면 변경하지 않는다.
        if(memberDTO.getOldPassword().equals(memberDTO.getNewPassword())){
            throw CustomException.localError()
                    .cause("newPassword")
                    .code("oldPassword.equal.newPassword")
                    .message("기존 비밀번호와 새 비밀번호가 일치합니다.")
                    .httpStatus(HttpStatus.BAD_REQUEST).build();
        }

        member.changePassword(memberDTO.getNewPassword());
    }


    /**
     * 회원조회
     * @param memberId
     * @exception : 일치하는 memberId 없으면 -> CustomException.globalError(ErrorCode.ID_NOT_FOUND)
     * @return MemberDTO.Response
     */
    public MemberDTO.Response detail(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                CustomException.globalError(ErrorCode.ID_NOT_FOUND)
        );

        MemberDTO.Response response = MemberDTO.entityToDto(member);
        return response;
    }
}
