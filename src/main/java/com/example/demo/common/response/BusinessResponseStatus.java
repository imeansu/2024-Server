package com.example.demo.common.response;

import lombok.Getter;

@Getter
public enum BusinessResponseStatus {
    RESTRICTED_USER(false, 499, "차단된 유저입니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BusinessResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}