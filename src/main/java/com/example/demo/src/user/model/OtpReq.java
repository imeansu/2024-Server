package com.example.demo.src.user.model;

import com.example.demo.src.user.dto.OtpReqDto;
import com.example.demo.src.user.validation.PhoneNumberConstraint;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class OtpReq {
    @NotNull(message = "이름을 입력해주세요.")
    @Size(min = 1, max = 20, message = "이름은 최소 1자, 최대 20자 입니다.")
    private String name;
    @PhoneNumberConstraint(message = "휴대폰 번호를 국가번호를 포함하여 12자 입력해주세요.")
    private String phoneNumber;

    public OtpReqDto toDto(Long userId) {
        return OtpReqDto.builder()
                .userId(userId)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .build();
    }
}
