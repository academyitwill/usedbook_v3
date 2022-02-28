package com.lotu_us.usedbook.domain.dto;

import com.lotu_us.usedbook.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MemberDTO {

    @Getter
    @ToString
    public static class Join{
        @NotBlank(message = "{nickname.NotBlank}")
        @Size(max = 10, message = "{nickname.SizeMax}")
        private String nickname;

        @NotBlank
        private String email;

        @NotBlank(message = "{password.NotBlank}")
        //@Pattern(regexp = PasswordRegexpConfig.reg, message = "{password.Pattern}")
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
    public static class UpdatePassword{
        @NotBlank(message = "{password.NotBlank}")
        //@Pattern(regexp = PasswordRegexpConfig.reg, message = "{password.Pattern}")
        private String oldPassword;

        @NotBlank(message = "{password.NotBlank}")
        //@Pattern(regexp = PasswordRegexpConfig.reg, message = "{password.Pattern}")
        private String newPassword;

        public UpdatePassword(String oldPassword, String newPassword) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }
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
