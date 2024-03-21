package com.example.demo.src.admin;

import com.example.demo.common.history.DataHistoryService;
import com.example.demo.common.history.model.GetDataHistoryRes;
import com.example.demo.common.request.PageDto;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.admin.model.AdminRequest;
import com.example.demo.src.admin.model.HistorySearchCriteria;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.GetUserDetailRes;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.UserSearchCriteria;
import com.example.demo.src.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final AdminService adminService;
    private final DataHistoryService dataHistoryService;

    /**
     * 유저 다건 조회 API
     * [GET] /users/search
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/users/search")
    public BaseResponse<List<GetUserRes>> searchUsers(UserSearchCriteria userSearchCriteria, PageDto pageDto) {
        List<GetUserRes> getUsersRes = userService.searchUsers(userSearchCriteria, PageRequest.of(pageDto.getPage(), pageDto.getSize()));
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
    @GetMapping("/history/search")
    public BaseResponse<List<GetDataHistoryRes>> searchHistory(HistorySearchCriteria historySearchCriteria, PageDto pageDto) {
        List<GetDataHistoryRes> getDataHistoryRes = dataHistoryService.searchHistory(historySearchCriteria, PageRequest.of(pageDto.getPage(), pageDto.getSize()));
        return new BaseResponse<>(getDataHistoryRes);
    }

    /**
     * 회원 상세 조회 API
     * [GET] /users/:userId
     * @return BaseResponse<GetUserDetailRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/users/{userId}")
    public BaseResponse<GetUserDetailRes> getUser(@PathVariable("userId") Long userId) {
        GetUserDetailRes getUserDetailRes = userService.getUserDetail(userId);
        return new BaseResponse<>(getUserDetailRes);
    }

}
