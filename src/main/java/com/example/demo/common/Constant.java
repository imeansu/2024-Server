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
        DEACTIVATE_USER_BY_ADMIN,
        SIGN_UP,
        AGREE_TERMS, MODIFY_USER_NAME, LOGIN,
    }

    public enum EventType {
        USER,
        ADMIN
    }

    public static <T extends Enum<T>> T valueOfOrNull(Class<T> enumType, String value) {
        try {
            return Enum.valueOf(enumType, value);
        } catch (Exception e) {
            return null;
        }
    }
}

