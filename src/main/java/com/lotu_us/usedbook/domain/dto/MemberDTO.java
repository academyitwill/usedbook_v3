package com.lotu_us.usedbook.domain.dto;

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
}
