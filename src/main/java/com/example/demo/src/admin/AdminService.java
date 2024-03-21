package com.example.demo.src.admin;


import com.example.demo.common.Constant;
import com.example.demo.common.Constant.DataEvent;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.history.DataHistoryService;
import com.example.demo.common.history.entity.DataHistory;
import com.example.demo.src.admin.model.AdminRequest;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.common.Constant.EventType.ADMIN;
import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_USER;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final DataHistoryService dataHistoryService;
    private final ObjectMapper mapper;

    public void restrictUser(Long userId, AdminRequest adminRequest) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.setUserStatus(Constant.UserStatus.RESTRICTED);

        dataHistoryService.save(DataHistory.builder()
                .userId(userId)
                .eventType(ADMIN)
                .dataEvent(DataEvent.DEACTIVATE_USER_BY_ADMIN)
                .data(mapper.convertValue(user, Object.class))
                .reason("by " + adminRequest.getAdminUser() + " / " + adminRequest.getReason())
                .build());
    }
}
