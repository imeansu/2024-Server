package com.example.demo.src.admin;


import com.example.demo.common.Constant;
import com.example.demo.common.Constant.DataEvent;
import com.example.demo.common.Constant.EventType;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.history.DataHistoryRepository;
import com.example.demo.common.history.DataHistoryRepositorySupport;
import com.example.demo.common.history.entity.DataHistory;
import com.example.demo.common.history.model.GetDataHistoryRes;
import com.example.demo.src.admin.model.AdminRequest;
import com.example.demo.src.admin.model.HistorySearchCriteria;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.common.Constant.EventType.ADMIN;
import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_USER;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final DataHistoryRepository dataHistoryRepository;
    private final DataHistoryRepositorySupport dataHistoryRepositorySupport;

    public void restrictUser(Long userId, AdminRequest adminRequest) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.setUserStatus(Constant.UserStatus.RESTRICTED);

        dataHistoryRepository.save(DataHistory.builder()
                .userId(userId)
                .eventType(ADMIN)
                .dataEvent(DataEvent.DEACTIVATE_USER_BY_ADMIN)
                .reason("by " + adminRequest.getAdminUser() + " / " + adminRequest.getReason())
                .build());
    }


    public List<GetDataHistoryRes> searchHistory(HistorySearchCriteria c, Pageable pageable) {
        EventType eventType = Constant.valueOfOrNull(EventType.class, c.getEventType());
        DataEvent dataEvent = Constant.valueOfOrNull(DataEvent.class, c.getEventType());

        Page<DataHistory> dataHistories = dataHistoryRepositorySupport.searchUsers(c.getUserId(), eventType, dataEvent, c.getCreatedAt(), pageable);
        return dataHistories.stream().map(GetDataHistoryRes::new).collect(Collectors.toList());
    }
}
