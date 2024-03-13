package com.example.demo.src.user.model;

import com.example.demo.common.Constant;
import com.example.demo.common.Constant.SocialLoginType;
import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserReq {
    @NotNull(message = "아이디를 입력해주세요.")
    @Size(min = 1, max = 20, message = "아이디는 최소 1자, 최대 20자 입니다.")
    private String loginId;
    @NotNull(message = "비밀번호를 입력해주세요.")
    @Size(min = 7, max = 20, message = "비밀번호는 최소 7자, 최대 20자 입니다.")
    private String password;
    @NotNull(message = "가입 유형 정보가 필요합니다.")
    private boolean isOAuth;
    private SocialLoginType oauthType;
    @NotNull(message = "이름을 입력해주세요.")
    @Size(min = 1, max = 20, message = "이름은 최소 1자, 최대 20자 입니다.")
    private String name;
    @NotNull(message = "휴대폰 번호를 입력해주세요.")
    @Size(min = 12, max = 12, message = "휴대폰 번호를 국가번호를 포함하여 12자 입력해주세요.")
    private String phoneNumber;
    @NotNull(message = "생년월일을 입력해주세요.")
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
