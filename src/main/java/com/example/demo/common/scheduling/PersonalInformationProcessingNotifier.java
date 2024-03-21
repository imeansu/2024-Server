package com.example.demo.common.scheduling;

import com.example.demo.common.Constant;
import com.example.demo.common.Constant.DataEvent;
import com.example.demo.common.history.DataHistoryService;
import com.example.demo.common.history.entity.DataHistory;
import com.example.demo.common.service.MessageSender;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.common.Constant.DataEvent.PERSONAL_INFO_NOTI;
import static com.example.demo.common.Constant.EventType.ADMIN;

@Profile("batch")
@Slf4j
@Component
@RequiredArgsConstructor
public class PersonalInformationProcessingNotifier {
    private final UserService userService;
    private final MessageSender messageSender;
    private final DataHistoryService dataHistoryService;
    @Value("classpath:personal-info-noti-template.txt")
    private Resource templateResource;
    private String PERSONAL_INFO_NOTI_TEMPLATE;

    @PostConstruct
    public void init() throws IOException {
        PERSONAL_INFO_NOTI_TEMPLATE = FileCopyUtils.copyToString(new InputStreamReader(templateResource.getInputStream(), StandardCharsets.UTF_8));
    }

    // 개인정보 처리 알림
    @Scheduled(cron = "0 0 11 * * *")  // 매일 11시에 실행
    @Transactional
    public void notifyPersonalInformationProcessing() {
        LocalDateTime now = LocalDateTime.now();
        List<User> targetUsers = userService.getNeedLawNotifyUsers(now);
        targetUsers.forEach( user -> {
            try {
                messageSender.send(user.getPhoneNumber(), PERSONAL_INFO_NOTI_TEMPLATE);
                user.updateLastLawNotifiedAt();
                Map<String, String> historyData = new HashMap() {{
                        put("lastLawNotifiedAt", user.getLastLawNotifiedAt().toString());
                        put("phoneNumber", user.getPhoneNumber());
                    }};
                logHistory(user.getId(), ADMIN, PERSONAL_INFO_NOTI, historyData, "개인정보 처리 알림");
            } catch (Exception e) {
                log.error("fail to notify personal information processing : userId: {}", user.getId(), e);
                logHistory(user.getId(), ADMIN, PERSONAL_INFO_NOTI, user, "개인정보 처리 알림 실패");
            }

        });
    }

    private void logHistory(Long userId, Constant.EventType eventType, DataEvent dataEvent, Object data, String reason) {
        dataHistoryService.save(DataHistory.builder()
                .userId(userId)
                .eventType(eventType)
                .dataEvent(dataEvent)
                .data(data)
                .reason(reason)
                .build());
    }
}
