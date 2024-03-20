package com.example.demo.common.exceptions.business;

import com.example.demo.common.Constant.SocialLoginType;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class SocialLoginUserNotFoundException extends BaseException {
    private UnsignedUpSocialLoginUser result;
    public SocialLoginUserNotFoundException(BaseResponseStatus status) {
        super(status);
    }

    public SocialLoginUserNotFoundException(SocialLoginType socialLoginType, String code,String oauthId) {
        super(BaseResponseStatus.SOCIAL_LOGIN_USER_NOT_FOUND);
        this.result = new UnsignedUpSocialLoginUser(socialLoginType, code, oauthId);
    }

    @Override
    public UnsignedUpSocialLoginUser getResult() {
        return result;
    }

    @Getter
    @AllArgsConstructor
    public class UnsignedUpSocialLoginUser {
        private SocialLoginType socialLoginType;
        private String code;
        private String oauthId;
    }
}
