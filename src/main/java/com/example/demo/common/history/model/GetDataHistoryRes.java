package com.example.demo.common.history.model;

import com.example.demo.common.Constant;
import com.example.demo.common.history.entity.DataHistory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetDataHistoryRes {
    private Long id;
    private Long userId;
    private Constant.EventType eventType;
    private Constant.DataEvent dataEvent;
    private String reason;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public GetDataHistoryRes(DataHistory dataHistory) {
        this.id = dataHistory.getId();
        this.userId = dataHistory.getUserId();
        this.eventType = dataHistory.getEventType();
        this.dataEvent = dataHistory.getDataEvent();
        this.reason = dataHistory.getReason();
        this.createdAt = dataHistory.getCreatedAt();
    }
}
