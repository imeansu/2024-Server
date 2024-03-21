package com.example.demo.src.user.model;

import com.example.demo.common.Constant;
import com.example.demo.src.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GetUserDetailRes {
    private Long id;
    private String loginId;
    private String name;
    private boolean isOAuth;
    private Constant.SocialLoginType socialLoginType;
    private String oauthId;
    private String phoneNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Integer userStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastLoginAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastLawNotifiedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public GetUserDetailRes(User user) {
        this.id = user.getId();
        this.loginId = user.getLoginId();
        this.name = user.getName();
        this.isOAuth = user.isOAuth();
        this.socialLoginType = user.getSocialLoginType();
        this.oauthId = user.getOauthId();
        this.phoneNumber = user.getPhoneNumber();
        this.birthday = user.getBirthday();
        this.userStatus = user.getUserStatus();
        this.lastLoginAt = user.getLastLoginAt();
        this.lastLawNotifiedAt = user.getLastLawNotifiedAt();
        this.createdAt = user.getCreatedAt();
    }
}
