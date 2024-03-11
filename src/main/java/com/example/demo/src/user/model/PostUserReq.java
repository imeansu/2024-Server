package com.example.demo.src.user.model;

import com.example.demo.common.Constant;
import com.example.demo.common.Constant.SocialLoginType;
import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserReq {
    @NotBlank
    private String loginId;
    private String password;
    private boolean isOAuth;
    private SocialLoginType oauthType;
    private String name;
    private String phoneNumber;
    private LocalDate birthday;

    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .passwordHash(this.password)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .isOAuth(this.isOAuth)
                .oauthType(this.oauthType)
                .birthday(this.birthday)
                .build();
    }
}
