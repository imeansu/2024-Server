package com.example.demo.common.history.entity;

import com.example.demo.common.Constant.DataEvent;
import com.example.demo.common.Constant.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document(collection = "data_history")
public class DataHistory<T> {
    @Id
    private String id;

    private Long userId;

    private EventType eventType;

    private DataEvent dataEvent;

    private T data;

    private String reason;

    private LocalDateTime createdAt;

    @Builder
    public DataHistory(Long userId, EventType eventType, DataEvent dataEvent, T data, String reason) {
        this.userId = userId;
        this.eventType = eventType;
        this.dataEvent = dataEvent;
        this.data = data;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
    }
}
