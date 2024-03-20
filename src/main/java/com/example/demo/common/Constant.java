package com.example.demo.common;

import lombok.Getter;

public class Constant {
    public enum SocialLoginType{
        GOOGLE,
        KAKAO,
        NAVER
    }

    public enum TermsType {
        SERVICE,
        DATA,
        LOCATION
    }

    @Getter
    public enum UserStatus {
        NORMAL(1),
        DORMANCY(2),
        RESTRICTED(4),
        DEACTIVATE(8);

        private final int value;

        UserStatus(int value) {
            this.value = value;
        }
    }

    public enum DataEvent {
        RESTRICT_USER_BY_ADMIN,
        DEACTIVATE_USER_BY_ADMIN
    }

    public enum EventType {
        USER,
        ADMIN
    }
}

