package com.example.demo.src.user.dto;

import com.example.demo.common.Constant;
import com.example.demo.common.Constant.SocialLoginType;
import com.example.demo.common.Constant.TermsType;
import com.example.demo.src.user.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUserDto {
    private String loginId;
    private String password;
    private boolean isOAuth;
    private SocialLoginType socialLoginType;
    private String oauthId;
    private String name;
    private String phoneNumber;
    private String birthday;
    private Set<TermsType> terms;

    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .passwordHash(this.password)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .isOAuth(this.isOAuth)
                .oauthId(this.oauthId)
                .socialLoginType(this.socialLoginType)
                .birthday(LocalDate.parse(this.birthday))
                .userStatus(Constant.UserStatus.NORMAL.getValue())
                .build();
    }
}