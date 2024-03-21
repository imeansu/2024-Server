package com.example.demo.src.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpReqDto {
    private Long userId;
    private String name;
    private String phoneNumber;
}
