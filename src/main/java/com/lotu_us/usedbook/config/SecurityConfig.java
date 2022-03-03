package com.lotu_us.usedbook.config;

import com.lotu_us.usedbook.auth.FormLoginFailureHandler;
import com.lotu_us.usedbook.auth.PrincipalOauth2UserService;
import com.lotu_us.usedbook.domain.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;
    private final FormLoginFailureHandler formLoginFailureHandler;
    private final String[] whitelist = {
            "/resources/**", "/css/**", "/js/**", "/img/**",
            "/oauth2", "/api/**",
            "/",
            "/login", "/join", "/joinOk", "/findPassword", "/findPasswordEmailSend",
            "/item/list", "/item/list/**", "/item/{itemId}"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //post요청 forbiddon에러 방지
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers(whitelist).permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/loginProcessing")
                .defaultSuccessUrl("/")
                .failureHandler(formLoginFailureHandler)
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
            .and()
            .oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
    }
}
