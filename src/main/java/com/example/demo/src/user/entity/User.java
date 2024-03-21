package com.example.demo.src.user.entity;

import com.example.demo.common.Constant.SocialLoginType;
import com.example.demo.common.Constant.UserStatus;
import com.example.demo.common.entity.BaseEntity;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "user") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class User extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String loginId;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private boolean isOAuth;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private SocialLoginType socialLoginType;

    @Column
    private String oauthId;

    @Column(nullable = false, length = 12)
    private String phoneNumber;

    @Column
    private LocalDate birthday;

    @Column(nullable = false)
    private Integer userStatus;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime lastLoginAt;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime lastLawNotifiedAt;

    @Builder
    public User(Long id, String loginId, String passwordHash, boolean isOAuth, SocialLoginType socialLoginType, String oauthId, String name, String phoneNumber, LocalDate birthday, Integer userStatus) {
        this.id = id;
        this.loginId = loginId;
        this.passwordHash = passwordHash;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isOAuth = isOAuth;
        this.socialLoginType = socialLoginType;
        this.oauthId = oauthId;
        this.birthday = birthday;
        this.userStatus = userStatus;
        this.lastLawNotifiedAt = LocalDateTime.now();
    }

    public void updateName(String username) {
        this.name = username;
    }

    public void resetPassword(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }
    public void updateLastLawNotifiedAt() {
        this.lastLawNotifiedAt = LocalDateTime.now();
    }

    public void deleteUser() {
        this.state = State.INACTIVE;
        setUserStatus(UserStatus.DEACTIVATE);
        clearUserStatus(UserStatus.NORMAL);
    }

    public void setUserStatus(UserStatus status) {
        if (userStatus == null) {
            userStatus = status.getValue();
        } else {
            userStatus |= status.getValue();
        }
    }

    public void clearUserStatus(UserStatus status) {
        if (userStatus != null) {
            userStatus &= ~status.getValue();
        }
    }

    public boolean hasUserStatus(UserStatus status) {
        return userStatus != null && (userStatus & status.getValue()) != 0;
    }

    public boolean canLogin() {
        if (state != State.ACTIVE) {
            throw new BaseException(BaseResponseStatus.NOT_FIND_USER);
        }

        if (hasUserStatus(UserStatus.DORMANCY)) {
            throw new BaseException(BaseResponseStatus.DORMANT_USER);
        }

        if (hasUserStatus(UserStatus.RESTRICTED)) {
            throw new BaseException(BaseResponseStatus.RESTRICTED_USER);
        }

        return true;
    }

}
