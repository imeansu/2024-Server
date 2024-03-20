package com.example.demo.src.admin;

import com.example.demo.common.history.entity.DataHistory;
import com.example.demo.common.history.model.GetDataHistoryRes;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.admin.model.AdminRequest;
import com.example.demo.src.admin.model.HistorySearchCriteria;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.UserSearchCriteria;
import com.example.demo.src.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final AdminService adminService;

    /**
     * 유저 다건 조회 API
     * [GET] /users/search
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/users/search")
    public BaseResponse<List<GetUserRes>> searchUsers(UserSearchCriteria userSearchCriteria, Pageable pageable) {
        List<GetUserRes> getUsersRes = userService.searchUsers(userSearchCriteria, pageable);
        return new BaseResponse<>(getUsersRes);
    }

    /**
     * 유저 정지 API
     * [PATCH] /users/:userId/restrict
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/users/{userId}/restrict")
    public BaseResponse<String> restrictUser(@PathVariable("userId") Long userId, @RequestBody AdminRequest adminRequest){

        adminService.restrictUser(userId, adminRequest);

        String result = "유저 정지 성공";
        return new BaseResponse<>(result);
    }

    /**
     * 데이터 히스토리 조회 API
     * [PATCH] /history
     * @return BaseResponse<List<DataHistory>>
     */
    @ResponseBody
    @GetMapping("/history")
    public BaseResponse<List<GetDataHistoryRes>> searchHistory(HistorySearchCriteria historySearchCriteria, Pageable pageable) {
        List<GetDataHistoryRes> dataHistories = adminService.searchHistory(historySearchCriteria, pageable);
        return new BaseResponse<>(dataHistories);
    }

}
