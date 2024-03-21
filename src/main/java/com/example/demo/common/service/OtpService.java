package com.example.demo.common.service;

import com.example.demo.common.util.RandomProvider;
import com.example.demo.src.user.dto.OtpReqDto;
import com.example.demo.src.user.model.OtpInfo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OtpService {
    private static final  Integer EXPIRE_MIN = 5;
    private Cache<String, OtpInfo> otpCache;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private RandomProvider randomProvider;
    private final int MAX_ACCESS_TIMES = 5;

    public OtpService() {
        otpCache = Caffeine.newBuilder()
                .expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
                .build();
    }

    public OtpInfo generateOtp(OtpReqDto otpReqDto){
        OtpInfo otpInfo = getRandomOTP(otpReqDto);
        String otpMessage = "[Gridge - OTP code] " + otpInfo.getOtp();
        log.info(otpMessage);
        messageSender.send(otpReqDto.getPhoneNumber(), otpMessage);
        return otpInfo;
    }

    private OtpInfo getRandomOTP(OtpReqDto otpReqDto) {
        String otp =  new DecimalFormat("000000")
                .format(randomProvider.getRandomInt(999999));

        OtpInfo otpInfo = OtpInfo.builder()
                .userId(otpReqDto.getUserId())
                .otp(otp)
                .accessTimes(0)
                .name(otpReqDto.getName())
                .phoneNumber(otpReqDto.getPhoneNumber())
                .build();
        otpCache.put(otpReqDto.getPhoneNumber(), otpInfo);
        return otpInfo;
    }
    //get saved otp
    public OtpInfo getCacheOtp(String key){
        OtpInfo otpInfo = otpCache.getIfPresent(key);
        if (otpInfo.addAndGetAccessTimes() > MAX_ACCESS_TIMES) {
            otpInfo.setOtp("fail >_<");
            return otpInfo;
        }
        return otpInfo;
    }

    //clear stored otp
    public void clearOtp(String key){
        otpCache.invalidate(key);
    }
}
