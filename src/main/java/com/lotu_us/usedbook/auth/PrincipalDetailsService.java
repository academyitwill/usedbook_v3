package com.lotu_us.usedbook.auth;

import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service    //반드시 기재해주자!!! loginProcessingUrl이 동작하지 않는다.
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member findMember = memberRepository.findByEmail(username).orElse(null);
        if(findMember != null){
            return new PrincipalDetails(findMember);
        }
        return null;
    }
}
