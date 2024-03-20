package com.example.demo.src.user.model;

import com.example.demo.common.Constant.UserStatus;
import com.example.demo.src.user.validation.EnumConstraint;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchCriteria {
    private String name;
    private String loginId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    @EnumConstraint(message = "올바르지 않은 회원 상태입니다.", enumClass = UserStatus.class, nullable = true)
    private String userStatus;
}
