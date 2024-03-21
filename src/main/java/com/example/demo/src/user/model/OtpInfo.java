package com.example.demo.src.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpInfo {
    private Long userId;
    private String otp;
    private Integer accessTimes;
    private String name;
    private String phoneNumber;
    private boolean isSuccess;

    public Integer addAndGetAccessTimes() {
        return ++this.accessTimes;
    }
}
