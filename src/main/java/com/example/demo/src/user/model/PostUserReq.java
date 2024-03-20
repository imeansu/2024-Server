package com.example.demo.src.user.model;

import com.example.demo.common.Constant.TermsType;
import com.example.demo.common.Constant.UserStatus;
import com.example.demo.src.user.dto.PostUserDto;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.validation.DateConstraint;
import com.example.demo.src.user.validation.LoginIdConstraint;
import com.example.demo.src.user.validation.PhoneNumberConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserReq {
    @Size(min = 1, max = 20, message = "아이디는 최소 1자, 최대 20자 입니다.")
    @LoginIdConstraint(message = " 사용자 이름에는 문자, 숫자, 밑줄 및 마침표만 사용할 수 있습니다.")
    private String loginId;
    @NotNull(message = "비밀번호를 입력해주세요.")
    @Size(min = 7, max = 20, message = "비밀번호는 최소 7자, 최대 20자 입니다.")
    private String password;
    @NotNull(message = "이름을 입력해주세요.")
    @Size(min = 1, max = 20, message = "이름은 최소 1자, 최대 20자 입니다.")
    private String name;
    @PhoneNumberConstraint(message = "휴대폰 번호를 국가번호를 포함하여 12자 입력해주세요.")
    private String phoneNumber;
    @DateConstraint(message = "생년월일을 YYYY-MM-DD 형식으로 입력해주세요.")
    private String birthday;
    private Set<TermsType> terms;

    public PostUserDto toDto() {
        return PostUserDto.builder()
                .loginId(this.loginId)
                .password(this.password)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .birthday(this.birthday)
                .terms(this.terms)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .passwordHash(this.password)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .birthday(LocalDate.parse(this.birthday))
                .userStatus(UserStatus.NORMAL.getValue())
                .build();
    }
}
