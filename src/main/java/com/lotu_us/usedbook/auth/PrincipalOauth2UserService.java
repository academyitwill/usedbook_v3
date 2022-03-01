package com.lotu_us.usedbook.auth;

import com.lotu_us.usedbook.auth.userinfo.GoogleUserInfo;
import com.lotu_us.usedbook.auth.userinfo.KakaoUserInfo;
import com.lotu_us.usedbook.auth.userinfo.NaverUserInfo;
import com.lotu_us.usedbook.auth.userinfo.Oauth2UserInfo;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.domain.enums.Role;
import com.lotu_us.usedbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User =  super.loadUser(userRequest);

        Oauth2UserInfo oauth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId();    //google 또는 naver

        if(provider.equals("google")){
            oauth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(provider.equals("naver")){
            oauth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }else if(provider.equals("kakao")){
            oauth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }

        String email = oauth2UserInfo.getEmail();                        //이메일
        String defaultNickname = oauth2UserInfo.getProvider() + "_user" + UUID.randomUUID().toString().substring(0, 6);
        String password = bCryptPasswordEncoder.encode("password" + UUID.randomUUID().toString().substring(0, 6));
        // 비밀번호. 사용자가 입력한 적은 없지만 만들어준다.

        Member findMember = memberRepository.findByEmail(email).orElse(null);

        //DB에 없는 사용자라면 회원가입처리
        if(findMember == null){
            findMember = Member.JoinOAuth2()
                    .nickname(defaultNickname)
                    .password(password)
                    .email(email)
                    .provider(provider).build();
            memberRepository.save(findMember);
        }

        return new PrincipalDetails(findMember, oauth2UserInfo);
    }
}
