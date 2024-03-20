package com.example.demo.src.user.model;

import com.example.demo.src.user.validation.LoginIdConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostLoginReq {
    @Size(min = 1, max = 20, message = "아이디는 최소 1자, 최대 20자 입니다.")
    @LoginIdConstraint(message = " 사용자 이름에는 문자, 숫자, 밑줄 및 마침표만 사용할 수 있습니다.")
    private String loginId;
    @NotNull(message = "비밀번호를 입력해주세요.")
    @Size(min = 7, max = 20, message = "비밀번호는 최소 7자, 최대 20자 입니다.")
    private String password;
}
