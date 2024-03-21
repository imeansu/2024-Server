package com.example.demo.src.user.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PasswordResetReq {
    @NotNull(message = "비밀번호를 입력해주세요.")
    @Size(min = 7, max = 20, message = "비밀번호는 최소 7자, 최대 20자 입니다.")
    private String password;
}
