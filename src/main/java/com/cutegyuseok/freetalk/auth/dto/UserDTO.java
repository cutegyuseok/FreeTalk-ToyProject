package com.cutegyuseok.freetalk.auth.dto;

import com.cutegyuseok.freetalk.auth.enumType.UserRole;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

public class UserDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ApiModel(value = "토큰에 담길 정보")
    public static class UserAccessDTO {
        @ApiModelProperty(value = "이메일 ", required = true)
        private String email;

        @ApiModelProperty(value = "권한 ", required = true)
        private String role;

        public UserAccessDTO(Claims claims) {
            this.email = claims.get("email", String.class);
            this.role = claims.get("role", String.class);
        }

        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singleton(new SimpleGrantedAuthority(this.role));
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ApiModel(value = "회원가입시 입력할 정보")
    public static class UserSignupReqDTO {
        @ApiModelProperty(value = "이메일 ", required = true)
        private String email;
        @ApiModelProperty(value = "이름 ", required = true)
        private String name;
        @ApiModelProperty(value = "비밀번호 ", required = true)
        private String password;
        @ApiModelProperty(value = "핸드폰 번호", required = true)
        private String phone;
        @ApiModelProperty(value = "생년월일", required = true)
        private LocalDate birthday;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ApiModel(value = "프로필 입력시 입력할 정보")
    public static class UserProfileReqDTO {
        @ApiModelProperty(value = "닉네임", required = true)
        private String nickName;
        @ApiModelProperty(value = "프로필 사진", required = true)
        private String profileImage;
        @ApiModelProperty(value = "자기소개", required = true)
        private String selfIntroduction;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "이메일 확인 코드 입력")
    public static class EmailConfirmationCodeReqDTO {

        @ApiModelProperty(value = "이메일 확인 코드")
        private String code;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ApiModel(value = "로그인")
    public static class LoginReqDTO {

        @ApiModelProperty(value = "이메일 ", required = true)
        private String userEmail;

        @ApiModelProperty(value = "비밀번호 ", required = true)
        private String userPassword;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "권한 확인")
    public static class RoleCheckDTO {

        @ApiModelProperty(value = "권한")
        private UserRole role;
    }

}
