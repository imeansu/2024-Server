package com.example.demo.src.admin.model;

import com.example.demo.common.Constant;
import com.example.demo.src.user.validation.EnumConstraint;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorySearchCriteria {
    private Long userId;
    @EnumConstraint(message = "올바르지 않은 이벤트 타입입니다.", enumClass = Constant.EventType.class, nullable = true)
    private String eventType;
    @EnumConstraint(message = "올바르지 않은 데이터 이벤트 타입입니다.", enumClass = Constant.DataEvent.class, nullable = true)
    private String dataEvent;
    private String reason;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endAt;
}
