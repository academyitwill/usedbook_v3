package com.lotu_us.usedbook.domain.dto;

import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.util.validation.annotation.Email;
import com.lotu_us.usedbook.util.validation.annotation.Nickname;
import com.lotu_us.usedbook.util.validation.annotation.Password;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class MemberDTO {

    @Getter
    @ToString
    public static class Join{
        @Nickname
        private String nickname;

        @Email
        private String email;

        //@Password
        private String password;

        @Builder
        public Join(String nickname, String email, String password) {
            this.nickname = nickname;
            this.email = email;
            this.password = password;
        }
    }


    @Getter
    @ToString
    public static class UpdatePassword {
//        @Password
        private String oldPassword;

        @Password
        private String newPassword;

        public UpdatePassword(String oldPassword, String newPassword) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }
    }


    @Getter
    @ToString
    public static class findPassword {
        @Email
        private String email;

        @Nickname
        private String nickname;
    }



    @Getter
    @ToString
    public static class Response{
        private Long id;
        private String nickname;
        private String email;
    }


    public static Response entityToDto(Member member) {
        Response response = new Response();
        response.id = member.getId();
        response.nickname = member.getNickname();
        response.email = member.getEmail();
        return response;
    }


}
