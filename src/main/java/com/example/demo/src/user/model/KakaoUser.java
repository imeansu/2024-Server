package com.example.demo.src.user.model;

import com.example.demo.common.Constant;
import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

import static com.example.demo.common.Constant.SocialLoginType.KAKAO;

//카카오로 액세스 토큰을 보내 받아올 구글에 등록된 사용자 정보
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUser {
    public String id;
    public ZonedDateTime connectedAt;
    public KakaoAccount kakaoAccount;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class KakaoAccount {
        private boolean profileNicknameNeedsAgreement;
        private boolean hasEmail;
        private boolean emailNeedsAgreement;
        private boolean isEmailValid;
        private boolean isEmailVerified;
        private boolean hasBirthday;
        private boolean birthdayNeedsAgreement;
        private String email;
    }
}
