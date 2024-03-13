package com.example.demo.common.response;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INPUT_VALUE_INVALID(401, "입력값이 올바르지 않습니다.");

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    int code;
    String message;
}
