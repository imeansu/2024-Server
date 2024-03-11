package com.example.demo.src.user.entity;

import com.example.demo.common.Constant;
import com.example.demo.common.Constant.SocialLoginType;
import com.example.demo.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

import static com.example.demo.common.Constant.SocialLoginType.KAKAO;

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
    private SocialLoginType oauthType;

    @Column(nullable = false, length = 12)
    private String phoneNumber;

    @Column
    private LocalDate birthday;

    @Builder
    public User(Long id, String loginId, String passwordHash, boolean isOAuth, SocialLoginType oauthType, String name, String phoneNumber, LocalDate birthday) {
        this.id = id;
        this.loginId = loginId;
        this.passwordHash = passwordHash;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isOAuth = isOAuth;
        this.oauthType = oauthType;
        this.birthday = birthday;
    }

    public void updateName(String username) {
        this.name = username;
    }

    public void deleteUser() {
        this.state = State.INACTIVE;
    }

}
